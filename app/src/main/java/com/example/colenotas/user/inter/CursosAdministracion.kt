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
fun CursosAdminScreen(navController: NavController) {
    var docentes by remember { mutableStateOf<List<DocenteConCursos>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val respuesta = RetrofitClient.api.obtenerDocentesConCursos()
            if (respuesta.isSuccessful) {
                docentes = respuesta.body() ?: emptyList()
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
            Text(text = "Cursos por docente", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(text = "Docentes", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = "Administracion 2026",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (docentes.isEmpty()) {
                    Text("No hay docentes registrados.", fontSize = 14.sp, color = Color.Gray)
                } else {
                    docentes.forEach { docente ->
                        DocenteItemAdmin(docente = docente, navController = navController)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DocenteItemAdmin(docente: DocenteConCursos, navController: NavController) {
    val cursos = docente.cursos ?: emptyList()
    var expandido by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expandido = !expandido }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("☆", fontSize = 18.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(docente.nombre_completo, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    Text("Ciclo 2026", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${cursos.size}", fontSize = 14.sp, color = Color.Gray)
                Icon(
                    imageVector = if (expandido) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = "",
                    tint = Color.Gray
                )
            }
        }

        if (expandido) {
            cursos.forEach { curso ->
                AdminCursoItemDatos(
                    curso = curso,
                    onClick = {
                        val nombreEncoded = curso.nombre_curso.replace(" ", "%20")
                        navController.navigate("verNotasAdmin/${curso.id}/$nombreEncoded")
                    }
                )
            }
        }

        HorizontalDivider(color = Color(0xFFEEEEEE))
    }
}

@Composable
fun AdminCursoItemDatos(curso: CursoRespuesta, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 32.dp, top = 6.dp, bottom = 6.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("☆", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(curso.nombre_curso, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(curso.grado, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "",
            tint = Color.Gray
        )
    }
}