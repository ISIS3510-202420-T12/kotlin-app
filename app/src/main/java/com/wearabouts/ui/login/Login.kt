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
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun Login(navController: NavController, viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
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
                onValueChange = { 
                    username = it 
                    showError = false},
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = password,
                onValueChange = {
                    password = it
                    showError = false 
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    when {
                        username.isEmpty() && password.isEmpty() -> {
                            showError = true
                            errorMessage = "Please enter username and password"
                        }
                        username.isEmpty() -> {
                            showError = true
                            errorMessage = "Please enter username"
                        }
                        password.isEmpty() -> {
                            showError = true
                            errorMessage = "Please enter password"
                        }
                        else -> {
                            showError = false
                            viewModel.login(username, password)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
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
                is LoginViewModel.LoginState.Error -> showErrorToast(context, (loginState as LoginViewModel.LoginState.Error).message)
                else -> {}
            }
        }
    }
}

private fun showErrorToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

private fun formatErrorMessage(errorMessage: String?): String {
    return when (errorMessage) {
        "INVALID_CREDENTIALS" -> "Incorrect username or password, try again"
        "INVALID_EMAIL" -> "Invalid email address, please check and try again"
        "USER_DISABLED" -> "Your account has been disabled, contact support for assistance"
        "USER_NOT_FOUND" -> "Account not found, please register or try again"
        "NETWORK_REQUEST_FAILED" -> "Network error, please check your connection and try again"
        else -> "An unexpected error occurred, please try again later" // Default fallback message
    } ?: "Unknown error"
}