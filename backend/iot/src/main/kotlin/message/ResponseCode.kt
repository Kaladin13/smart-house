package org.bakalover.iot.message

enum class ResponseCode(val code: ULong) {
    OK(0u),
    THING_DOES_NOT_EXISTS(101u),
    INVALID_ACTION_FORMAT(102u),
    INVALID_TEMPERATURE(103u),
    INVALID_HUMIDITY(104u),
    OUT_OF_CHARGE(105u),
    INTERNAL_ERROR(106u),
}