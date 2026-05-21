package com.example.colenotas.user.inter

data class DocenteConCursos(
    val id: Int,
    val nombre_completo: String,
    val cursos: List<CursoRespuesta>? = null
)