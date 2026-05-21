package com.example.colenotas.user.inter

data class EventoRespuesta(
    val id: Int,
    val nombre: String,
    val fecha: String,
    val comentario: String?,
    val todo_el_dia: Boolean,
    val creado_por: String
)

data class EventoRequest(
    val nombre: String,
    val fecha: String,
    val comentario: String,
    val todo_el_dia: Boolean,
    val creado_por: Int
)