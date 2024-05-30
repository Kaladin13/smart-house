package org.example.subscibers

import org.example.logs.LokiLogger
import org.redisson.Redisson
import org.redisson.api.RQueue
import org.redisson.api.RTopic
import org.redisson.config.Config

const val PUBLISH_QUEUE = "request_house"
const val CONSUME_QUEUE = "response_house"
const val REDIS_URL = "redis://127.0.0.1:6379"

class RedisConsumerSubscriber() : RedisSubscriber {
    private val redisQueue: RQueue<String>
    private val redisTopic: RTopic

    private lateinit var lokiLogger: LokiLogger

    constructor(lokiLogger: LokiLogger) : this() {
        this.lokiLogger = lokiLogger
    }

    init {
        val config = Config()
        config.useSingleServer().setAddress(REDIS_URL)

        val client = Redisson.create(config)

        redisQueue = client.getQueue(CONSUME_QUEUE)
        redisTopic = client.getTopic(PUBLISH_QUEUE)
    }

    override fun startSubscription() {
        redisTopic.addListener(
            String::class.java
        ) { p0, p1 -> lokiLogger.log(p1) }
    }
}