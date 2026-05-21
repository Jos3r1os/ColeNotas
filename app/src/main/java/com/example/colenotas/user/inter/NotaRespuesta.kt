package com.example.colenotas.user.inter

data class AlumnoConNotas(
    val id: Int,
    val nombre_completo: String,
    val nota_id: Int?,
    val as_nota: String?,
    val p1_nota: String?,
    val p2_nota: String?,
    val hw_nota: String?,
    val za_nota: String?,
    val ef_nota: String?,
    val punteo: String?
)

data class NotaRequest(
    val as_nota: Double?,
    val p1_nota: Double?,
    val p2_nota: Double?,
    val hw_nota: Double?,
    val za_nota: Double?,
    val ef_nota: Double?
)