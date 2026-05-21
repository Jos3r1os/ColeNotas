package com.example.colenotas.user.inter

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
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

    @POST("api/avisos")
    suspend fun crearAviso(@Body request: AvisoRequest): Response<AvisoRespuesta>

    @GET("api/avisos")
    suspend fun obtenerAvisos(): Response<List<AvisoRespuesta>>

    @DELETE("api/avisos/{id}")
    suspend fun eliminarAviso(@Path("id") id: Int): Response<Unit>

    @PATCH("api/avisos/{id}/leido")
    suspend fun marcarComoLeido(@Path("id") id: Int): Response<AvisoRespuesta>

    @GET("api/cursos/docentes")
    suspend fun obtenerDocentesConCursos(): Response<List<DocenteConCursos>>
}