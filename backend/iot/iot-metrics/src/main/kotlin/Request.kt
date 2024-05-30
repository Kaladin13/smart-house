package org.bakalover.iot

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val taskId: Int,
    val houseId: Int,
    val thing: String,
    val action: String
)