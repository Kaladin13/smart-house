package org.bakalover.iot

import kotlinx.serialization.Serializable

@Serializable
data class Request(val houseId: Int, val action: String, val details: String)