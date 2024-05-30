package ru.itmo.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskRequest(
    val taskId: Long,
    val houseId: Long,
    val thing: String,
    val action: String
)