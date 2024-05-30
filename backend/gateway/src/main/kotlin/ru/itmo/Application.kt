package ru.itmo

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.startKoin
import ru.itmo.di.createKoinModule
import ru.itmo.plugins.configureRouting
import ru.itmo.plugins.configureSerialization
import ru.itmo.plugins.configureSockets

fun main() {
    embeddedServer(Netty, port = 5454, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    startKoin {
        modules(createKoinModule(this@module))
    }

    configureSerialization()
    configureSockets()
    configureRouting()
}
