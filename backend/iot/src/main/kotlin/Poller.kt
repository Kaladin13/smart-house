package org.bakalover.iot

import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.bakalover.iot.message.Request
import org.redisson.api.RQueue

class Poller(
    private val id: Int,
    private val pollFrom: RQueue<String>,
    private val sendTo: ISwitch<Request>,
) {
    private val backoff = Backoff()

    suspend fun startPoll() {
        val chan = sendTo.getChannel(id)
        while (true) {
            if (pollFrom.isNotEmpty()) {
                backoff.reset()
                val taskBatch = pollFrom.poll(BATCH_SIZE);
                taskBatch.forEach { task ->
                    chan.send(Json.decodeFromString<Request>(task))
                }
            } else {
                delay(backoff.nextDelay())
            }
        }
    }
}