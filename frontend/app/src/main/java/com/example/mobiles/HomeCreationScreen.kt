package com.example.mobiles

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobiles.model.HouseCreationModel
import com.example.mobiles.model.HouseModel
import com.example.mobiles.model.ID
import com.example.mobiles.model.UserModel
import com.example.mobiles.util.createHouse



@Composable
fun HomeCreationScreen(onNavigateToHome: () -> Unit) {
    var hasLight by remember { mutableStateOf(false) }
    var hasVacuumCleaner by remember { mutableStateOf(false) }
    var hasClimateControl by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var houseName by remember { mutableStateOf(TextFieldValue()) }
    val devices = remember { mutableStateListOf<Int>() }
    val houseId = remember { mutableStateOf(HouseCreationModel(0)) }

    fun updateDeviceList(id: Int, add: Boolean) {
        if (add) {
            if (!devices.contains(id)) devices.add(id)
        } else {
            devices.remove(id)
        }
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    Log.d("back to home", "back to home")
                    onNavigateToHome()
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Back")
            }

            Text(
                text = "Add new house",
                fontSize = 24.sp,
            )

            TextField(
                value = houseName,
                onValueChange = { houseName = it },
                label = { Text("House name") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Light")
                Button(
                    onClick = {
                        hasLight = !hasLight
                        updateDeviceList(1, hasLight)
                    },
                ) {
                    Text(text = if (hasLight) "-" else "+", color = Color.White)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Vacuum cleaner")
                Button(
                    onClick = {
                        hasVacuumCleaner = !hasVacuumCleaner
                        updateDeviceList(2, hasVacuumCleaner)
                    },
                ) {
                    Text(text = if (hasVacuumCleaner) "-" else "+", color = Color.White)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Climate control")
                Button(
                    onClick = {
                        hasClimateControl = !hasClimateControl
                        updateDeviceList(3, hasClimateControl)
                    },
                ) {
                    Text(text = if (hasClimateControl) "-" else "+", color = Color.White)
                }
            }
            Button(
                onClick = {
                    Log.d("CreateHouse", "Creating house with devices: ${devices.toString()}")
                    createHouse("eef3fa36-aafa-4be9-8817-abc61b3e3f70", houseName.text, devices, houseId)
                    onNavigateToHome()
                },
            ) {
                Text(text = "Add")
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeCreation() {
    HomeCreationScreen(onNavigateToHome = {})
}
