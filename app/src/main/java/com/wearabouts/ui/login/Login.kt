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

@Composable
fun Login(navController: NavController, viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    // Cambio en la línea 9: Añadir Box para centrar el contenido
    Box(
        modifier = Modifier
            .fillMaxSize(), // Hacer que el Box ocupe toda la pantalla
        contentAlignment = Alignment.Center // Centrar el contenido dentro del Box
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), // Llenar todo el ancho disponible dentro del Box
            horizontalAlignment = Alignment.CenterHorizontally // Centrar los elementos horizontalmente
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
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.login(username, password) },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Login")
            }

            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Go to Register")
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
