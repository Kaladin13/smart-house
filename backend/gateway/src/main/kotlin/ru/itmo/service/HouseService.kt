package ru.itmo.service

import org.jooq.DSLContext
import ru.itmo.jooq.generated.tables.Device.Companion.DEVICE
import ru.itmo.jooq.generated.tables.House.Companion.HOUSE
import ru.itmo.jooq.generated.tables.HouseDevices.Companion.HOUSE_DEVICES
import ru.itmo.jooq.generated.tables.references.DEVICE_ACTIONS
import ru.itmo.model.Action
import ru.itmo.model.CreateHouseRequest
import ru.itmo.model.Device
import ru.itmo.model.Devices
import ru.itmo.model.House
import java.util.*

class HouseService(private val dsl: DSLContext) {

    fun createHouse(request: CreateHouseRequest): Long {
        checkDevicesExist(request.devicesIds)

        val userId = UUID.fromString(request.userToken)

        val houseRecord = dsl.insertInto(HOUSE)
            .set(HOUSE.NAME, request.houseName)
            .set(HOUSE.USER_ID, userId)
            .returning(HOUSE.ID)
            .fetchOne()

        if (request.devicesIds.isNotEmpty()) {
            linkDevicesToHouse(houseRecord?.id!!, request.devicesIds)
        }

        return houseRecord?.id!!
    }

    fun deleteHouse(houseId: Long, userToken: String) {
        checkHouseOwner(UUID.fromString(userToken), houseId)
        dsl.deleteFrom(HOUSE).where(HOUSE.ID.eq(houseId)).execute()
    }

    fun addDevicesInHouse(houseId: Long, devices: Devices, userToken: String) {
        checkHouseOwner(UUID.fromString(userToken), houseId)
        checkDevicesExist(devices.devicesIds.map { it.id })
        linkDevicesToHouse(houseId, devices.devicesIds.map { it.id })
    }

    fun getDeviceActions(deviceId: Long): List<Action> {
        return dsl.selectFrom(DEVICE_ACTIONS)
            .where(DEVICE_ACTIONS.DEVICE_ID.eq(deviceId))
            .fetch()
            .map {
                Action(
                    it.name!!,
                    it.description!!,
                )
            }
    }

    fun getAllowedDevices(): List<Device> {
        return dsl.selectFrom(DEVICE)
            .fetch().map { record ->
                Device(
                    id = record.id!!,
                    name = record.name,
                )
            }
    }

    fun getHousesByUserToken(userToken: String): List<House> {
        val userId = UUID.fromString(userToken)
        return dsl.selectFrom(HOUSE)
            .where(HOUSE.USER_ID.eq(userId))
            .fetch().map { record ->
                House(
                    id = record?.id!!,
                    name = record.name!!,
                    devices = dsl.selectFrom(HOUSE_DEVICES)
                        .where(HOUSE_DEVICES.HOUSE_ID.eq(record.id))
                        .fetch()
                        .toList()
                        .map { it.deviceId!! }
                )
            }
    }


    private fun linkDevicesToHouse(houseId: Long, devicesIds: List<Long>) {
        devicesIds.forEach { deviceId ->
            dsl.insertInto(HOUSE_DEVICES)
                .set(HOUSE_DEVICES.HOUSE_ID, houseId)
                .set(HOUSE_DEVICES.DEVICE_ID, deviceId)
                .execute()
        }
    }

    private fun checkDevicesExist(devicesIds: List<Long>) {
        val count = dsl.selectCount()
            .from(DEVICE)
            .where(DEVICE.ID.`in`(devicesIds))
            .fetchOne(0, Int::class.java) ?: 0

        if (count != devicesIds.size) {
            throw IllegalArgumentException("Some device IDs do not exist in the database")
        }
    }

    private fun checkHouseOwner(userToken: UUID, houseId: Long) {
        val count = dsl.selectCount()
            .from(HOUSE)
            .where(
                HOUSE.ID.eq(houseId)
                    .and(HOUSE.USER_ID.eq(userToken))
            )
            .fetchOne(0, Int::class.java) ?: 0

        if (count < 1) {
            throw IllegalArgumentException("House does not belong to user")
        }
    }
}
