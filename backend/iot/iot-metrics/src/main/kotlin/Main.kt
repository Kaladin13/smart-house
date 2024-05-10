package org.bakalover.iot

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.redisson.Redisson
import org.redisson.config.Config
import kotlin.random.Random

const val PUBLISH_QUEUE = "publish_queue"
const val CONSUME_QUEUE = "consume_queue"
const val REDIS_URL = "redis://127.0.0.1:6379"
const val K_HOUSES = 1500
const val BATCH_SIZE = 256
const val TASKS = 100000

fun main(): Unit = runBlocking {

    // Redis
    val config = Config()
    config.useSingleServer().setAddress(REDIS_URL)
    val client = Redisson.create(config)
    var startTime: Long = 0

    launch {
        val queue = client.getQueue<String>(CONSUME_QUEUE)
        startTime = System.currentTimeMillis()
        (0..<TASKS).forEach { _ ->
            queue.offer(Json.encodeToString(Request(Random.nextInt(K_HOUSES), "hi", "hjjjjjjjjjjjjjjjjjjji")))
        }
    }

    launch {

        val queue = client.getQueue<String>(PUBLISH_QUEUE)
        var count = 0

        while (true) {
            if (queue.isNotEmpty()) {
                val list = queue.poll(BATCH_SIZE)
                count += list.size
                if (count >= TASKS) {
                    break
                }
            }
        }
        println("rps: ${count / ((System.currentTimeMillis() - startTime) / 1000)} requests")
    }
}