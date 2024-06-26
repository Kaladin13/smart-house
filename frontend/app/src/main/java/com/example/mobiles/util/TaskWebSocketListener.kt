package com.example.mobiles.util
import android.util.Log
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class TaskWebSocketListener(private val onMessageReceived: (String) -> Unit,
                            private val onError: (String) -> Unit) : WebSocketListener(

)  {override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
    super.onOpen(webSocket, response)
    Log.d("WebSocket", "Connection opened")
}

override fun onMessage(webSocket: WebSocket, text: String) {
    super.onMessage(webSocket, text)
    Log.d("WebSocket", "Message received: $text")
    onMessageReceived(text)
}

override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
    super.onMessage(webSocket, bytes)
    Log.d("WebSocket", "Message received (bytes): ${bytes.hex()}")
    onMessageReceived(bytes.hex())
}

override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
    super.onClosing(webSocket, code, reason)
    Log.d("WebSocket", "Connection closing: $code / $reason")
    webSocket.close(1000, null)
}

override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
    super.onFailure(webSocket, t, response)
    Log.e("WebSocket", "Error: ${t.message}")
    onError(t.message ?: "Unknown error")
}
}


