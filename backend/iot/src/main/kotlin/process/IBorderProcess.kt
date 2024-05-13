package org.bakalover.iot.process

import kotlinx.coroutines.CoroutineScope
import org.bakalover.iot.pipeline.ISwitch

interface IBorderProcess<V> : IAbortableProcess {
    fun doProcess(switch: ISwitch<V>, scope: CoroutineScope)
}