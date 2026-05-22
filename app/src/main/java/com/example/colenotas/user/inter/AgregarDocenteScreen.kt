package com.example.colenotas.user.inter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable

@Composable
fun AgregarDocenteScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var cursos by remember { mutableStateOf(listOf(Pair("", ""))) }
    var guardando by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var exitoMsg by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFDDDDDD), RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Text("🏫", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agregar Docente", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        HorizontalDivider()

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text("Datos del docente", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Nombre completo", fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = { Text("Ej: Ingeniero Torres", color = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Correo", fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    placeholder = { Text("correo@colenotas.com", color = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Contraseña", fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = clave,
                    onValueChange = { clave = it },
                    placeholder = { Text("Contraseña", color = Color.LightGray) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Cursos asignados", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = {
                        cursos = cursos + Pair("", "")
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar curso", tint = Color(0xFF2C2C2C))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                cursos.forEachIndexed { index, (nombreCurso, grado) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = nombreCurso,
                            onValueChange = { nuevo ->
                                cursos = cursos.toMutableList().also { it[index] = Pair(nuevo, grado) }
                            },
                            placeholder = { Text("Nombre del curso", color = Color.LightGray) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = grado,
                            onValueChange = { nuevo ->
                                cursos = cursos.toMutableList().also { it[index] = Pair(nombreCurso, nuevo) }
                            },
                            placeholder = { Text("Grado", color = Color.LightGray) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        if (cursos.size > 1) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.clickable {
                                    cursos = cursos.toMutableList().also { it.removeAt(index) }
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (errorMsg.isNotEmpty()) {
                Text(errorMsg, color = Color.Red, fontSize = 13.sp)
            }
            if (exitoMsg.isNotEmpty()) {
                Text(exitoMsg, color = Color(0xFF1565C0), fontSize = 13.sp)
            }

            Button(
                onClick = {
                    if (nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
                        errorMsg = "Nombre, correo y contraseña son obligatorios"
                        return@Button
                    }
                    scope.launch {
                        guardando = true
                        errorMsg = ""
                        exitoMsg = ""
                        try {
                            val respuesta = RetrofitClient.api.crearDocente(
                                mapOf(
                                    "nombre_completo" to nombre,
                                    "correo" to correo,
                                    "clave" to clave
                                )
                            )
                            if (respuesta.isSuccessful) {
                                val docente = respuesta.body()!!
                                cursos.filter { it.first.isNotEmpty() }.forEach { (nombreCurso, grado) ->
                                    RetrofitClient.api.asignarCurso(
                                        mapOf(
                                            "nombre_curso" to nombreCurso,
                                            "grado" to grado,
                                            "docente_id" to docente.id.toString()
                                        )
                                    )
                                }
                                exitoMsg = "Docente creado correctamente"
                                nombre = ""
                                correo = ""
                                clave = ""
                                cursos = listOf(Pair("", ""))
                            } else {
                                errorMsg = "Error al crear el docente"
                            }
                        } catch (e: Exception) {
                            errorMsg = "No se pudo conectar: ${e.message}"
                        }
                        guardando = false
                    }
                },
                enabled = !guardando,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
            ) {
                if (guardando) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text("Guardar docente", color = Color.White, fontSize = 15.sp)
                }
            }
        }
    }
}