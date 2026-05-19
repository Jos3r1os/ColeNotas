package com.example.colenotas.user.inter

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.colenotas.user.inter.UsuarioRespuesta

interface ApiService {
    @POST("api/usuarios/login")
    fun login(@Body request: Map<String, String>): Call<UsuarioRespuesta>
}