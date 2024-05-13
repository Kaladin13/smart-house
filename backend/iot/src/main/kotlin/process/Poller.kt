package org.bakalover.iot.process

import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import org.bakalover.iot.BATCH_SIZE
import org.bakalover.iot.message.Request
import org.bakalover.iot.pipeline.ISwitch
import org.redisson.api.RQueue

class Poller(
    private val id: Int,
    private val pollFrom: RQueue<String>,
) : IBorderProcess<Request> {
    private val backoff = Backoff()
    private lateinit var job: Job

    override fun doProcess(switch: ISwitch<Request>, scope: CoroutineScope) {
        job = scope.launch {
            val channel = switch.getChannel(id)
            while (true) {
                if (pollFrom.isNotEmpty()) {
                    backoff.reset()
                    val taskBatch = pollFrom.poll(BATCH_SIZE);
                    taskBatch.forEach { task ->
                        channel.send(Json.decodeFromString<Request>(task))
                    }
                } else {
                    delay(backoff.nextDelay())
                }
            }
        }
    }

    override suspend fun abort() {
        job.cancelAndJoin()
    }
}