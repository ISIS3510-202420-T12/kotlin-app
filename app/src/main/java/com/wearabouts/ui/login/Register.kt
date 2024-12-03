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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale

import com.wearabouts.R
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image

@Composable
fun Register(navController: NavController, viewModel: RegisterViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val maxUsernameLength = 32
    val maxPasswordLength = 20
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val registerState by viewModel.registerState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.bground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = username,
                onValueChange = { newValue -> 
                    if (newValue.length <= maxUsernameLength) {
                        username = newValue
                        showError = false
                        viewModel.resetState()
                    }
                },                
                label = { Text("Email (max. 32 characters)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = username.length >= maxUsernameLength
            )
            TextField(
                value = password,
                onValueChange = { newValue ->
                    if (newValue.length <= maxPasswordLength) {
                        password = newValue
                        showError = false
                        viewModel.resetState()
                    }
                },                
                label = { Text("Password (min. 6 characters, max. 20)") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),                
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = password.length >= maxPasswordLength,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }


            )
            Button(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
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
                            viewModel.register(username, password, context)
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
                    "Register",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = Poppins,
                        textAlign = TextAlign.Center
                    )
                )
            }

            when (registerState) {
                is RegisterViewModel.RegisterState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        color = Primary
                    )
                }
                is RegisterViewModel.RegisterState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                }
                is RegisterViewModel.RegisterState.Error -> {
                    LaunchedEffect(registerState) {
                        if (registerState is RegisterViewModel.RegisterState.Error && !(registerState as RegisterViewModel.RegisterState.Error).shown) {
                            showErrorToast(context, (registerState as RegisterViewModel.RegisterState.Error).message)
                            viewModel.markErrorAsShown()
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

private fun showErrorToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
