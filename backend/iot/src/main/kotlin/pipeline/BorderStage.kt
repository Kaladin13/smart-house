package org.bakalover.iot.pipeline

import kotlinx.coroutines.CoroutineScope
import org.bakalover.iot.process.IBorderProcess

class BorderStage<V>(
    private val procs: List<IBorderProcess<V>>,
    private val switch: ISwitch<V>,
    private val scope: CoroutineScope
) : IPipelineStage {
    override fun doWork() {
        procs.forEach { it.doProcess(switch, scope) }
    }

    override suspend fun abort() {
        procs.forEach { it.abort() }
    }
}