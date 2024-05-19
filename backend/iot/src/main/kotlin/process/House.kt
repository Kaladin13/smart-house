package org.bakalover.iot.process

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import org.bakalover.iot.message.Request
import org.bakalover.iot.message.Response
import org.bakalover.iot.pipeline.ISwitch

class House(private val id: Int) : IIntermediateProcess<Request, Response> {
    private lateinit var receiveChannel: ReceiveChannel<Request>
    private lateinit var sendChannel: SendChannel<Response>
    private lateinit var job: Job

    override fun doProcess(commutationFrom: ISwitch<Request>, commutationTo: ISwitch<Response>, scope: CoroutineScope) {
        receiveChannel = commutationFrom.getChannel(id)
        sendChannel = commutationTo.getChannel(id)
        job = scope.launch {
            run()
        }
    }

    private suspend fun run() {
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

    override suspend fun abort() {
        job.cancelAndJoin()
    }
}