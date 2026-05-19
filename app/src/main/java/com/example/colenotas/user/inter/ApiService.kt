package com.example.colenotas.user.inter

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/usuarios/login")
    suspend fun login(@Body request: Map<String, String>): Response<UsuarioRespuesta>
}