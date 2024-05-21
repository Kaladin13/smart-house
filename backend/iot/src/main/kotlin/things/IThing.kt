package org.bakalover.iot.things

import kotlinx.coroutines.CoroutineScope
import org.bakalover.iot.message.ResponseCode

interface IThing {
    fun doAction(action: String): Pair<ResponseCode, String>
    fun backgroundActivity(scope: CoroutineScope)
    suspend fun abort()
}