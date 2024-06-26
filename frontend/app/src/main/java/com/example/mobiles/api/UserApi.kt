package com.example.mobiles.api

import com.example.mobiles.model.CreateHouseRequest
import com.example.mobiles.model.DeviceActionsResponce
import com.example.mobiles.model.DeviceModel
import com.example.mobiles.model.GetHousesResponse
import com.example.mobiles.model.HouseCreationModel
import com.example.mobiles.model.LoginRequest
import com.example.mobiles.model.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(@Body loginRequest: LoginRequest): Call<Void>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<UserModel?>

    @Headers("Content-Type: application/json")
    @POST("house")
    fun createHouse(@Body createHouseRequest: CreateHouseRequest): Call<HouseCreationModel?>

    @Headers("Content-Type: application/json")
    @DELETE("house")
    fun deleteHouse(@Query("userToken")userToken: String, @Query("houseId") houseId:Long ): Call<Void>

//   TODO: check what is first
    @Headers("Content-Type: application/json")
    @PUT("house")
    fun editHouse(@Query("userToken")userToken: String, @Query("houseId") houseId:Int ): Call<Void>

    @Headers("Content-Type: application/json")
    @GET("house")
    fun getHouses(@Query("userToken") userToken: String): Call<GetHousesResponse?>

    @Headers("Content-Type: application/json")
    @GET("devices")
    fun getDevices(): Call<List<DeviceModel>>

    @Headers("Content-Type: application/json")
    @GET("devices/action/{device-id}")
    fun getDeviceActions(@Path("device-id")id: Int): Call<DeviceActionsResponce?>

}

