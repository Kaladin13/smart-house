package org.bakalover.iot

import org.redisson.api.RQueue

class Poller(
    private val id: Int,
    private val pollFrom: RQueue<String>,
    private val sendTo: Switch<String>,
) {
    suspend fun startPoll() {
        val chan = sendTo.getChannelById(id)
        while (true) {
            if (pollFrom.isNotEmpty()) {
                val taskBatch = pollFrom.poll(BATCH_SIZE);
                taskBatch.forEach { taskSubBatch ->
                    chan.send(taskSubBatch)
                }
            }
        }
    }
}