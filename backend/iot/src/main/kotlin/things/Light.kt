package org.bakalover.iot.things

import kotlinx.coroutines.*
import org.bakalover.iot.message.ResponseCode
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min

const val lowerLumenBound = 1400
const val upperLumenBound = 1800

class Light() : IThing {

    private var lumens = AtomicInteger(lowerLumenBound)
    private var isOn = AtomicBoolean(false)
    private lateinit var job: Job

    override fun doAction(action: String): Pair<ResponseCode, String> {
        val parts = action.split(" ")

        if (parts.size != 1) {
            return Pair(ResponseCode.INVALID_ACTION_FORMAT, "")
        }


        when (parts[0]) {
            "on" -> {
                isOn.set(true)
                return Pair(ResponseCode.OK, "${lumens.get()}")
            }

            "off" -> {
                isOn.set(false)
                return Pair(ResponseCode.OK, "")
            }

            "get" -> {
                return Pair(ResponseCode.OK, "${lumens.get()}")
            }

            else -> {
                return Pair(ResponseCode.INVALID_ACTION_FORMAT, "")
            }
        }
    }

    override fun backgroundActivity(scope: CoroutineScope) {
        job = scope.launch {
            while (true) {
                val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                if (currentHour > 19 || isOn.get()) {
                    for (i in 0..20) {
                        lumens.set(max(lumens.get() - 20 * i, lowerLumenBound))
                        delay(1000 * 60)
                    }
                }
                if ((currentHour in 8..13) || isOn.get()) {
                    for (i in 0..20) {
                        lumens.set(min(lumens.get() + 20 * i, upperLumenBound))
                        delay(1000 * 60)
                    }
                }
                delay(1000 * 60 * 10) // 10 minutes
            }
        }
    }

    override suspend fun abort() {
        job.cancelAndJoin()
    }
}