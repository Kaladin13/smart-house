package org.bakalover.iot.message

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val taskId: Int,
    val houseId: Int,
    val responseCode: ResponseCode,
    val report: String
)