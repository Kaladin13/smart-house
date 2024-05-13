package org.bakalover.iot.process

import kotlinx.coroutines.*
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bakalover.iot.BATCH_SIZE
import org.bakalover.iot.message.Response
import org.bakalover.iot.pipeline.ISwitch
import org.redisson.api.RQueue

class Publisher(
    private val publishTo: RQueue<String>,
) : IBorderProcess<Response> {
    private val backoff = Backoff()
    private lateinit var job: Job

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun doProcess(switch: ISwitch<Response>, scope: CoroutineScope) {
        job = scope.launch {
            val taskBatch = mutableListOf<Response>()
            while (true) {
                select { // Try to receive from all houses
                    switch.getAll().forEachIndexed { _, channel ->
                        channel.onReceive { task ->
                            taskBatch.add(task)
                            if (taskBatch.size >= BATCH_SIZE) {
                                backoff.reset()
                                publishBatch(taskBatch)
                                taskBatch.clear()
                            }
                        }
                    }
                    onTimeout(backoff.nextDelay()) {
                        publishBatch(taskBatch)
                        taskBatch.clear()
                    }
                }
            }
        }
    }

    private fun publishBatch(batch: List<Response>) {
        publishTo.addAll(batch.map { it -> Json.encodeToString(it) })
    }

    override suspend fun abort() {
        job.cancelAndJoin()
    }
}