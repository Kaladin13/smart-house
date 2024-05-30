package ru.itmo.model

import kotlinx.serialization.Serializable

@Serializable
data class House(
    val id: Long,
    val name: String,
    val devices: List<Long>
)
