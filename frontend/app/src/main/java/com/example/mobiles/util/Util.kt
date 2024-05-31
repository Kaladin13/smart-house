package com.example.mobiles.util

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.mobiles.api.UserApi
import com.example.mobiles.model.CreateHouseRequest
import com.example.mobiles.model.GetHousesResponse
import com.example.mobiles.model.HouseCreationModel
import com.example.mobiles.model.ID
import com.example.mobiles.model.LoginRequest
import com.example.mobiles.model.UserModel
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

private fun createRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5454")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun login(
    login: String,
    password: String,
    userState: MutableState<UserModel>
) {
    val retrofit = createRetrofit()
    val api = retrofit.create(UserApi::class.java)
    val loginRequest = LoginRequest(login = login, password = password)
    val call: Call<UserModel?> = api.login(loginRequest)

    call.enqueue(object : Callback<UserModel?> {
        override fun onResponse(call: Call<UserModel?>, response: Response<UserModel?>) {
            if (response.isSuccessful) {
                val profile = response.body()
                Log.d("Main", "Raw response body: ${response.raw()}")
                if (profile != null) {
                    Log.d("Main", "Success! $profile")
                    userState.value = profile
                } else {
                    Log.e("Main", "Profile is null")
                }
            } else {
                Log.e("Main", "Response not successful: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<UserModel?>, t: Throwable) {
            Log.e("Main", "Request failed: ${t.message}")
        }
    })
}


fun register(
    login: String,
    password: String,
) {
    val retrofit = createRetrofit()
    val api = retrofit.create(UserApi::class.java)
    val loginRequest = LoginRequest(login = login, password = password)
    val call: Call<Void> = api.register(loginRequest)

    call.enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                Log.d("Main", "Raw response body: ${response.raw()}")
            } else {
                Log.e("Main", "Response not successful: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Log.e("Main", "Request failed: ${t.message}")
        }
    })
}

fun createHouse(
    userToken: String,
    houseName: String,
    devicesIds: List<Int>,
    houseId: MutableState<HouseCreationModel>
) {
    val retrofit = createRetrofit()
    val api = retrofit.create(UserApi::class.java)
    val createHouseRequest = CreateHouseRequest(userToken = userToken, houseName = houseName,
        devicesIds = devicesIds)
    val call: Call<HouseCreationModel?> = api.createHouse(createHouseRequest)

    call.enqueue(object : Callback<HouseCreationModel?> {
        override fun onResponse(
            call: Call<HouseCreationModel?>,
            response: Response<HouseCreationModel?>
        ) {
            if (response.isSuccessful) {
                val houseIdResponse = response.body()
                Log.d("Main", "Raw response body: ${response.raw()}")
                if (houseIdResponse != null) {
                    Log.d("Main", "Success! $houseIdResponse")
                    houseId.value = houseIdResponse
                } else {
                    Log.e("Main", "Profile is null")
                }
            } else {
                Log.e("Main", "Response not successful: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<HouseCreationModel?>, t: Throwable) {
            Log.e("Main", "Request failed: ${t.message}")
        }
    })
}

fun getHouses(
    userToken: String,
    houses: MutableState<GetHousesResponse>
) {
    Log.d("main", userToken)
    val retrofit = createRetrofit()
    val api = retrofit.create(UserApi::class.java)
    val call: Call<GetHousesResponse?> = api.getHouses(userToken)

    call.enqueue(object : Callback<GetHousesResponse?> {
        override fun onResponse(
            call: Call<GetHousesResponse?>,
            response: Response<GetHousesResponse?>
        ) {
            if (response.isSuccessful) {
                val housesResponse = response.body()
                Log.d("Main hi", "Raw response body: ${response.raw()}")
                if (housesResponse != null) {
                    Log.d("Main", "Success! $housesResponse")
                    houses.value = housesResponse
                } else {
                    Log.e("Main", "Profile is null")
                }
            } else {
                Log.e("Main", "Response not successful: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<GetHousesResponse?>, t: Throwable) {
            Log.e("Main", "Request failed: ${t.message}")
        }
    })
}

fun getDevices(
    userToken: String,
    houses: MutableState<GetHousesResponse>
) {
    val retrofit = createRetrofit()
    val api = retrofit.create(UserApi::class.java)
    val call: Call<GetHousesResponse?> = api.getHouses(userToken)

    call.enqueue(object : Callback<GetHousesResponse?> {
        override fun onResponse(
            call: Call<GetHousesResponse?>,
            response: Response<GetHousesResponse?>
        ) {
            if (response.isSuccessful) {
                val housesResponse = response.body()
                Log.d("Main", "Raw response body: ${response.raw()}")
                if (housesResponse != null) {
                    Log.d("Main", "Success! $housesResponse")
                    houses.value = housesResponse
                } else {
                    Log.e("Main", "Profile is null")
                }
            } else {
                Log.e("Main", "Response not successful: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<GetHousesResponse?>, t: Throwable) {
            Log.e("Main", "Request failed: ${t.message}")
        }
    })
}
