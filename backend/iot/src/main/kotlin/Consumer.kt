package org.bakalover.iot

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select

const val TIMEOUT = 5L // in milliseconds
const val CONSUME_CHANNEL = "consume_topic"

@OptIn(ExperimentalCoroutinesApi::class)
class Consumer(
    private val consumeFrom: List<Channel<String>>,
    private val houseRegistry: HouseRegistry,
) {
    suspend fun startConsume() {
        val taskBatch = mutableListOf<String>()
        while (true) {
            select { // Circular walkthrough
                consumeFrom.forEachIndexed { _, channel ->
                    channel.onReceive { task ->
                        taskBatch.add(task)
                        if (taskBatch.size >= BATCH_SIZE) {
                            processBatch(taskBatch)
                            taskBatch.clear()
                        }
                    }
                }
                onTimeout(TIMEOUT) { // All channels are starving
                    processBatch(taskBatch)
                    taskBatch.clear()
                }
            }
        }
    }

    private fun processBatch(batch: List<String>) {

    }
}