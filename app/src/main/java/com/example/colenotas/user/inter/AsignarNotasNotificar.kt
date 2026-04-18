package com.example.colenotas.user.inter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryRed    = Color(0xFFD32F2F)
private val MediumGray    = Color(0xFF9E9E9E)
private val CheckBlue     = Color(0xFF1565C0)
private val TextPrimary   = Color(0xFF212121)
private val TextSecondary = Color(0xFF757575)
private val DividerColor  = Color(0xFFE0E0E0)

data class EstudianteNota(
    val id: String,
    val nombre: String,
    val estado: String = "ASCENDIDO",
    val nota: String = "NOTA TOTAL (NOTA)",
    val checked: Boolean = true
)

@Composable
fun AsignarNotasNotificarScreen() {
    val estudiantes = remember {
        listOf(
            EstudianteNota("1190-24-399022", "Hector Leon"),
            EstudianteNota("1190-24-399022", "Kanji Tatsumi"),
            EstudianteNota("1190-24-399022", "Ann Takamaki"),
            EstudianteNota("1190-24-399022", "Yosuke Hanamura"),
            EstudianteNota("1190-24-399022", "Hector Leon")
        )
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
            Text(text = "Asignar Notas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        HorizontalDivider(color = DividerColor)

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = MediumGray, modifier = Modifier.size(22.dp))
                Column {
                    Text("Calculo I", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
                    Text("Segundo Semestre", fontSize = 12.sp, color = TextSecondary)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Icon(imageVector = Icons.Outlined.KeyboardArrowUp, contentDescription = null, tint = MediumGray, modifier = Modifier.size(16.dp))
                Text("A", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
            }
        }

        HorizontalDivider(color = DividerColor)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(estudiantes) { estudiante ->
                EstudianteNotaItem(estudiante = estudiante)
                HorizontalDivider(color = DividerColor, thickness = 0.5.dp)
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed, contentColor = Color.White)
            ) {
                Text("Notificar Notas Ingresadas", fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun EstudianteNotaItem(estudiante: EstudianteNota) {
    var checked by remember { mutableStateOf(estudiante.checked) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = estudiante.id, fontSize = 12.sp, color = TextSecondary)
            Text(text = estudiante.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
        }
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = CheckboxDefaults.colors(checkedColor = CheckBlue, uncheckedColor = MediumGray)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = estudiante.estado, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
            Text(text = estudiante.nota, fontSize = 11.sp, color = TextSecondary)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AsignarNotasNotificarPreview() {
    MaterialTheme { AsignarNotasNotificarScreen() }
}