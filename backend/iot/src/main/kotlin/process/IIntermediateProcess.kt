package org.bakalover.iot.process

import kotlinx.coroutines.CoroutineScope
import org.bakalover.iot.pipeline.ISwitch

interface IIntermediateProcess<V, Q> : IAbortableProcess {
    fun doProcess(commutationFrom: ISwitch<V>, commutationTo: ISwitch<Q>, scope: CoroutineScope)
}