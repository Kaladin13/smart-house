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
const val TASKS = 200000

fun main(): Unit = runBlocking {

    // Redis
    val config = Config()
    config.useSingleServer().setAddress(REDIS_URL)
    val client = Redisson.create(config)
    var startTime: Long = 0

    launch {
        for (tasksCount in 1..TASKS step TASKS / 100) {

            val queue = client.getQueue<String>(CONSUME_QUEUE)
            startTime = System.currentTimeMillis()
            val tasks =
                List(tasksCount) { _ ->
                    Json.encodeToString(Request(Random.nextInt(K_HOUSES), "hi", "hjjjjjjjjjjjjjjjjjjji"))
                }
            queue.addAll(tasks);

            val queueGet = client.getQueue<String>(PUBLISH_QUEUE)
            var count = 0

            while (true) {
                if (queueGet.isNotEmpty()) {
                    val list = queueGet.poll(BATCH_SIZE)
                    count += list.size
                    if (count >= tasksCount) {
                        break
                    }
                }
            }
            println("${tasksCount};${count / ((System.currentTimeMillis() - startTime) / 1000)}")
        }
    }
}