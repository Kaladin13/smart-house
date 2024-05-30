package ru.itmo.model

import kotlinx.serialization.Serializable

@Serializable
data class DeviceActions(
    val actions: List<Action>
)
