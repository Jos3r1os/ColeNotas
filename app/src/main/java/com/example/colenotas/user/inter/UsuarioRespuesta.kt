package com.example.colenotas.user.inter

data class UsuarioRespuesta(
    val id: Int,
    val correo: String,
    val rol: String,
    val nombre_completo: String
)