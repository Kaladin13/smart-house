package org.bakalover.iot.things

import kotlinx.coroutines.channels.Channel

class House(
    private val receiveChannel: Channel<String>,
    private val sendChannel: Channel<String>,
) {

    suspend fun run() {
        while (true) {
            processRequest()
            sendUpdate()
        }
    }

    private suspend fun processRequest() {
        receiveChannel.receive()
    }

    private suspend fun sendUpdate() {
        sendChannel.send("a")
    }
}