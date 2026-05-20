package com.example.colenotas.user.inter

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("api/usuarios/login")
    suspend fun login(@Body request: Map<String, String>): Response<UsuarioRespuesta>

    @GET("api/cursos/docente/{id}")
    suspend fun obtenerCursosPorDocente(@Path("id") id: Int): Response<List<CursoRespuesta>>

    @Multipart
    @POST("api/usuarios/foto/{id}")
    suspend fun subirFoto(
        @Path("id") id: Int,
        @Part foto: MultipartBody.Part
    ): Response<FotoRespuesta>
}