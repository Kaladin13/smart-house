package org.bakalover.iot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
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

    fun getHouseSendChannelById(id: Int): Channel<String> {
        return registry[id]!!.getReceiveChannel()
    }

    fun getReceiveCommunication(): List<Channel<String>> {
        val communication = mutableListOf<Channel<String>>()
        for (entry in registry) {
            communication.add(entry.value.getSendChannel())
        }
        return communication
    }
}