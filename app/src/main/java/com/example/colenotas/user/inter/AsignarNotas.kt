package com.example.colenotas.user.inter

import androidx.compose.foundation.BorderStroke
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
import kotlinx.coroutines.launch

@Composable
fun AsignarNotasScreen(cursoId: Int = 0, nombreCurso: String = "") {
    var alumnos by remember { mutableStateOf<List<AlumnoConNotas>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var alumnoSeleccionado by remember { mutableStateOf<AlumnoConNotas?>(null) }

    var asNota by remember { mutableStateOf("") }
    var p1Nota by remember { mutableStateOf("") }
    var p2Nota by remember { mutableStateOf("") }
    var hwNota by remember { mutableStateOf("") }
    var zaNota by remember { mutableStateOf("") }
    var efNota by remember { mutableStateOf("") }

    var guardando by remember { mutableStateOf(false) }
    var mensajeExito by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(cursoId) {
        try {
            val respuesta = RetrofitClient.api.obtenerAlumnosPorCurso(cursoId)
            if (respuesta.isSuccessful) {
                alumnos = respuesta.body() ?: emptyList()
            }
        } catch (e: Exception) { }
        cargando = false
    }

    fun seleccionarAlumno(alumno: AlumnoConNotas) {
        alumnoSeleccionado = alumno
        asNota = alumno.as_nota?.replace(".00", "") ?: ""
        p1Nota = alumno.p1_nota?.replace(".00", "") ?: ""
        p2Nota = alumno.p2_nota?.replace(".00", "") ?: ""
        hwNota = alumno.hw_nota?.replace(".00", "") ?: ""
        zaNota = alumno.za_nota?.replace(".00", "") ?: ""
        efNota = alumno.ef_nota?.replace(".00", "") ?: ""
        mensajeExito = ""
        mensajeError = ""
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
            Text(text = "Asignar Notas", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        HorizontalDivider()

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        nombreCurso.replace("%20", " "),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (alumnoSeleccionado != null) {
                val alumno = alumnoSeleccionado!!

                Text(alumno.nombre_completo, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                if (alumno.punteo != null) {
                    Text(
                        "Nota total: ${alumno.punteo}",
                        fontSize = 13.sp,
                        color = Color(0xFF1565C0),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    CampoNotaEditable("AS", asNota, Modifier.weight(1f)) { asNota = it }
                    CampoNotaEditable("P1", p1Nota, Modifier.weight(1f)) { p1Nota = it }
                    CampoNotaEditable("P2", p2Nota, Modifier.weight(1f)) { p2Nota = it }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    CampoNotaEditable("HW", hwNota, Modifier.weight(1f)) { hwNota = it }
                    CampoNotaEditable("ZA", zaNota, Modifier.weight(1f)) { zaNota = it }
                    CampoNotaEditable("EF", efNota, Modifier.weight(1f)) { efNota = it }
                }

                if (mensajeExito.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensajeExito, color = Color(0xFF1565C0), fontSize = 13.sp)
                }
                if (mensajeError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensajeError, color = Color.Red, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { alumnoSeleccionado = null },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                guardando = true
                                mensajeExito = ""
                                mensajeError = ""
                                try {
                                    val request = NotaRequest(
                                        as_nota = asNota.toDoubleOrNull(),
                                        p1_nota = p1Nota.toDoubleOrNull(),
                                        p2_nota = p2Nota.toDoubleOrNull(),
                                        hw_nota = hwNota.toDoubleOrNull(),
                                        za_nota = zaNota.toDoubleOrNull(),
                                        ef_nota = efNota.toDoubleOrNull()
                                    )
                                    val respuesta = RetrofitClient.api.guardarNota(
                                        alumno.id, cursoId, request
                                    )
                                    if (respuesta.isSuccessful) {
                                        mensajeExito = "Notas guardadas correctamente"
                                        val nuevaRespuesta = RetrofitClient.api.obtenerAlumnosPorCurso(cursoId)
                                        if (nuevaRespuesta.isSuccessful) {
                                            alumnos = nuevaRespuesta.body() ?: emptyList()
                                            alumnoSeleccionado = alumnos.find { it.id == alumno.id }
                                        }
                                    } else {
                                        mensajeError = "Error al guardar las notas"
                                    }
                                } catch (e: Exception) {
                                    mensajeError = "No se pudo conectar: ${e.message}"
                                }
                                guardando = false
                            }
                        },
                        enabled = !guardando,
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (guardando) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text("Guardar", color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text("Alumnos del curso", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            if (alumnos.isEmpty() && !cargando) {
                Text("No hay alumnos inscritos.", fontSize = 14.sp, color = Color.Gray)
            } else {
                alumnos.forEach { alumno ->
                    AlumnoItemClickable(
                        nombre = alumno.nombre_completo,
                        tieneNotas = alumno.punteo != null,
                        seleccionado = alumnoSeleccionado?.id == alumno.id,
                        onClick = { seleccionarAlumno(alumno) }
                    )
                }
            }
        }
    }
}

@Composable
fun CampoNotaEditable(
    label: String,
    valor: String,
    modifier: Modifier = Modifier,
    onValorChange: (String) -> Unit
) {
    Column(modifier = modifier.padding(4.dp)) {
        Text(label, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            modifier = Modifier.height(52.dp).fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
        )
    }
}

@Composable
fun AlumnoItemClickable(
    nombre: String,
    tieneNotas: Boolean,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (seleccionado) Color(0xFFEEEEEE) else Color.Transparent)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(nombre, fontWeight = FontWeight.Medium, fontSize = 15.sp)
        }
        if (tieneNotas) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF1565C0).copy(alpha = 0.1f)
            ) {
                Text(
                    "Con notas",
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "",
            tint = Color.Gray
        )
    }
    HorizontalDivider(color = Color(0xFFEEEEEE))
}