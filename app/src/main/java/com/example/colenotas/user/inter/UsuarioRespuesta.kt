package com.example.colenotas.user.inter

data class UsuarioData(
    val id: Int,
    val nombre: String? = null,
    val rol: String
)
data class UsuarioRespuesta(
    val mensaje: String,
    val usuario: UsuarioData
)