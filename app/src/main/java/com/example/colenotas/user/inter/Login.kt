package com.example.colenotas.user.inter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(navController: NavController) {
    var usuario by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(value = usuario, onValueChange = { usuario = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = contra, onValueChange = { contra = it }, label = { Text("Contraseña") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        if (error.isNotEmpty()) Text(text = error, color = Color.Red)

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    isLoading = true
                    val credenciales = mapOf("correo" to usuario, "clave" to contra)

                    RetrofitClient.instance.login(credenciales).enqueue(object : Callback<UsuarioRespuesta> {
                        override fun onResponse(call: Call<UsuarioRespuesta>, response: Response<UsuarioRespuesta>) {
                            isLoading = false
                            if (response.isSuccessful) {
                                val user = response.body()
                                if (user?.rol == "admin") {
                                    navController.navigate("homeAdmin")
                                } else {
                                    navController.navigate("homeDocente")
                                }
                            } else {
                                error = "Credenciales incorrectas"
                            }
                        }

                        override fun onFailure(call: Call<UsuarioRespuesta>, t: Throwable) {
                            isLoading = false
                            error = "Error: " + t.message
                        }
                    })
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }
        }
    }
}