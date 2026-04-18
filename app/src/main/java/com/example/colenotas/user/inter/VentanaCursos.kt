package com.example.colenotas.user.inter

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VentanaCursosScreen() {
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
                text = "Ingeniero Figueroa",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            VentanaCursoItem("Calculo I", "Segundo Semestre", "65")
            VentanaCursoItem("Calculo II", "Tercer Semestre", "40")
            VentanaCursoItem("Calculo III", "Cuarto Semestre", "3")
            VentanaCursoItem("Programacion II", "Cuarto Semestre", "30")
            VentanaCursoItem("Programacion III", "Quinto Semestre", "10")
            VentanaCursoItem("Redes de Computadoras", "Sexto Semestre", "14")
        }
    }
}

@Composable
fun VentanaCursoItem(titulo: String, semestre: String, numero: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Star, contentDescription = "", tint = Color.DarkGray)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, fontSize = 14.sp)
            Text(text = semestre, fontSize = 12.sp, color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "",
                modifier = Modifier.size(16.dp),
                tint = Color.Gray
            )
            Text(text = numero, fontSize = 12.sp)
        }
    }
    HorizontalDivider(color = Color(0xFFEEEEEE))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EstaEsLaVentanaCursos() {
    MaterialTheme { VentanaCursosScreen() }
}