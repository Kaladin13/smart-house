package org.bakalover.iot.things

import kotlinx.coroutines.channels.Channel

class House(
    private val receiveChannel: Channel<String>,
    private val sendChannel: Channel<String>
) {

    suspend fun run() {
        // House Lifecycle
        // getting updates from send receiveChannel
        // and post updated to sendChannel
    }

    fun getSendChannel(): Channel<String> {
        return sendChannel
    }

    fun getReceiveChannel(): Channel<String> {
        return receiveChannel
    }
}