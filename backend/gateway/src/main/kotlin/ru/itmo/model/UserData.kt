package ru.itmo.model

data class UserData(
    val id: Long,
    val login: String,
    val hashedPassword: String,
)