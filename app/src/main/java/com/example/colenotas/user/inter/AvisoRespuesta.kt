package com.example.colenotas.user.inter

data class AvisoRespuesta(
    val id: Int,
    val titulo: String,
    val mensaje: String,
    val urgente: Boolean,
    val fecha: String,
    val creado_en: String,
    val enviado_por: String
)

data class AvisoRequest(
    val titulo: String,
    val mensaje: String,
    val urgente: Boolean,
    val fecha: String,
    val admin_id: Int
)