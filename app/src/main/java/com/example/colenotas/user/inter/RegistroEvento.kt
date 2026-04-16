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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroEventoScreen(navController: NavHostController) {

    var nombreEvento by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }
    var todoElDia by remember { mutableStateOf(false) }
    var calendarioPicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

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
                Text("IMG")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Calendario Ciclo 2026",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider()

        Spacer(modifier = Modifier.height(85.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {

                Text(
                    text = "Registrar Evento",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Registra el evento",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text("Evento", fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = nombreEvento,
                    onValueChange = { nombreEvento = it },
                    placeholder = { Text("Nombre", color = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Fecha", fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("dd/mm/yyyy", color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { calendarioPicker = true },
                    enabled = false,
                    shape = RoundedCornerShape(8.dp),

                )

                if (calendarioPicker) {
                    DatePickerDialog(
                        onDismissRequest = { calendarioPicker = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val millis = datePickerState.selectedDateMillis
                                    if (millis != null) {
                                        val sdf = java.text.SimpleDateFormat(
                                            "dd/MM/yyyy",
                                            java.util.Locale.getDefault()
                                        )
                                        fecha = sdf.format(java.util.Date(millis))
                                    }
                                    calendarioPicker = false
                                }
                            ) {
                                Text("Confirmar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { calendarioPicker = false }) {
                                Text("Cancelar")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Comentario", fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    placeholder = { Text("...", color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(12.dp))


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = todoElDia,
                        onCheckedChange = { todoElDia = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.Black
                        )
                    )
                    Text("Todo el día?", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Guardar evento",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroEventoscreenPreview() {
    RegistroEventoScreen(navController = rememberNavController())
}