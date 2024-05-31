package ru.itmo.model

import kotlinx.serialization.Serializable

@Serializable
data class Houses(
    val houses: List<House>
)