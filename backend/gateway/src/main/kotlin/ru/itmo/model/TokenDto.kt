package ru.itmo.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val token: String
)

