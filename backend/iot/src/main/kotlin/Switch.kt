package org.bakalover.iot

import kotlinx.coroutines.channels.Channel

class Switch<T>(private val size: Int) {
    private val channels = List(size) { _ -> Channel<T>(capacity = Channel.UNLIMITED) }

    fun getChannelById(id: Int): Channel<T> {
        return channels[id]
    }

    fun getAllChannels(): List<Channel<T>> {
        return channels
    }
}