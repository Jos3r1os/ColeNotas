package com.example.colenotas.user.inter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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

@Composable
fun VerEventosAdminScreen(navController: NavController) {
    var eventos by remember { mutableStateOf<List<EventoRespuesta>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            val respuesta = RetrofitClient.api.obtenerEventos()
            if (respuesta.isSuccessful) {
                eventos = respuesta.body() ?: emptyList()
            }
        } catch (e: Exception) { }
        cargando = false
    }

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
            Text("Eventos del Ciclo", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        HorizontalDivider()

        if (cargando) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Ciclo 2026", fontSize = 14.sp, color = Color.Gray)

                if (eventos.isEmpty()) {
                    Text("No hay eventos registrados.", fontSize = 14.sp, color = Color.Gray)
                } else {
                    eventos.forEach { evento ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            evento.nombre,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            evento.fecha.substring(0, 10),
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                        if (!evento.comentario.isNullOrEmpty()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                evento.comentario,
                                                fontSize = 13.sp,
                                                color = Color.DarkGray
                                            )
                                        }
                                        Text(
                                            "Por: ${evento.creado_por}",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = Color(0xFFD32F2F),
                                        modifier = Modifier
                                            .clickable {
                                                scope.launch {
                                                    try {
                                                        RetrofitClient.api.eliminarEvento(evento.id)
                                                        eventos = eventos.filter { it.id != evento.id }
                                                    } catch (e: Exception) { }
                                                }
                                            }
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}