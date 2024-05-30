package org.bakalover.iot

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val taskId: Int,
    val houseId: Int,
    val responseCode: Int,
    val report: String
)