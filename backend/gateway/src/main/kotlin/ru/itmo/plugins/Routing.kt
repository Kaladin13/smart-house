package ru.itmo.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject
import ru.itmo.model.TokenDto
import ru.itmo.model.UserDto
import ru.itmo.service.UserService


fun Application.configureRouting() {
    val userService: UserService by inject(UserService::class.java)
    routing {

        route("/register") {
            post {
                val userDto = call.receive<UserDto>()
                userService.registerUser(userDto)
                call.respond(HttpStatusCode.OK)
            }
        }

        route("/login") {
            post {
                val userDto = call.receive<UserDto>()
                val token = userService.loginUser(userDto)
                call.respond(TokenDto(token))
            }
        }
    }
}
