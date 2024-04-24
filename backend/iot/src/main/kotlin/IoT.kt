package org.bakalover.iot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.redisson.Redisson
import org.redisson.config.Config
import java.util.concurrent.Executors

const val PUBLISH_QUEUE = "publish_queue"
const val CONSUME_QUEUE = "publish_queue"
const val REDIS_URL = "redis://127.0.0.1:6379"
const val K_POLLERS = 3
const val K_PUBLISHERS = 3
const val K_CONSUMERS = 5
const val K_HOUSES = 1000
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


    val houseRegistry = HouseRegistry()

    val pollConsumeSwitch = Switch<String>(K_POLLERS)
    val consumeHouseSwitch = Switch<String>(K_HOUSES)
    val housePublishSwitch = Switch<String>(K_HOUSES)

    // Polling
    val pollJobs = List(K_POLLERS) { id ->
        scope.launch {
            val poller = Poller(id, client.getQueue<String>(CONSUME_QUEUE), pollConsumeSwitch)
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

    // TODO: deploy houses

    // Publishing
    val publishJobs = List(K_PUBLISHERS) {
        scope.launch {
            val publisher = Publisher(client.getQueue<String>(PUBLISH_QUEUE), housePublishSwitch)
            publisher.startPublish()
        }
    }

    runBlocking {
        pollJobs.forEach { it.join() }
        consumerJobs.forEach { it.join() }
        publishJobs.forEach { it.join() }
    }


    // Graceful redis client shutdown
    Runtime.getRuntime().addShutdownHook(Thread {
        client.shutdown()
    })
}
