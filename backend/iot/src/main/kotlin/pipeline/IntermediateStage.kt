package org.bakalover.iot.pipeline

import kotlinx.coroutines.CoroutineScope
import org.bakalover.iot.process.IIntermediateProcess

class IntermediateStage<V, Q>(
    private val procs: List<IIntermediateProcess<V, Q>>,
    private val fromSwitch: ISwitch<V>,
    private val toSwitch: ISwitch<Q>,
    private val scope: CoroutineScope
) : IPipelineStage {
    override fun doWork() {
        procs.forEach { it.doProcess(fromSwitch, toSwitch, scope) }
    }

    override suspend fun abort() {
        procs.forEach { it.abort() }
    }
}