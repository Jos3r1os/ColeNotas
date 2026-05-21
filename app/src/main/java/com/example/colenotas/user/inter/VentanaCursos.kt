package com.example.colenotas.user.inter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun VentanaCursosScreen(navController: NavController) {
    var cursos by remember { mutableStateOf<List<CursoRespuesta>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(SesionUsuario.id) {
        try {
            val respuesta = RetrofitClient.api.obtenerCursosPorDocente(SesionUsuario.id)
            if (respuesta.isSuccessful) {
                cursos = respuesta.body() ?: emptyList()
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFDDDDDD), RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Text("🏫", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Tus cursos", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        HorizontalDivider()

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(text = "Tus cursos", fontSize = 14.sp, color = Color.Gray)
            Text(
                text = SesionUsuario.nombre.ifEmpty { "Docente" },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                cargando -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                cursos.isEmpty() -> {
                    Text(
                        "No hay cursos asignados.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                else -> {
                    cursos.forEach { curso ->
                        VentanaCursoItem(
                            titulo = curso.nombre_curso,
                            semestre = curso.grado,
                            onClick = {
                                val nombreEncoded = curso.nombre_curso.replace(" ", "%20")
                                navController.navigate("asignarNotas/${curso.id}/$nombreEncoded")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VentanaCursoItem(titulo: String, semestre: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Star, contentDescription = "", tint = Color.DarkGray)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, fontSize = 14.sp)
            Text(text = semestre, fontSize = 12.sp, color = Color.Gray)
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "",
            tint = Color.Gray
        )
    }
    HorizontalDivider(color = Color(0xFFEEEEEE))
}