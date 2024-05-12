package org.bakalover.iot

import kotlinx.coroutines.channels.Channel

interface ISwitch<V> {
    fun getChannel(id: Int): Channel<V>
    fun getAll(): List<Channel<V>>
}