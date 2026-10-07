package com.anjali.wedmoments

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLogin: suspend (email: String, password: String) -> Result<Unit>,
    onGoogleLogin: suspend () -> Unit,
    onCreateAccountClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome Back", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Login to WedMoments", color = Color.Gray, fontSize = 12.sp)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                if (email.isBlank() || pass.isBlank()) {
                    Toast.makeText(context, "Enter email and password", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                scope.launch {
                    val result = onLogin(email, pass)
                    result.exceptionOrNull()?.message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Login")
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = {
                scope.launch { onGoogleLogin() }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Continue with Google")
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = { onForgotPasswordClick() }) {
            Text("Forgot Password?")
        }

        TextButton(onClick = { onCreateAccountClick() }) {
            Text("Create Account")
        }

        Text(
            "Demo: anjali@gmail.com / password123",
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}
