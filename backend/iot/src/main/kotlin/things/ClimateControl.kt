package org.bakalover.iot.things

import kotlinx.coroutines.*
import org.bakalover.iot.message.ResponseCode
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

const val lowerBoundTemperature = 18
const val upperBoundTemperature = 40
const val lowerBoundHumidity = 0
const val upperBoundHumidity = 100

class ClimateControl() : IThing {
    private var t = AtomicInteger(20)
    private var h = AtomicInteger(50) // Percentage
    private lateinit var job: Job;

    override fun doAction(action: String): Pair<ResponseCode, String> {
        val parts = action.split(" ")

        if (parts.size != 3) {
            return Pair(ResponseCode.INVALID_ACTION_FORMAT, "")
        }

        when (parts[0]) {
            "set" -> {
                when (parts[1]) {
                    "t" -> {
                        try {
                            val to = parts[2].toInt()
                            if (to > upperBoundTemperature || to < lowerBoundTemperature) {
                                return Pair(ResponseCode.INVALID_TEMPERATURE, "")
                            }
                            t.set(to)
                            return Pair(ResponseCode.OK, "")
                        } catch (e: NumberFormatException) {
                            return Pair(ResponseCode.INVALID_ACTION_FORMAT, "")
                        }
                    }

                    "h" -> {
                        try {
                            val to = parts[2].toInt()
                            if (to > upperBoundHumidity || to < lowerBoundHumidity) {
                                return Pair(ResponseCode.INVALID_HUMIDITY, "")
                            }
                            h.set(to)
                            return Pair(ResponseCode.OK, "")
                        } catch (e: NumberFormatException) {
                            return Pair(ResponseCode.INVALID_ACTION_FORMAT, "")
                        }
                    }

                    else -> {
                        return Pair(ResponseCode.INVALID_ACTION_FORMAT, "")
                    }
                }
            }

            "get" -> {
                return Pair(ResponseCode.OK, "$t $h")
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
                if (currentHour > 20) {
                    t.set(19)
                }
                delay(1000 * 60 * 10) // 10 minutes
            }
        }
    }

    override suspend fun abort() {
        job.cancelAndJoin()
    }
}


