package org.bakalover.iot.pipeline

interface IPipeline {
    fun launch()
    suspend fun abort()
}