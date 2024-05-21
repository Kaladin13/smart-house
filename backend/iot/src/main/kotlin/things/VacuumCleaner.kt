package org.bakalover.iot.things

import kotlinx.coroutines.*
import org.bakalover.iot.message.ResponseCode
import java.util.concurrent.atomic.AtomicInteger

class VacuumCleaner : IThing {
    private lateinit var job: Job
    private var chargePercentage = AtomicInteger(100)
    private var cleaningDonePercentage = 0

    override fun doAction(action: String): Pair<ResponseCode, String> {
        if (action != "clean") {
            return Pair(ResponseCode.INVALID_ACTION_FORMAT, "Vacuum cleaner")
        } else {
            if (chargePercentage.get() == 100) {
                cleaningDonePercentage += 50
                chargePercentage.set(0)
                if (cleaningDonePercentage == 100) {
                    cleaningDonePercentage = 0
                    return Pair(ResponseCode.OK, "")
                } else {
                    return Pair(ResponseCode.OUT_OF_CHARGE, "")
                }
            } else {
                return Pair(ResponseCode.OUT_OF_CHARGE, "")
            }
        }
    }

    override fun backgroundActivity(scope: CoroutineScope) {
        job = scope.launch {
            while (true) {
                if (chargePercentage.get() <= 10) {
                    while (chargePercentage.get() < 100) {
                        chargePercentage.addAndGet(1)
                        delay(200)
                    }
                } else {
                    delay(1000 * 5)
                }
            }
        }
    }

    override suspend fun abort() {
        job.cancelAndJoin()
    }
}