package org.bakalover.iot

import kotlinx.coroutines.channels.Channel
import org.redisson.api.RedissonClient

class Poller(
    private val pollFrom: RedissonClient,
    private val sendTo: Channel<String>,
) {
    suspend fun startPoll() {
        while (true) {
            val tasks = pollFrom.getQueue<String>(CONSUME_CHANNEL).poll(BATCH_SIZE)
            tasks.forEach { task ->
                sendTo.send(task)
            }
        }
    }
}