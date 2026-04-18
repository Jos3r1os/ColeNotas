package com.example.colenotas.user.inter

import androidx.compose.foundation.BorderStroke
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
fun AsignarNotasScreen() {
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
                    Text("Calculo I", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text("Segundo Semestre", fontSize = 14.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "", modifier = Modifier.size(18.dp))
                Text("A", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("1190-24-399022", fontSize = 13.sp, color = Color.Gray)
            Text("Hector Leon", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                Checkbox(
                    checked = true,
                    onCheckedChange = {},
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2C2C2C))
                )
                Column {
                    Text("ASCENDIDO", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("NOTA TOTAL (NOTA)", fontSize = 13.sp, color = Color.Gray)
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                CampoNota("AS", Modifier.weight(1f))
                CampoNota("P1", Modifier.weight(1f))
                CampoNota("P2", Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                CampoNota("HW", Modifier.weight(1f))
                CampoNota("ZA", Modifier.weight(1f))
                CampoNota("EF", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.6f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Agregar Datos", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            AlumnoItem("1190-24-399022", "Kanji Tatsumi")
            AlumnoItem("1190-24-399022", "Ann Takamaki")
            AlumnoItem("1190-24-399022", "Yosuke Hanamura")
        }
    }
}

@Composable
fun CampoNota(label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(4.dp)) {
        Text(label, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
        Surface(
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier.height(45.dp).fillMaxWidth()
        ) {
            Box(contentAlignment = Alignment.CenterStart) {
                Text("Value", modifier = Modifier.padding(start = 12.dp), color = Color.Black, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun AlumnoItem(codigo: String, nombre: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(codigo, fontSize = 12.sp, color = Color.Gray)
        Text(nombre, fontWeight = FontWeight.Medium, fontSize = 15.sp)
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = Color(0xFFEEEEEE))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AsignarNotasPreview() {
    MaterialTheme { AsignarNotasScreen() }
}