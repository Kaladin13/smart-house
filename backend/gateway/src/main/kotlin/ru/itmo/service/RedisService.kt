package ru.itmo.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub
import ru.itmo.model.TaskRequest
import ru.itmo.model.TaskResponse

class RedisService(
    private val jedis: Jedis,
    private val json: Json
) {

    fun publishTask(task: TaskRequest) {
        jedis.publish("tasks", json.encodeToString(task))
    }

    suspend fun subscribeToResponse(taskId: Long, onMessageReceived: (TaskResponse) -> Unit) {
        withContext(Dispatchers.IO) {
            jedis.subscribe(object : JedisPubSub() {
                override fun onMessage(channel: String, message: String) {
                    val taskResponse = json.decodeFromString<TaskResponse>(message)
                    if (taskResponse.taskId ==

                        taskId
                    ) {
                        onMessageReceived(taskResponse)
                        this.unsubscribe()
                    }
                }
            }, "responses")
        }
    }
}
