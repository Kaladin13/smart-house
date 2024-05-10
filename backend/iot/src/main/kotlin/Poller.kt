package org.bakalover.iot

import kotlinx.serialization.json.Json
import org.bakalover.iot.message.Request
import org.redisson.api.RQueue

class Poller(
    private val id: Int,
    private val pollFrom: RQueue<String>,
    private val sendTo: Switch<Request>,
) {
    suspend fun startPoll() {
        val chan = sendTo.getChannelById(id)
        while (true) {
            if (pollFrom.isNotEmpty()) {
                val taskBatch = pollFrom.poll(BATCH_SIZE);
                taskBatch.forEach { task ->
                    chan.send(Json.decodeFromString<Request>(task))
                }
            }
        }
    }
}