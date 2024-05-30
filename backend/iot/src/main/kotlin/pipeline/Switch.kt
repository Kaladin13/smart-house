package org.bakalover.iot.pipeline

import kotlinx.coroutines.channels.Channel

class Switch<V>(size: Int) : ISwitch<V> {
    private val channels = List(size) { _ -> Channel<V>(capacity = Channel.UNLIMITED) }

    // Unsafe!!!
    override fun getChannel(id: Int): Channel<V> {
        return channels[id]
    }

    override fun getAll(): List<Channel<V>> {
        return channels
    }
}
