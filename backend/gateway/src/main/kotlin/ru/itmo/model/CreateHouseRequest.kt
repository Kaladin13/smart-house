package ru.itmo.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateHouseRequest(
    val userToken: String,
    val houseName: String,
    val devicesIds: List<Long>
)
