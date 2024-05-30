package org.bakalover.iot.pipeline

class Pipeline(private val stages: List<IPipelineStage>) : IPipeline {
    override fun launch() {
        stages.forEach { it.doWork() }
    }

    override suspend fun abort() {
        stages.forEach { it.abort() }
    }
}