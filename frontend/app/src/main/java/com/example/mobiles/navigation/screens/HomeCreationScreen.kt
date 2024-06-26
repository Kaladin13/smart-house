package com.example.mobiles.navigation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobiles.model.HouseCreationModel
import com.example.mobiles.model.UserViewModel
import com.example.mobiles.ui.theme.Purple40
import com.example.mobiles.ui.theme.Purple80
import com.example.mobiles.util.createHouse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCreationScreen(
    userViewModel: UserViewModel = viewModel(),
    onNavigateToHomeList: () -> Unit
) {
    var hasLight by remember { mutableStateOf(false) }
    var hasVacuumCleaner by remember { mutableStateOf(false) }
    var hasClimateControl by remember { mutableStateOf(false) }
    var houseName by remember { mutableStateOf(TextFieldValue("")) }
    val devices = remember { mutableStateListOf<Int>() }
    val houseId = remember { mutableStateOf(HouseCreationModel(0)) }
    val user by userViewModel.user.collectAsState()

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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Add New House",
                fontSize = 24.sp,
                color = Purple40
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = houseName,
                onValueChange = { houseName = it },
                label = { Text("House Name") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Purple80,
                    cursorColor = Color.Black,
                    disabledLabelColor = Purple80,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                trailingIcon = {
                    if (houseName.text.isNotEmpty()) {
                        IconButton(onClick = { houseName = TextFieldValue("") }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )


            DeviceToggle("Light", hasLight) { checked ->
                hasLight = checked
                updateDeviceList(1, checked)
            }

            DeviceToggle("Vacuum Cleaner", hasVacuumCleaner) { checked ->
                hasVacuumCleaner = checked
                updateDeviceList(2, checked)
            }

            DeviceToggle("Climate Control", hasClimateControl) { checked ->
                hasClimateControl = checked
                updateDeviceList(3, checked)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        Log.d("back to home", "back to home")
                        onNavigateToHomeList()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Back")
                }

                Button(
                    onClick = {
                        Log.d(
                            "CreateHouse",
                            "Creating house with devices: ${devices.joinToString()}"
                        )
                        user?.let { createHouse(it.token, houseName.text, devices) }
                        onNavigateToHomeList()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Add")
                }

                Spacer(modifier = Modifier.width(16.dp))


            }
        }
    }
}

@Composable
fun DeviceToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = label, fontSize = 16.sp, color = Purple40)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Purple40,
                uncheckedThumbColor = Purple40,
                checkedTrackColor = Purple80,
                uncheckedTrackColor = Purple80
            )
        )
    }
}
