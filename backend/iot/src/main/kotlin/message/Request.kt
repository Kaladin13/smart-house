package org.bakalover.iot.message

import kotlinx.serialization.Serializable

@Serializable
data class Request(val houseId: Int, val action: String, val details: String);