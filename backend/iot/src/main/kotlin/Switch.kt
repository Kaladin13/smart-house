package org.bakalover.iot

import kotlinx.coroutines.channels.Channel

class Switch<V>(private val size: Int) : ISwitch<V> {
    private val channels = List(size) { _ -> Channel<V>(capacity = Channel.UNLIMITED) }

    override fun getChannel(id: Int): Channel<V> {
        return channels[id]
    }

    override fun getAll(): List<Channel<V>> {
        return channels
    }
}