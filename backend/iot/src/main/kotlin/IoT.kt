package org.bakalover.iot

import kotlinx.coroutines.*
import org.redisson.Redisson
import org.redisson.config.Config
import java.util.concurrent.Executors

const val PUBLISH_QUEUE = "publish_queue"
const val CONSUME_QUEUE = "publish_queue"
const val REDIS_URL = "redis://127.0.0.1:6379"
const val K_POLLERS = 3
const val K_PUBLISHERS = 3
const val K_CONSUMERS = 5
const val K_HOUSES = 10000
const val BATCH_SIZE = 256

fun main() {

    // Maximize CPU utilization: Sharding + WorkStealing + Minimum context switches
    val kWorkers = Runtime.getRuntime().availableProcessors()
    val dispatcher = Executors.newWorkStealingPool(kWorkers).asCoroutineDispatcher()
    val scope = CoroutineScope(dispatcher)

    // Redis
    val config = Config()
    config.useSingleServer().setAddress(REDIS_URL)
    val client = Redisson.create(config)


    val pollConsumeSwitch = Switch<String>(K_POLLERS)
    val consumeHouseSwitch = Switch<String>(K_HOUSES)
    val housePublishSwitch = Switch<String>(K_HOUSES)

    val houseRegistry = HouseRegistry(consumeHouseSwitch, housePublishSwitch)

    // Polling
    val pollJobs = List(K_POLLERS) { id ->
        scope.launch {
            val poller = Poller(id, client.getQueue(CONSUME_QUEUE), pollConsumeSwitch)
            poller.startPoll()
        }
    }

    // Processing
    val consumerJobs = List(K_CONSUMERS) { _ ->
        scope.launch {
            val consumer = Consumer(pollConsumeSwitch, consumeHouseSwitch)
            consumer.startConsume()
        }
    }

    (0..<K_HOUSES).forEach { id -> houseRegistry.deployNewHouse(id, scope) }


    // Publishing
    val publishJobs = List(K_PUBLISHERS) {
        scope.launch {
            val publisher = Publisher(client.getQueue(PUBLISH_QUEUE), housePublishSwitch)
            publisher.startPublish()
        }
    }


    // Graceful cancellation and shutdown for all coroutines
    Runtime.getRuntime().addShutdownHook(Thread {
        runBlocking { // Blocking is ok
            pollJobs.forEach { it.cancelAndJoin() }
            consumerJobs.forEach { it.cancelAndJoin() }
            houseRegistry.cancelAndJoin()
            publishJobs.forEach { it.cancelAndJoin() }
        }
        client.shutdown()
    })
}
