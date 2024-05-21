package org.bakalover.iot

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.redisson.Redisson
import org.redisson.config.Config

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

//    var tasks = listOf(
//        Json.encodeToString(Request(1, 1, "light", "on")),
//        Json.encodeToString(Request(2, 1, "light", "off")),
//        Json.encodeToString(Request(3, 1, "climate", "off")),
//        Json.encodeToString(Request(4, 1, "climate", "get")),
//        Json.encodeToString(Request(5, 1, "climate", "set t 123123")),
//        Json.encodeToString(Request(6, 1, "climate", "set t 25")),
//        Json.encodeToString(Request(7, 1, "climate", "set h 13123sddfb")),
//        Json.encodeToString(Request(8, 1, "climate", "set h 90")),
//        Json.encodeToString(Request(9, 1, "climate", "get")),
//    )
//
//
    val queue = client.getQueue<String>(CONSUME_QUEUE)
//    queue.addAll(tasks)
//
    val queueGet = client.getQueue<String>(PUBLISH_QUEUE)
//    while (true) {
//        if (queueGet.isNotEmpty()) {
//            val list = queueGet.poll(BATCH_SIZE)
//            list.forEach { println(it) }
//            break
//        }
//    }

    var tasks = listOf(

        Json.encodeToString(Request(10, 1, "cleaner", "sdfgu234")),
        Json.encodeToString(Request(12, 2, "cleaner", "clean")),
        Json.encodeToString(Request(13, 1, "cleaner", "clean")),
        Json.encodeToString(Request(14, 2, "cleaner", "clean")),
        Json.encodeToString(Request(15, 1, "cleaner", "clean")),
        Json.encodeToString(Request(16, 2, "cleaner", "clean")),
        Json.encodeToString(Request(17, 1, "cleaner", "clean")),
        Json.encodeToString(Request(18, 2, "cleaner", "clean")),

        )

    queue.addAll(tasks)
    while (true) {
        if (queueGet.isNotEmpty()) {
            val list = queueGet.poll(BATCH_SIZE)
            list.forEach { println(it) }
            break
        }
    }
}