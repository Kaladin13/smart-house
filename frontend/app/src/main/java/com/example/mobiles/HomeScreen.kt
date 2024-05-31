package com.example.mobiles

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobiles.model.GetHousesResponse
import com.example.mobiles.model.HouseModel
import com.example.mobiles.util.getHouses

@Composable
fun HomeScreen(onLogout: () -> Unit, onNavigateToHomeCreation: () -> Unit) {

    var checked by remember { mutableStateOf(true) }
    val number = remember { mutableIntStateOf(18) }
    val percentage = remember { mutableStateOf(0) }
    val houseList: List<HouseModel> = emptyList()

    val houses = remember { mutableStateOf(GetHousesResponse(houseList)) }

    LaunchedEffect(Unit) {
        // Call the getHouses function with user token
        getHouses("eef3fa36-aafa-4be9-8817-abc61b3e3f70", houses)
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TwoBlocksLayout()
            Spacer(modifier = Modifier.height(16.dp))
            DropDownMenu()
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                }
            )
            Text(
                text = "Light",
                fontSize = 24.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        getHouses("eef3fa36-aafa-4be9-8817-abc61b3e3f70", houses)
                        Log.d("main", houses.toString())
//                        onNavigateToHomeCreation()
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Run vacuum cleaner")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { if (number.value > 18) number.value-- }
                ) {
                    Text(text = "-")
                }
                Spacer(modifier = Modifier.width(16.dp))
                BasicTextField(
                    value = number.value.toString(),
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.let {
                            if (it in 18..40) {
                                number.value = it
                            }
                        }
                    },
                    readOnly = true,
                    modifier = Modifier.width(60.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { if (number.value < 40) number.value++ }
                ) {
                    Text(text = "+")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "temperature",
                    fontSize = 24.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { if (percentage.value > 0) percentage.value -= 10 }
                ) {
                    Text(text = "-")
                }
                Spacer(modifier = Modifier.width(16.dp))
                BasicTextField(
                    value = percentage.value.toString(),
                    onValueChange = { newValue ->
                        val cleanValue = newValue.filter { it.isDigit() }
                        cleanValue.toIntOrNull()?.let {
                            if (it in 0..100) {
                                percentage.value = it
                            }
                        }
                    },
                    modifier = Modifier.width(80.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { if (percentage.value < 100) percentage.value += 10 }
                ) {
                    Text(text = "+")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "humidity",
                    fontSize = 24.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            ExtendedFloatingActionButton(
                onClick = { onNavigateToHomeCreation() },
                icon = { Icon(Icons.Filled.Add, "Add new house button") },
                text = { Text(text = "Add new house") },
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(onLogout = {}, onNavigateToHomeCreation = {})
}
@Composable
fun DropDownMenu() {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp) // Adjust spacing as needed
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp) // Adjust size as needed
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "ArrowDropDown",
                    modifier = Modifier.size(24.dp) // Adjust size as needed
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Load") },
                onClick = { Toast.makeText(context, "Load", Toast.LENGTH_SHORT).show() }
            )
            DropdownMenuItem(
                text = { Text("Save") },
                onClick = { Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show() }
            )
        }
    }
}


@Composable
fun TwoBlocksLayout() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxHeight() // Ensures the column fills the entire height of the parent
    ) {
        // First row of blocks
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ensures the first row takes the required space
        ) {
            // First block
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Gray)
                    .padding(8.dp)
            ) {
                Text(
                    text = "First block",
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            // Second block (slightly longer)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp) // Adjust height as needed
                    .background(Color.DarkGray)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Second block (longer)",
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Second row of blocks
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ensures the second row takes the remaining space
                .padding(start = 4.dp) // Aligns the second row with the end of the first row
        ) {
            // Third block (fills remaining height)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.LightGray)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Third block",
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            // Fourth block (fills remaining height)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Gray)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Fourth block",
                    color = Color.White
                )
            }
        }
    }
}

