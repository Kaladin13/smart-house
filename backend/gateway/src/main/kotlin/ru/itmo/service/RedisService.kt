package ru.itmo.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.redisson.api.RQueue
import org.redisson.api.RedissonClient
import ru.itmo.model.TaskRequest
import ru.itmo.model.TaskResponse

class RedisService(
    private val client: RedissonClient,
    private val json: Json
) {

    fun publishTask(task: TaskRequest) {
        val publisher: RQueue<String> = client.getQueue("response_house")
        publisher.offer(json.encodeToString(task))
    }

    suspend fun subscribeToResponse(
        taskId: Long,
        onMessageReceived: (TaskResponse) -> Unit,
    ) {

        withContext(Dispatchers.IO) {
            val queue: RQueue<String> = client.getQueue("request_house")
            val startTime = System.currentTimeMillis()

            while (isActive && System.currentTimeMillis() - startTime < 3000) {
                val message = queue.poll()
                if (message != null) {
                    val taskResponse = json.decodeFromString<TaskResponse>(message)
                    if (taskResponse.taskId == taskId) {
                        onMessageReceived(taskResponse)
                        return@withContext
                    }
                }
                delay(100)
            }
        }
    }
}
