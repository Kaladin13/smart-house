package com.example.mobiles.navigation.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobiles.model.HouseModel
import com.example.mobiles.model.UserModel
import com.example.mobiles.model.UserViewModel
import com.example.mobiles.ui.theme.Purple40
import com.example.mobiles.ui.theme.Purple80
import com.example.mobiles.util.TaskWebSocketListener
import com.example.mobiles.util.WebSocketSingleton
import com.example.mobiles.util.deleteHouse
import com.example.mobiles.util.sendTaskRequest
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeScreen(
    homeId: Long,
    userViewModel: UserViewModel = viewModel(),
    onNavigateToHomeList: () -> Unit
) {
    val houses by userViewModel.houses.collectAsState()
    val user by userViewModel.user.collectAsState()
    val home = houses?.houses?.find { it.id === homeId }

    Surface(color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                if (home != null) {
                    user?.let { Dashboard(home, it, onNavigateToHomeList) }
                }
            }
        }
    }
}

@Composable
fun Dashboard(home: HouseModel, user: UserModel, onNavigateToHomeList: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = home.name,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
            )
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete house",
                modifier = Modifier
                    .size(24.dp)
                    .padding(3.dp)
                    .clickable {
                        deleteHouse(user.token, home.id)
                        onNavigateToHomeList()
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (2 in home.devices) {
            LightCard(home.id)
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (1 in home.devices) {
            VacuumCleanerCard(home.id)
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (3 in home.devices) {
            TemperatureCard(home.id)
            Spacer(modifier = Modifier.height(8.dp))
            HumidityCard(home.id)
            Spacer(modifier = Modifier.weight(1f))
        }


    }
}

@Composable
fun LightCard(homeId: Long) {
    var checked by remember { mutableStateOf(false) }
    val webSocket = WebSocketSingleton.getWebSocket(TaskWebSocketListener(onMessageReceived = { message ->
        Log.d("WebSocket", "Message from server: $message")
    },
        onError = { error ->
            Log.e("WebSocket", "Error: ${error}")
        }))
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Purple40,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Light",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    val action = if (it) "on" else "off"
                    sendTaskRequest(
                        webSocket = webSocket,
                        taskId = 1L,
                        houseId = homeId,
                        thing = "light",
                        action = action
                    )
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Purple80,
                    checkedTrackColor = Purple80,
                    uncheckedTrackColor = Color.White
                )
            )
        }
    }
}

@Composable
fun VacuumCleanerCard(homeId: Long) {
    val webSocket = WebSocketSingleton.getWebSocket(TaskWebSocketListener(onMessageReceived = { message ->
        Log.d("WebSocket", "Message from server: $message")
    },
        onError = { error ->
            Log.e("WebSocket", "Error: ${error}")
        }))
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Purple40,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement =
            Arrangement.Center
        ) {
            Text(
                text = "Vacuum cleaner",
                fontSize = 32.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = {sendTaskRequest(
                    webSocket = webSocket,
                    taskId = 1L,
                    houseId = homeId,
                    thing = "cleaner",
                    action = "clean"
                )

//                getHouses("eef3fa36-aafa-4be9-8817-abc61b3e3f70", houses)
//                        onNavigateToHomeCreation()
                },
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Purple40
                )
            ) {
                Text("Run")
            }
        }
    }
}

@Composable
fun TemperatureCard(homeId: Long) {
    val number = remember { mutableIntStateOf(18) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TemperatureToggle(homeId)
            }

        }
    }
}

@Composable
fun HumidityCard(homeId: Long) {
    val percentage = remember { mutableIntStateOf(0) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            HumidityToggle(homeId)
        }
    }
}


@Composable
fun TemperatureToggle(homeId: Long) {
    var temperature by remember { mutableStateOf(22f) }
    val minTemp = 18f
    val maxTemp = 30f
    val webSocket = WebSocketSingleton.getWebSocket(TaskWebSocketListener(onMessageReceived = { message ->
        Log.d("WebSocket", "Message from server: $message")
    },
        onError = { error ->
            Log.e("WebSocket", "Error: ${error}")
        }))

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier
            .size(300.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val x = change.position.x - size.width / 2
                    val y = change.position.y - size.height / 2
                    val angle = atan2(y, x) * (180 / Math.PI).toFloat() + 90f
                    val normalizedAngle = if (angle < 0) angle + 360 else angle
                    val newTemperature = minTemp + (normalizedAngle / 360f) * (maxTemp - minTemp)
                    temperature = newTemperature.coerceIn(minTemp, maxTemp)
                    sendTaskRequest(
                        webSocket = webSocket,
                        taskId = 2L,
                        houseId = homeId,
                        thing = "climate",
                        action = "set t ${temperature.toInt()}"
                    )
                }
            }
        ) {
            drawCircularIndicator(
                temperature = temperature,
                minTemperature = minTemp,
                maxTemperature = maxTemp
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Temperature",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${temperature.toInt()}°",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Purple40
            )
        }
    }
}


@Composable
fun HumidityToggle(homeId: Long) {
    var humidity by remember { mutableStateOf(30f) }
    val minTemp = 0f
    val maxTemp = 100f
    val webSocket = WebSocketSingleton.getWebSocket(TaskWebSocketListener(onMessageReceived = { message ->
        Log.d("WebSocket", "Message from server: $message")
    },
        onError = { error ->
            Log.e("WebSocket", "Error: ${error}")
        }))

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier
            .size(300.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val x = change.position.x - size.width / 2
                    val y = change.position.y - size.height / 2
                    val angle = atan2(y, x) * (180 / Math.PI).toFloat() + 90f
                    val normalizedAngle = if (angle < 0) angle + 360 else angle
                    val newTemperature = minTemp + (normalizedAngle / 360f) * (maxTemp - minTemp)
                    humidity = newTemperature.coerceIn(minTemp, maxTemp)
                    sendTaskRequest(
                        webSocket = webSocket,
                        taskId = 2L,
                        houseId = homeId,
                        thing = "climate",
                        action = "set t ${humidity.toInt()}"
                    )
                }
            }
        ) {
            drawCircularIndicator(
                temperature = humidity,
                minTemperature = minTemp,
                maxTemperature = maxTemp
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Humidity",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${humidity.toInt()}°",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Purple40
            )
        }
    }
}

fun DrawScope.drawCircularIndicator(
    temperature: Float,
    minTemperature: Float,
    maxTemperature: Float
) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = size.minDimension / 2.5f
    val angleOffset = -90f

    drawArc(
        color = Color.LightGray,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
    )

    val sweepAngle = (temperature - minTemperature) / (maxTemperature - minTemperature) * 360f
    drawArc(
        color = Purple40,
        startAngle = angleOffset,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
    )

    val indicatorAngle = angleOffset + sweepAngle
    val indicatorX = center.x + radius * cos(Math.toRadians(indicatorAngle.toDouble())).toFloat()
    val indicatorY = center.y + radius * sin(Math.toRadians(indicatorAngle.toDouble())).toFloat()
    drawCircle(
        color = Purple40,
        radius = 15.dp.toPx(),
        center = Offset(indicatorX, indicatorY)
    )
}