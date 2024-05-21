package org.bakalover.iot.process

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import org.bakalover.iot.message.Request
import org.bakalover.iot.message.Response
import org.bakalover.iot.message.ResponseCode
import org.bakalover.iot.pipeline.ISwitch
import org.bakalover.iot.things.ClimateControl
import org.bakalover.iot.things.IThing
import org.bakalover.iot.things.Light

class House(private val id: Int) : IIntermediateProcess<Request, Response> {
    private lateinit var receiveChannel: ReceiveChannel<Request>
    private lateinit var sendChannel: SendChannel<Response>
    private lateinit var job: Job
    private lateinit var things: Map<String, IThing>;

    override fun doProcess(commutationFrom: ISwitch<Request>, commutationTo: ISwitch<Response>, scope: CoroutineScope) {
        receiveChannel = commutationFrom.getChannel(id)
        sendChannel = commutationTo.getChannel(id)

        val climate = ClimateControl()
        val light = Light()

        climate.backgroundActivity(scope)
        light.backgroundActivity(scope)

        things = mapOf(
            Pair("climate", climate),
            Pair("light", light)
        )


        job = scope.launch {
            run()
        }
    }

    private suspend fun run() {
        while (true) {
            processRequest()
        }
    }

    private suspend fun processRequest() {
        val request = receiveChannel.receive()
        val thing = request.thing
        if (things.contains(thing)) {
            val thingResponse = things[thing]?.doAction(request.action)
            if (thingResponse != null) {
                sendChannel.send(
                    Response(
                        request.taskId,
                        request.houseId,
                        thingResponse.first,
                        thingResponse.second
                    )
                )
            } else {
                sendChannel.send(
                    Response(
                        request.taskId,
                        request.houseId,
                        ResponseCode.INTERNAL_ERROR,
                        ""
                    )
                )

            }
        } else {
            sendChannel.send(Response(request.taskId, request.houseId, ResponseCode.THING_DOES_NOT_EXISTS, ""))
        }
    }

    override suspend fun abort() {
        things.forEach { (_, thing) -> thing.abort() }
        job.cancelAndJoin()
    }
}