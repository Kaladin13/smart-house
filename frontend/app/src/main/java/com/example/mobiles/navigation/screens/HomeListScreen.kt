package com.example.mobiles.navigation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobiles.R
import com.example.mobiles.model.UserViewModel
import com.example.mobiles.navigation.NavRoutes
import com.example.mobiles.util.getHouses

@Composable
fun HomeListScreen(userViewModel: UserViewModel = viewModel(), navController: NavHostController) {
    val user by userViewModel.user.collectAsState()
    LaunchedEffect(Unit) {
        user?.let { getHouses(it.token, userViewModel) }
    }
    val houses by userViewModel.houses.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(),
    ) {
        Text(
            text = "My houses",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(houses?.houses ?: emptyList()) { home ->
                Row(
                    modifier = Modifier
                        .padding(3.dp)
                        .fillMaxSize()
                        .clickable {
                            navController.navigate("${NavRoutes.HomeScreen.route}/${home.id}")
                        }
                        .drawBehind {
                            val strokeWidth = 2 * density
                            val y = size.height - strokeWidth / 2

                            drawLine(
                                Color.LightGray,
                                Offset(0f, y),
                                Offset(size.width, y),
                                strokeWidth
                            )
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.house),
                        contentDescription = "house picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(3.dp)
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = home.name,
                        modifier = Modifier.clickable {
                            navController.navigate("${NavRoutes.HomeScreen.route}/${home.id}")
                        }
                    )

                }
            }
        }
    }
}