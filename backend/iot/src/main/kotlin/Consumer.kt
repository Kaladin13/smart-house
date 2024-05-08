package org.bakalover.iot

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select


class Consumer(
    private val consumeFrom: Switch<String>,
    private val sendRequestsTo: Switch<String>,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun startConsume() {
        val taskBatch = mutableListOf<String>()
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

    private suspend fun processBatch(batch: List<String>) {
        val batches = batch.groupBy { it }
        batch.forEach { el ->
            sendRequestsTo.getChannelById(el.toInt()).send(el)
        }
    }
}