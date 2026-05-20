package com.example.colenotas.user.inter

data class UsuarioData(
    val id: Int,
    val nombre: String? = null,
    val rol: String,
    val foto_url: String? = null
)
data class UsuarioRespuesta(
    val mensaje: String,
    val usuario: UsuarioData
)