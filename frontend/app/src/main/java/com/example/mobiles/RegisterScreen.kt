package com.example.mobiles

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobiles.ui.theme.Purple40
import com.example.mobiles.ui.theme.Purple80
import com.example.mobiles.util.login
import com.example.mobiles.util.register


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegister: (String, String) -> Unit,  onNavigateToHome: () -> Unit, onNavigateToLogin: () -> Unit) {
    var login by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    val focusManager = LocalFocusManager.current


    Surface( color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Login",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = Purple40
            )

            val maxLength = 20
            TextField(
                value = login,
                onValueChange = {
                    if (it.text.length <= maxLength) login = it
                },
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
                    if (login.text.isNotEmpty()) {
                        IconButton(onClick = { login = TextFieldValue("") }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "${login.text.length} / $maxLength",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                textAlign = TextAlign.End,
                color = Purple40
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Password",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = Purple40
            )

            TextField(
                value = password,
                onValueChange = {
                    if (it.text.length <= maxLength) password = it
                },
                visualTransformation = PasswordVisualTransformation(),
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
                    if (password.text.isNotEmpty()) {
                        IconButton(onClick = { password = TextFieldValue("") }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "${password.text.length} / $maxLength",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                textAlign = TextAlign.End,
                color = Purple40
            )

            Button(
                onClick = {
                    register(login.text, password.text)
                    onNavigateToHome()
                },
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors( containerColor = Purple40,)
            ) {
                Text("Register")
            }
            Button(
                onClick = {
                    onNavigateToLogin()
                },
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor= Color.Transparent, contentColor = Purple40,)
            ) {
                Text("Return to login")
            }
        }

    }
}
