package org.bakalover.iot

import kotlinx.coroutines.channels.Channel
import org.redisson.api.RQueue

class Poller(
    private val pollFrom: RQueue<String>,
    private val sendTo: Channel<String>,
) {
    suspend fun startPoll() {
        while (true) {
            val tasks = pollFrom.poll(BATCH_SIZE)
            tasks.forEach { task ->
                sendTo.send(task)
            }
        }
    }
}