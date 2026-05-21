package com.example.colenotas.user.inter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun AvisosScreen() {
    var avisos by remember { mutableStateOf<List<AvisoRespuesta>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            val respuesta = RetrofitClient.api.obtenerAvisos()
            if (respuesta.isSuccessful) {
                avisos = respuesta.body() ?: emptyList()
            }
        } catch (e: Exception) { }
        cargando = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
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
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Avisos de la Administración",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (avisos.isEmpty()) {
                    Text("No hay avisos nuevos.", fontSize = 14.sp, color = Color.Gray)
                } else {
                    avisos.forEach { aviso ->
                        AvisoCard(
                            titulo = aviso.titulo,
                            descripcion = aviso.mensaje,
                            urgente = aviso.urgente,
                            fecha = aviso.fecha,
                            enviadoPor = aviso.enviado_por,
                            onEnterado = {
                                scope.launch {
                                    try {
                                        val respuesta = RetrofitClient.api.marcarComoLeido(aviso.id)
                                        if (respuesta.isSuccessful) {
                                            avisos = avisos.map {
                                                if (it.id == aviso.id) it.copy(urgente = false)
                                                else it
                                            }
                                        }
                                    } catch (e: Exception) { }
                                }
                            },
                            onBorrar = {
                                scope.launch {
                                    try {
                                        RetrofitClient.api.eliminarAviso(aviso.id)
                                        avisos = avisos.filter { it.id != aviso.id }
                                    } catch (e: Exception) { }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AvisoCard(
    titulo: String,
    descripcion: String,
    urgente: Boolean = false,
    fecha: String = "",
    enviadoPor: String = "",
    onEnterado: () -> Unit = {},
    onBorrar: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (urgente) Color(0xFFFFEBEE) else Color(0xFFEDEDED)
        ),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        if (urgente) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFFD32F2F)
                            ) {
                                Text(
                                    "URGENTE",
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(descripcion, fontSize = 13.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Enviado por: $enviadoPor — $fecha",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onEnterado,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Enterado")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onBorrar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Borrar", color = Color.White)
                }
            }
        }
    }
}