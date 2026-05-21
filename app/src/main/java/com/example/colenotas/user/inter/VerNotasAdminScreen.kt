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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerNotasAdminScreen(cursoId: Int = 0, nombreCurso: String = "") {
    var alumnos by remember { mutableStateOf<List<AlumnoConNotas>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(cursoId) {
        try {
            val respuesta = RetrofitClient.api.obtenerAlumnosPorCurso(cursoId)
            if (respuesta.isSuccessful) {
                alumnos = respuesta.body() ?: emptyList()
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
            Text(text = "Notas del curso", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        HorizontalDivider()

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    nombreCurso.replace("%20", " "),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (alumnos.isEmpty()) {
                Text("No hay alumnos inscritos.", fontSize = 14.sp, color = Color.Gray)
            } else {
                alumnos.forEach { alumno ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    alumno.nombre_completo,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                if (alumno.punteo != null) {
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = when {
                                            (alumno.punteo.toDoubleOrNull() ?: 0.0) >= 60 -> Color(0xFF1B5E20).copy(alpha = 0.1f)
                                            else -> Color(0xFFD32F2F).copy(alpha = 0.1f)
                                        }
                                    ) {
                                        Text(
                                            "Prom: ${alumno.punteo}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = when {
                                                (alumno.punteo.toDoubleOrNull() ?: 0.0) >= 60 -> Color(0xFF1B5E20)
                                                else -> Color(0xFFD32F2F)
                                            },
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                } else {
                                    Text("Sin notas", fontSize = 12.sp, color = Color.Gray)
                                }
                            }

                            if (alumno.punteo != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(color = Color(0xFFEEEEEE))
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    NotaChip("AS", alumno.as_nota)
                                    NotaChip("P1", alumno.p1_nota)
                                    NotaChip("P2", alumno.p2_nota)
                                    NotaChip("HW", alumno.hw_nota)
                                    NotaChip("ZA", alumno.za_nota)
                                    NotaChip("EF", alumno.ef_nota)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotaChip(label: String, valor: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 11.sp, color = Color.Gray)
        Text(
            valor?.replace(".00", "") ?: "-",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}