package ru.itmo.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory
import ru.itmo.model.TaskRequest
import ru.itmo.service.RedisService
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

fun Application.configureSockets() {
    val redisService: RedisService by inject(RedisService::class.java)
    val json: Json by inject(Json::class.java)
    val sessions = ConcurrentHashMap<Long, WebSocketSession>()
    val logger = LoggerFactory.getLogger("Sockets")

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocket("/task") {
            logger.info("get request $incoming")
            try {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val task = json.decodeFromString<TaskRequest>(receivedText)
                    sessions[task.taskId] = this

                    launch {
                        redisService.publishTask(task)

                        redisService.subscribeToResponse(task.taskId) { response ->
                            val session = sessions[response.taskId]
                            session ?: return@subscribeToResponse
                            try {
                                runBlocking { session.send(Frame.Text(json.encodeToString(response))) }
                            } catch (e: ClosedReceiveChannelException) {
                                sessions.remove(response.taskId)
                            }
                        }
                    }
                }
            } catch (e: ClosedReceiveChannelException) {
                println("ClosedReceiveChannelException: ${e.message}")
            } catch (e: Throwable) {
                println("Throwable: ${e.message}")
            } finally {
                val sessionIds = sessions.filterValues { it == this }.keys
                for (sessionId in sessionIds) {
                    sessions.remove(sessionId)
                }
            }
        }
    }
}


