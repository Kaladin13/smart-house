package ru.itmo.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse(
    val taskId: Long,
    val houseId: Long,
    val responseCode: String,
    val report: String? = null
)
