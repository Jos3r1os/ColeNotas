package com.example.colenotas.user.inter

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun PerfilScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var subiendo by remember { mutableStateOf(false) }
    var mensajeFoto by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            fotoUri = uri
            scope.launch {
                subiendo = true
                mensajeFoto = ""
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes() ?: return@launch
                    inputStream.close()

                    val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                    val part = MultipartBody.Part.createFormData("foto", "foto.jpg", requestBody)

                    val respuesta = RetrofitClient.api.subirFoto(SesionUsuario.id, part)
                    if (respuesta.isSuccessful) {
                        val fotoUrl = respuesta.body()?.foto_url ?: ""
                        SesionUsuario.fotoUrl = fotoUrl
                        mensajeFoto = "Foto actualizada"
                    } else {
                        mensajeFoto = "Error al subir la foto"
                    }
                } catch (e: Exception) {
                    mensajeFoto = "Error: ${e.javaClass.simpleName}: ${e.message}"
                }
                subiendo = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
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
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Perfil",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        HorizontalDivider()

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(width = 300.dp, height = 250.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFDDDDDD)),
            contentAlignment = Alignment.Center
        ) {
            when {
                fotoUri != null -> {
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                SesionUsuario.fotoUrl.isNotEmpty() -> {
                    AsyncImage(
                        model = "http://100.95.36.52:3000${SesionUsuario.fotoUrl}",
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    Text("📷", fontSize = 48.sp)
                }
            }

            if (subiendo) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
        }

        if (mensajeFoto.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mensajeFoto,
                fontSize = 12.sp,
                color = if (mensajeFoto.contains("Error")) Color.Red else Color(0xFF1565C0)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = SesionUsuario.nombre.ifEmpty { "Usuario" },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        val rolTexto = when (SesionUsuario.rol) {
            "admin" -> "Administrador"
            "docente" -> "Docente"
            "alumno" -> "Alumno"
            else -> SesionUsuario.rol
        }

        val rolColor = when (SesionUsuario.rol) {
            "admin" -> Color(0xFFD32F2F)
            "docente" -> Color(0xFF1565C0)
            else -> Color.Gray
        }

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = rolColor.copy(alpha = 0.1f)
        ) {
            Text(
                text = rolTexto,
                fontSize = 13.sp,
                color = rolColor,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = SesionUsuario.correo.ifEmpty { "" },
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { launcher.launch("image/*") },
            enabled = !subiendo,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
        ) {
            Text("Subir Foto de Perfil", color = Color.White, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
        ) {
            Text("Vincular con Google", color = Color.White, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                SesionUsuario.id = 0
                SesionUsuario.nombre = ""
                SesionUsuario.rol = ""
                SesionUsuario.correo = ""
                SesionUsuario.fotoUrl = ""
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text("Log Out", color = Color.White, fontSize = 15.sp)
        }
    }
}