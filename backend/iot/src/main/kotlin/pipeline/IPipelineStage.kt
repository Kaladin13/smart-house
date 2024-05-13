package org.bakalover.iot.pipeline

interface IPipelineStage {
    fun doWork();
    suspend fun abort();
}