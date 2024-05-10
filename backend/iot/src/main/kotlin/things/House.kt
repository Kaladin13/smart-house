package org.bakalover.iot.things

import kotlinx.coroutines.channels.Channel
import org.bakalover.iot.message.Request
import org.bakalover.iot.message.Response

class House(
    private val receiveChannel: Channel<Request>,
    private val sendChannel: Channel<Response>,
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
        sendChannel.send(Response(1, "hjjjjjjjjjjjjjjjjjjji"))
    }
}