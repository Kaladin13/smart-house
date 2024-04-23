package org.bakalover.iot

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select
import org.redisson.api.RQueue

class Publisher(
    private val publishTo: RQueue<String>,
    private val updatesFrom: HouseRegistry,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun startPublish() {
        val taskBatch = mutableListOf<String>()
        while (true) {
            select { // Circular walkthrough
                updatesFrom.getReceiveCommunication().forEachIndexed { _, channel ->
                    channel.onReceive { task ->
                        taskBatch.add(task)
                        if (taskBatch.size >= BATCH_SIZE) {
                            publishBatch(taskBatch)
                            taskBatch.clear()
                        }
                    }
                }
                onTimeout(TIMEOUT) { // All channels are starving
                    publishBatch(taskBatch)
                    taskBatch.clear()
                }
            }
        }
    }

    private fun publishBatch(batch: List<String>) {
        publishTo.addAll(batch)
    }
}