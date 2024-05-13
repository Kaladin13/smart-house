package org.bakalover.iot.process

interface IAbortableProcess {
    suspend fun abort()
}