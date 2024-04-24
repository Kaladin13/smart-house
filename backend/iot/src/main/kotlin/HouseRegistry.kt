package org.bakalover.iot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import org.bakalover.iot.things.House

class HouseRegistry(
    private val receiveRequestsFrom: Switch<String>,
    private val sendUpdatesTo: Switch<String>
) {
    private var registry: Map<Int, House> = mutableMapOf()
    private var jobs: List<Job> = mutableListOf()

    fun deployNewHouse(id: Int, scope: CoroutineScope) {
        val house = House(receiveRequestsFrom.getChannelById(id), sendUpdatesTo.getChannelById(id))
        registry.plus(Pair(id, house))
        jobs.plus(scope.launch {
            house.run()
        })
    }

    suspend fun cancelAndJoin() {
        jobs.forEach { it.cancelAndJoin() }
    }

    fun checkHouseById(id: Int): Boolean {
        return registry.contains(id)
    }
}