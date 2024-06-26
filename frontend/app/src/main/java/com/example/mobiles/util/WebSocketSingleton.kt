package com.example.mobiles.util

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

object WebSocketSingleton {
    private var webSocket: WebSocket? = null

    fun getWebSocket(listener: TaskWebSocketListener): WebSocket {
        if (webSocket == null) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("ws://10.0.2.2:5454/task")
                .build()
            webSocket = client.newWebSocket(request, listener)
            client.dispatcher.executorService.shutdown()
        }
        return webSocket!!
    }
}
