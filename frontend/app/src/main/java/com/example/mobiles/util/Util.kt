package com.example.mobiles.util

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.mobiles.api.UserApi
import com.example.mobiles.model.CreateHouseRequest
import com.example.mobiles.model.GetHousesResponse
import com.example.mobiles.model.HouseCreationModel
import com.example.mobiles.model.LoginRequest
import com.example.mobiles.model.TaskRequest
import com.example.mobiles.model.UserModel
import com.example.mobiles.model.UserViewModel
import com.google.gson.Gson
import okhttp3.WebSocket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
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
    userViewModel: UserViewModel,
    onError: (String) -> Unit,
    onSuccess: () -> Unit
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
                    userViewModel.setUser(profile)
                    Log.d("LoginLogic", "Profile received: $profile")
                    onSuccess()
                } else {
                    Log.e("Main", "Profile is null")
                    onError("Login failed. Please check your credentials and try again.")
                }
            } else {
                Log.e("Main", "Response not successful: ${response.code()}")
                onError("Login failed. Please check your credentials and try again.")
            }
        }

        override fun onFailure(call: Call<UserModel?>, t: Throwable) {
            Log.e("Main", "Request failed: ${t.message}")
            onError("Network error. Please check your connection and try again.")
        }
    })
}


fun register(
    login: String,
    password: String,
    onError: (String) -> Unit,
    onSuccess: () -> Unit
) {
    val retrofit = createRetrofit()
    val api = retrofit.create(UserApi::class.java)
    val loginRequest = LoginRequest(login = login, password = password)
    val call: Call<Void> = api.register(loginRequest)

    call.enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                onSuccess()
                Log.d("Main", "Raw response body: ${response.raw()}")
            } else {
                onError("Register failed.")
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
                Log.d("Main createHouse", "Raw response body: ${response.raw()}")
                if (houseIdResponse != null) {
                    Log.d("Main createHouse", "Success! $houseIdResponse")
                } else {
                    Log.e("Main createHouse", "Profile is null")
                }
            } else {
                Log.e("Main createHouse", "Response not successful: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<HouseCreationModel?>, t: Throwable) {
            Log.e("Main", "Request failed: ${t.message}")
        }
    })
}

fun getHouses(
    userToken: String,
    userViewModel: UserViewModel,
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
                    userViewModel.setHouses(housesResponse)
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


fun deleteHouse(
    userToken: String,
    houseId: Long
) {
    val retrofit = createRetrofit()
    val api = retrofit.create(UserApi::class.java)
    val call: Call<Void> = api.deleteHouse(userToken, houseId)

    call.enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                val housesResponse = response.body()
                Log.d("Main", "Raw response body: ${response.raw()}")
                    Log.d("Main", "Success! $housesResponse")
                    Log.e("Main", "Profile is null")
                }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            TODO("Not yet implemented")
        }

    })
}

//fun connectAndSendTaskRequest(houseId: Long, thing: String, action: String ) {
//    val webSocketUrl = "ws://10.0.2.2:5454/task"
//    val listener = WebSocketListener(
//        onMessageReceived = { message ->
//            Log.d("WebSocket", "Message from server: $message")
//        },
//        onError = { error ->
//            Log.e("WebSocket", "Error: $error")
//        }
//    )
//    val webSocket = createWebSocket(webSocketUrl, listener)
//    sendTaskRequest(webSocket, taskId = 123, houseId, thing, action)
//}

fun sendTaskRequest(
    webSocket: WebSocket,
    taskId: Long,
    houseId: Long,
    thing: String,
    action: String
) {
    val taskRequest = TaskRequest(taskId, houseId, thing, action)
    val gson = Gson()
    val json = gson.toJson(taskRequest)
    Log.d("sendTaskRequest", "inside")
    webSocket.send(json)
}
