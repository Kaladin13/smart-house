package ru.itmo.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateHouseResponse(
    val houseId: Long
)
