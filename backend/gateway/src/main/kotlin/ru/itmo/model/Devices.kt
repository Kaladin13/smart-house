package ru.itmo.model

import kotlinx.serialization.Serializable

@Serializable
data class Devices(
    val devicesIds: List<Device>
)