package org.bakalover.iot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.bakalover.iot.message.Request
import org.bakalover.iot.message.Response
import org.bakalover.iot.pipeline.BorderStage
import org.bakalover.iot.pipeline.IntermediateStage
import org.bakalover.iot.pipeline.Pipeline
import org.bakalover.iot.pipeline.Switch
import org.bakalover.iot.process.Consumer
import org.bakalover.iot.process.House
import org.bakalover.iot.process.Poller
import org.bakalover.iot.process.Publisher
import org.redisson.Redisson
import org.redisson.config.Config
import java.util.concurrent.Executors

const val PUBLISH_QUEUE = "request_house"
const val CONSUME_QUEUE = "response_house"
const val REDIS_URL = "redis://127.0.0.1:6379"

//Minimum context switches
const val K_POLLERS = 3
const val K_CONSUMERS = 3
const val K_PUBLISHERS = 3
const val K_HOUSES = 1500
const val BATCH_SIZE = 256

fun main() {

    // Maximize CPU utilization: Sharding + WorkStealing
    val kWorkers = Runtime.getRuntime().availableProcessors()
    val dispatcher = Executors.newWorkStealingPool(kWorkers).asCoroutineDispatcher()
    val scope = CoroutineScope(dispatcher)

    // Redis
    val config = Config()
    config.useSingleServer().setAddress(REDIS_URL)
    val client = Redisson.create(config)


    // Pipeline
    val pollers = List(K_POLLERS) { id ->
        Poller(id, client.getQueue(CONSUME_QUEUE))
    }
    val consumers = List(K_CONSUMERS) {
        Consumer()
    }
    val houses = List(K_HOUSES) { id ->
        House(id)
    }
    val publishers = List(K_PUBLISHERS) {
        Publisher(client.getQueue(PUBLISH_QUEUE))
    }

    val pcSwitch = Switch<Request>(K_POLLERS)
    val chSwitch = Switch<Request>(K_HOUSES)
    val hpSwitch = Switch<Response>(K_HOUSES)

    val firstStage = BorderStage(pollers, pcSwitch, scope)
    val secondStage = IntermediateStage(consumers, pcSwitch, chSwitch, scope)
    val thirdStage = IntermediateStage(houses, chSwitch, hpSwitch, scope)
    val fourthStage = BorderStage(publishers, hpSwitch, scope)

    val pipeline = Pipeline(listOf(firstStage, secondStage, thirdStage, fourthStage))

    pipeline.launch()

    // Graceful cancellation and shutdown for all coroutines
    Runtime.getRuntime().addShutdownHook(Thread {
        runBlocking {
            pipeline.abort()
        }
        client.shutdown()
    })
}
