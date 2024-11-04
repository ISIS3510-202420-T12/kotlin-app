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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.wearabouts.ui.theme.Poppins
import com.wearabouts.ui.theme.Primary
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.runtime.remember
import androidx.compose.material3.CircularProgressIndicator
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.compose.material.icons.filled.Fingerprint
import android.content.Context
import androidx.compose.ui.layout.ContentScale

import com.wearabouts.R
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun Login(navController: NavController, biometricPrompt: BiometricPrompt, promptInfo: BiometricPrompt.PromptInfo, viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isBiometricAvailable by remember { mutableStateOf(false) }

    val maxUsernameLength = 32
    val maxPasswordLength = 20
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        isBiometricAvailable = viewModel.canAuthenticateWithBiometrics(context)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.bground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
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
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
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
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),               
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
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

            if (isBiometricAvailable) {
                Button(
                    onClick = { 
                        viewModel.authenticateWithBiometrics(biometricPrompt, promptInfo)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Fingerprint",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Login with Fingerprint",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = Poppins,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }

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
                            viewModel.login(username, password)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
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
                is LoginViewModel.LoginState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        color = Primary
                    )
                }
                is LoginViewModel.LoginState.Success -> {
                    LaunchedEffect(loginState) {
                        if (loginState is LoginViewModel.LoginState.Success) {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }                }
                is LoginViewModel.LoginState.Error -> {
                    LaunchedEffect(loginState) {
                        if (loginState is LoginViewModel.LoginState.Error && !(loginState as LoginViewModel.LoginState.Error).shown) {
                            showErrorToast(context, (loginState as LoginViewModel.LoginState.Error).message)
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
