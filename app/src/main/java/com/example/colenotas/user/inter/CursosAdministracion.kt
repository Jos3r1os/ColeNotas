package com.example.colenotas.user.inter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Curso(val nombre: String, val cantidad: String)
data class Docente(val nombre: String, val ciclo: String, val cantidad: String, val cursos: List<Curso>)

val docentesPrueba = listOf(
    Docente("Ingeniero Figueroa", "Ciclo 2026", "3", listOf(
        Curso("Calculo I", "A"),
        Curso("Calculo II", "A"),
        Curso("Calculo III", "A")
    )),
    Docente("Ingeniero Solis", "Ciclo 2026", "4", emptyList()),
    Docente("Ingeniero Guzman", "Ciclo 2026", "3", emptyList())
)

@Composable
fun CursosAdminScreen() {
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
            Text(text = "Tus cursos", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        HorizontalDivider()

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(text = "Tus cursos", fontSize = 12.sp, color = Color.Gray)
            Text(
                text = "Administracion 2026",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            docentesPrueba.forEach { docente ->
                DocenteItem(docente = docente)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DocenteItem(docente: Docente) {
    var expandido by remember { mutableStateOf(docente.cursos.isNotEmpty()) }

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
                    Text(docente.nombre, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    Text(docente.ciclo, fontSize = 12.sp, color = Color.Gray)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("☆", fontSize = 14.sp, color = Color.Gray)
                Text(docente.cantidad, fontSize = 14.sp, color = Color.Gray)
            }
        }

        if (expandido) {
            docente.cursos.forEach { curso -> AdminCursoItem(curso = curso) }
        }

        HorizontalDivider(color = Color(0xFFEEEEEE))
    }
}

@Composable
fun AdminCursoItem(curso: Curso) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("☆", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(curso.nombre, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text("Ver notas", fontSize = 12.sp, color = Color.Gray)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("☆", fontSize = 14.sp, color = Color.Gray)
            Text(curso.cantidad, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CursosAdminPreview() {
    MaterialTheme { CursosAdminScreen() }
}