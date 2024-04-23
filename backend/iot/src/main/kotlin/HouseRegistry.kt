package org.bakalover.iot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.bakalover.iot.things.House

class HouseRegistry {
    private var registry: Map<Int, House> = mutableMapOf()

    fun deployNewHouse(id: Int, scope: CoroutineScope) {
        val house = House()
        registry.plus(Pair(id, house))
        scope.launch {
            house.run()
        }
    }

    fun getHouseById(id: Int): House? {
        return registry[id]
    }

    fun getSendCommunication(): List<Channel<String>> {
        val communication = mutableListOf<Channel<String>>()
        for (entry in registry) {
            communication.add(entry.value.getSendChannel())
        }
        return communication
    }

    fun getReceiveCommunication(): List<Channel<String>> {
        val communication = mutableListOf<Channel<String>>()
        for (entry in registry) {
            communication.add(entry.value.getReceiveChannel())
        }
        return communication
    }
}