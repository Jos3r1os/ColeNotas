package com.example.colenotas.user.inter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun MandarAvisoScreen(navController: NavController) {
    var titulo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var urgente by remember { mutableStateOf(false) }
    var enviando by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var exitoMsg by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
            Text("Mandar Aviso", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        HorizontalDivider()

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text("Mandar un mensaje", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    "¿Algo importante que decir?",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text("Título", fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    placeholder = { Text("Título del aviso", color = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Mensaje", fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = mensaje,
                    onValueChange = { mensaje = it },
                    placeholder = { Text("Escribe el mensaje...", color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 6
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = urgente,
                        onCheckedChange = { urgente = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color.Black)
                    )
                    Text("Marcar como urgente", fontSize = 14.sp)
                }

                if (errorMsg.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMsg, color = Color.Red, fontSize = 13.sp)
                }

                if (exitoMsg.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(exitoMsg, color = Color(0xFF1565C0), fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (titulo.isEmpty() || mensaje.isEmpty()) {
                            errorMsg = "El título y mensaje son obligatorios"
                            return@Button
                        }
                        scope.launch {
                            enviando = true
                            errorMsg = ""
                            exitoMsg = ""
                            try {
                                val fechaHoy = LocalDate.now().toString()
                                val request = AvisoRequest(
                                    titulo = titulo,
                                    mensaje = mensaje,
                                    urgente = urgente,
                                    fecha = fechaHoy,
                                    admin_id = SesionUsuario.id
                                )
                                val respuesta = RetrofitClient.api.crearAviso(request)
                                if (respuesta.isSuccessful) {
                                    exitoMsg = "Aviso enviado correctamente"
                                    titulo = ""
                                    mensaje = ""
                                    urgente = false
                                } else {
                                    errorMsg = "Error al enviar el aviso"
                                }
                            } catch (e: Exception) {
                                errorMsg = "No se pudo conectar: ${e.message}"
                            }
                            enviando = false
                        }
                    },
                    enabled = !enviando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    if (enviando) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text("Enviar mensaje", color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}