package com.wearabouts.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ButtonDefaults
import com.wearabouts.ui.theme.Primary
import com.wearabouts.ui.theme.Poppins
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.graphics.painterResource

@Composable
fun Register(navController: NavController, viewModel: RegisterViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val registerState by viewModel.registerState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.images),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
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
                onClick = { viewModel.register(username, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
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

            when (registerState) {
                is RegisterViewModel.RegisterState.Loading -> Text("Registering...")
                is RegisterViewModel.RegisterState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                }
                is RegisterViewModel.RegisterState.Error -> Text((registerState as RegisterViewModel.RegisterState.Error).message)
                else -> {}
            }
        }
    }
}