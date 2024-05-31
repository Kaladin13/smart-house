package com.example.mobiles.model

data class ProfileModel(
    var userId: Int,
    var id: Int,
    var title: String,
    var completed: Boolean
)

data class UserModel(
    var token: String,
)

data class LoginRequest(
    val login: String,
    val password: String
)

data class ID(
    val id: Int
)

data class CreateHouseRequest(
    val userToken: String,
    val houseName: String,
    val devicesIds: List<Int>
)

data class HouseCreationModel(
    val houseId: Int
)


data class HouseModel(
    val id: Int,
    val name: String,
    val devices: Array<Int>,
)

data class GetHousesResponse(
    val houses: List<HouseModel>
)


data class DeviceModel(
    val id: Int,
    val name: String,
)

data class DeviceActionModel(
    val action: String,
    val description: String
)

data class DeviceActionsResponce(
    val actions: List<DeviceActionModel>
)