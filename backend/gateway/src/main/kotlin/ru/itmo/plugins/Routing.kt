package ru.itmo.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject
import ru.itmo.model.CreateHouseRequest
import ru.itmo.model.CreateHouseResponse
import ru.itmo.model.DeviceActions
import ru.itmo.model.Devices
import ru.itmo.model.Houses
import ru.itmo.model.TokenDto
import ru.itmo.model.UserDto
import ru.itmo.service.HouseService
import ru.itmo.service.UserService


fun Application.configureRouting() {
    val userService: UserService by inject(UserService::class.java)
    val houseService: HouseService by inject(HouseService::class.java)

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

        route("/house") {
            post {
                val request = call.receive<CreateHouseRequest>()
                val houseId = houseService.createHouse(request)
                call.respond(CreateHouseResponse(houseId))
            }
            delete {
                val houseId = call.parameters["houseId"]?.toLong() ?: throw IllegalArgumentException("Missing houseId")
                val userToken = call.parameters["userToken"] ?: throw IllegalArgumentException("Missing userToken")
                houseService.deleteHouse(houseId, userToken)
                call.respondText("House Deleted")
            }
            put {
                val houseId = call.parameters["houseId"]?.toLong() ?: throw IllegalArgumentException("Missing houseId")
                val userToken = call.parameters["userToken"] ?: throw IllegalArgumentException("Missing userToken")
                val devices = call.receive<Devices>()
                houseService.addDevicesInHouse(houseId, devices, userToken)
                call.respondText("Devices Added")
            }
            get {
                val userToken = call.parameters["userToken"] ?: throw IllegalArgumentException("Missing userToken")
                val houses = houseService.getHousesByUserToken(userToken)
                call.respond(Houses(houses))
            }
        }

        route("/devices") {
            get {
                val devices = houseService.getAllowedDevices()
                call.respond(devices)
            }

            get("/action/{device-id}") {
                val deviceId =
                    call.parameters["device-id"]?.toLong() ?: throw IllegalArgumentException("Missing device-id")
                val actions = houseService.getDeviceActions(deviceId)
                call.respond(DeviceActions(actions))
            }
        }
    }

}
