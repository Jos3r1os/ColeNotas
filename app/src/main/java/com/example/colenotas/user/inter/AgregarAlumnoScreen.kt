package com.example.colenotas.user.inter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun AgregarAlumnoScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var cursos by remember { mutableStateOf<List<CursoRespuesta>>(emptyList()) }
    var cursosSeleccionados by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var cargandoCursos by remember { mutableStateOf(true) }
    var guardando by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var exitoMsg by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            val respuesta = RetrofitClient.api.obtenerTodosLosCursos()
            if (respuesta.isSuccessful) {
                cursos = respuesta.body() ?: emptyList()
            }
        } catch (e: Exception) { }
        cargandoCursos = false
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
            Text("Agregar Alumno", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                Text("Datos del alumno", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Nombre completo", fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = { Text("Ej: Juan Pérez", color = Color.LightGray) },
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
                Text("Inscribir en cursos", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                if (cargandoCursos) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (cursos.isEmpty()) {
                    Text("No hay cursos disponibles.", fontSize = 14.sp, color = Color.Gray)
                } else {
                    cursos.forEach { curso ->
                        val seleccionado = cursosSeleccionados.contains(curso.id)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    cursosSeleccionados = if (seleccionado) {
                                        cursosSeleccionados - curso.id
                                    } else {
                                        cursosSeleccionados + curso.id
                                    }
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = seleccionado,
                                onCheckedChange = {
                                    cursosSeleccionados = if (seleccionado) {
                                        cursosSeleccionados - curso.id
                                    } else {
                                        cursosSeleccionados + curso.id
                                    }
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF2C2C2C)
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    curso.nombre_curso,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(curso.grado, fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                    }
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
                    if (nombre.isEmpty()) {
                        errorMsg = "El nombre es obligatorio"
                        return@Button
                    }
                    scope.launch {
                        guardando = true
                        errorMsg = ""
                        exitoMsg = ""
                        try {
                            val respuesta = RetrofitClient.api.crearAlumno(
                                mapOf("nombre_completo" to nombre)
                            )
                            if (respuesta.isSuccessful) {
                                val alumno = respuesta.body()!!
                                cursosSeleccionados.forEach { cursoId ->
                                    RetrofitClient.api.inscribirAlumno(
                                        mapOf(
                                            "alumno_id" to alumno.id.toString(),
                                            "curso_id" to cursoId.toString()
                                        )
                                    )
                                }
                                exitoMsg = "Alumno creado correctamente"
                                nombre = ""
                                cursosSeleccionados = emptySet()
                            } else {
                                errorMsg = "Error al crear el alumno"
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
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Guardar alumno", color = Color.White, fontSize = 15.sp)
                }
            }
        }
    }
}