package com.example.mobiles.util

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


object WebSocketSingleton {
    private var webSocket: WebSocket? = null

    fun getWebSocket(listener: TaskWebSocketListener): WebSocket {
        Log.d("WebSocketSingleton", "inside")
        if (webSocket == null) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("ws://localhost:5454/task")
                .build()
            webSocket = client.newWebSocket(request, listener)
        }
        return webSocket!!
    }
}