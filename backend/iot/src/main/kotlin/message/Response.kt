package org.bakalover.iot.message

import kotlinx.serialization.Serializable

@Serializable
data class Response(val houseId: Int, val report: String)