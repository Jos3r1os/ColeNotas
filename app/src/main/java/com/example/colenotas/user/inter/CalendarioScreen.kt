package com.example.colenotas.user.inter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarioScreen() {
    var eventos by remember { mutableStateOf<List<EventoRespuesta>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var mesActual by remember { mutableStateOf(YearMonth.now()) }
    var diaSeleccionado by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(Unit) {
        try {
            val respuesta = RetrofitClient.api.obtenerEventos()
            if (respuesta.isSuccessful) {
                eventos = respuesta.body() ?: emptyList()
            }
        } catch (e: Exception) { }
        cargando = false
    }

    val fechasConEventos = eventos.mapNotNull { evento ->
        runCatching { LocalDate.parse(evento.fecha.substring(0, 10)) }.getOrNull()
    }.toSet()

    val eventosDiaSeleccionado = diaSeleccionado?.let { dia ->
        eventos.filter { evento ->
            runCatching { LocalDate.parse(evento.fecha.substring(0, 10)) == dia }.getOrDefault(false)
        }
    } ?: emptyList()

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
            Text(
                text = "Calendario Ciclo 2026",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "<",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { mesActual = mesActual.minusMonths(1) }
            )
            Text(
                "${mesActual.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${mesActual.year}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                ">",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { mesActual = mesActual.plusMonths(1) }
            )
        }

        HorizontalDivider()

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb").forEach { dia ->
                Text(
                    text = dia,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        val primerDia = mesActual.atDay(1)
        val diasEnMes = mesActual.lengthOfMonth()
        val offsetInicio = primerDia.dayOfWeek.value % 7
        val totalCeldas = offsetInicio + diasEnMes
        val filas = (totalCeldas + 6) / 7

        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            repeat(filas) { fila ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    repeat(7) { columna ->
                        val indice = fila * 7 + columna
                        val numeroDia = indice - offsetInicio + 1
                        val fecha = if (numeroDia in 1..diasEnMes)
                            mesActual.atDay(numeroDia) else null
                        val tieneEvento = fecha != null && fechasConEventos.contains(fecha)
                        val esSeleccionado = fecha != null && fecha == diaSeleccionado
                        val esHoy = fecha != null && fecha == LocalDate.now()

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .background(
                                    when {
                                        esSeleccionado -> Color(0xFF2C2C2C)
                                        esHoy -> Color(0xFFEEEEEE)
                                        else -> Color.Transparent
                                    },
                                    CircleShape
                                )
                                .clickable(enabled = fecha != null) {
                                    diaSeleccionado = if (diaSeleccionado == fecha) null else fecha
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (fecha != null) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$numeroDia",
                                        fontSize = 13.sp,
                                        color = if (esSeleccionado) Color.White else Color.Black,
                                        fontWeight = if (esHoy) FontWeight.Bold else FontWeight.Normal
                                    )
                                    if (tieneEvento) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .background(
                                                    if (esSeleccionado) Color.White
                                                    else Color(0xFFD32F2F),
                                                    CircleShape
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (diaSeleccionado != null) {
                Text(
                    "Eventos del ${diaSeleccionado.toString()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                if (eventosDiaSeleccionado.isEmpty()) {
                    Text("No hay eventos este día.", fontSize = 13.sp, color = Color.Gray)
                } else {
                    eventosDiaSeleccionado.forEach { evento ->
                        EventoItem(evento = evento)
                    }
                }
            } else {
                Text("Próximos eventos", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                if (eventos.isEmpty()) {
                    Text("No hay eventos registrados.", fontSize = 13.sp, color = Color.Gray)
                } else {
                    eventos.take(5).forEach { evento ->
                        EventoItem(evento = evento)
                    }
                }
            }
        }
    }
}

@Composable
fun EventoItem(evento: EventoRespuesta) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFFD32F2F), CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(evento.nombre, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Text(
                    "${evento.fecha.substring(0, 10)} — ${evento.creado_por}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (!evento.comentario.isNullOrEmpty()) {
                    Text(evento.comentario, fontSize = 12.sp, color = Color.DarkGray)
                }
            }
        }
    }
}