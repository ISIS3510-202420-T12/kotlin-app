package com.wearabouts.ui.login

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.wearabouts.ui.theme.Poppins
import com.wearabouts.ui.theme.Primary

import com.wearabouts.R
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image

@Composable
fun Login(navController: NavController, viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.images),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.login(username, password) },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Login",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = Poppins,
                        textAlign = TextAlign.Center
                    )
                )
            }

            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Register",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = Poppins,
                        textAlign = TextAlign.Center
                    )
                )
            }

            when (loginState) {
                is LoginViewModel.LoginState.Loading -> Text("Loading...")
                is LoginViewModel.LoginState.Success -> {
                    navController.navigate("home")
                }
                is LoginViewModel.LoginState.Error -> Text((loginState as LoginViewModel.LoginState.Error).message)
                else -> {}
            }
        }
    }
}
