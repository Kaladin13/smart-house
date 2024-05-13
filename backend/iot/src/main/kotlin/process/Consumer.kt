package org.bakalover.iot.process

import kotlinx.coroutines.*
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select
import org.bakalover.iot.BATCH_SIZE
import org.bakalover.iot.K_CONSUMERS
import org.bakalover.iot.K_POLLERS
import org.bakalover.iot.message.Request
import org.bakalover.iot.pipeline.ISwitch


class Consumer() : IIntermediateProcess<Request, Request> {
    private val backoff = Backoff()
    private lateinit var job: Job

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun doProcess(commutationFrom: ISwitch<Request>, commutationTo: ISwitch<Request>, scope: CoroutineScope) {
        job = scope.launch {
            val taskBatch = mutableListOf<Request>()
            while (true) {
                select { // Try receiving from all
                    commutationFrom.getAll().forEachIndexed { _, channel ->
                        channel.onReceive { task ->
                            taskBatch.add(task)
                            if (taskBatch.size >= ((BATCH_SIZE * K_POLLERS) / K_CONSUMERS)) { // Balance batch
                                backoff.reset()
                                processBatch(taskBatch, commutationTo)
                                taskBatch.clear()
                            }
                        }
                    }
                    onTimeout(backoff.nextDelay()) { // All channels are starving
                        processBatch(taskBatch, commutationTo) // Flushing what we have at the moment
                        taskBatch.clear()
                    }
                }
            }
        }
    }

    private suspend fun processBatch(batch: List<Request>, to: ISwitch<Request>) {
        batch.forEach { el ->
            to.getChannel(el.houseId).send(el)
        }
    }

    override suspend fun abort() {
        job.cancelAndJoin()
    }
}