package org.bakalover.iot

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bakalover.iot.message.Response
import org.redisson.api.RQueue

class Publisher(
    private val publishTo: RQueue<String>,
    private val updatesFrom: Switch<Response>,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun startPublish() {
        val taskBatch = mutableListOf<Response>()
        while (true) {
            select { // Try to receive from all houses
                updatesFrom.getAllChannels().forEachIndexed { _, channel ->
                    channel.onReceive { task ->
                        taskBatch.add(task)
                        if (taskBatch.size >= BATCH_SIZE) {
                            publishBatch(taskBatch)
                            taskBatch.clear()
                        }
                    }
                }
                onTimeout(TIMEOUT) {
                    publishBatch(taskBatch) // Publish what we have at the moment
                    taskBatch.clear()
                }
            }
        }
    }

    private fun publishBatch(batch: List<Response>) {
        publishTo.addAll(batch.map { it -> Json.encodeToString(it) })
    }
}