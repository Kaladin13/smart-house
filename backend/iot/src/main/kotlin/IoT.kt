package org.bakalover.iot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.redisson.Redisson
import org.redisson.config.Config
import java.util.concurrent.Executors

const val PUBLISH_CHANNEL = "publish_topic"
const val REDIS_URL = "redis://127.0.0.1:6379"
const val K_POLLERS = 3
const val K_CONSUMERS = 5
const val BATCH_SIZE = 256

fun main(args: Array<String>) {

    // Maximize CPU utilization: Sharding + WorkStealing + Minimum context switches
    val kWorkers = Runtime.getRuntime().availableProcessors();
    val dispatcher = Executors.newWorkStealingPool(kWorkers).asCoroutineDispatcher()
    val scope = CoroutineScope(dispatcher)

    // Single Producer Multiple Consumer pattern for EACH channel
    val commutation = List(K_POLLERS) { Channel<String>(capacity = Channel.UNLIMITED) }

    // Redis
    val config: Config = Config()
    config.useSingleServer().setAddress(REDIS_URL)
    val client = Redisson.create(config)


    val houseRegistry = HouseRegistry()

    // Polling
    val pollJobs = commutation.mapIndexed { _, channel ->
        scope.launch {
            val poller = Poller(client, channel)
            poller.startPoll()
        }
    }

    // Processing
    val consumerJobs = List(K_CONSUMERS) {
        scope.launch {
            val consumer = Consumer(commutation, houseRegistry)
            consumer.startConsume()
        }
    }

    runBlocking {
        pollJobs.forEach { it.join() }
        consumerJobs.forEach { it.join() }
    }


    // Graceful redis client shutdown
    Runtime.getRuntime().addShutdownHook(Thread {
        client.shutdown()
    })
}

private fun processBatch(messages: List<String>) {
    if (messages.isEmpty()) {
        return
    }
    // Process the batch of messages
    println("Received batch of messages: $messages")
}

