package org.bakalover.iot

import kotlin.math.pow
import kotlin.random.Random

class Backoff(
    private val initialDelay: Long = 1,
    private val maxDelay: Long = 2000,
    private val randomizationFactor: Double = 0.5,
) {

    private var currentDelay: Long = initialDelay
    private var currentRetry: Int = 0

    fun reset() {
        currentDelay = initialDelay
        currentRetry = 0
    }

    fun nextDelay(): Long {

        currentRetry++
        val delay = (currentDelay * 2.0.pow(currentRetry - 1)).toLong()
        currentDelay = minOf(delay, maxDelay)

        val randomFactor = Random.nextDouble() * randomizationFactor
        currentDelay += (currentDelay * randomFactor).toLong()

        return currentDelay
    }
}