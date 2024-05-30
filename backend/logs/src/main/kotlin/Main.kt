package org.example

import org.example.logs.LokiLogger
import org.example.subscibers.RedisConsumerSubscriber
import org.example.subscibers.RedisSubscriber


fun main() {
    val lokiLogger = LokiLogger()

    val redisSubscriber: RedisSubscriber = RedisConsumerSubscriber()

    redisSubscriber.startSubscription()

//    Thread.sleep(5000)
}