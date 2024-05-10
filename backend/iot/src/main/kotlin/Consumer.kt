package org.bakalover.iot

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select
import org.bakalover.iot.message.Request


class Consumer(
    private val consumeFrom: Switch<Request>,
    private val sendRequestsTo: Switch<Request>,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun startConsume() {
        val taskBatch = mutableListOf<Request>()
        while (true) {
            select { // Try receiving from all
                consumeFrom.getAllChannels().forEachIndexed { _, channel ->
                    channel.onReceive { task ->
                        taskBatch.add(task)
                        if (taskBatch.size >= ((BATCH_SIZE * K_POLLERS) / K_CONSUMERS)) { // Balance batch
                            processBatch(taskBatch)
                            taskBatch.clear()
                        }
                    }
                }
                onTimeout(TIMEOUT) { // All channels are starving
                    processBatch(taskBatch) // Flushing what we have at the moment
                    taskBatch.clear()
                }
            }
        }
    }

    private suspend fun processBatch(batch: List<Request>) {
        batch.forEach { el ->
            sendRequestsTo.getChannelById(el.houseId).send(el)
        }
    }
}