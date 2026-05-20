package com.example.colenotas.user.inter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    var usuario by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contra,
            onValueChange = { contra = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (error.isNotEmpty()) {
            Text(text = error, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        error = ""
                        try {
                            val credenciales = mapOf(
                                "correo" to usuario,
                                "clave" to contra
                            )
                            val respuesta = RetrofitClient.api.login(credenciales)

                            if (respuesta.isSuccessful) {
                                val body = respuesta.body()!!
                                val user = body.usuario

                                `SesionUsuario`.id = user.id
                                `SesionUsuario`.nombre = user.nombre ?: "Usuario"
                                `SesionUsuario`.rol = user.rol

                                val nombreEncoded = `SesionUsuario`.nombre
                                    .replace(" ", "%20")

                                when (user.rol) {
                                    "admin" -> navController.navigate(
                                        "homeAdmin/$nombreEncoded/${user.id}"
                                    ) {
                                        popUpTo("login") { inclusive = true }
                                    }
                                    "docente" -> navController.navigate(
                                        "homeDocente/$nombreEncoded/${user.id}"
                                    ) {
                                        popUpTo("login") { inclusive = true }
                                    }
                                    else -> error = "Rol no reconocido"
                                }
                            } else {
                                error = "Credenciales incorrectas"
                            }
                        } catch (e: Exception) {
                            error = "No se pudo conectar: ${e.message}"
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }
        }
    }
}