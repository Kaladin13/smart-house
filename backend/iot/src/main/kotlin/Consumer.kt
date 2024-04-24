package org.bakalover.iot

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select

const val TIMEOUT = 5L // in milliseconds

class Consumer(
    private val consumeFrom: Switch<String>,
    private val sendRequestsTo: Switch<String>,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun startConsume() {
        val taskBatch = mutableListOf<String>()
        while (true) {
            select { // Try to receive from all
                consumeFrom.getAllChannels().forEachIndexed { _, channel ->
                    channel.onReceive { task ->
                        taskBatch.add(task)
                        if (taskBatch.size >= BATCH_SIZE) {
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

    private fun processBatch(batch: List<String>) {
        // Get id
        // Send update to switch using house id
    }
}