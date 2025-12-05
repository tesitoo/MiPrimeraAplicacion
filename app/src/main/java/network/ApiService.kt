package com.example.miprimeraaplicacion.network

import com.example.miprimeraaplicacion.models.Mantenimiento
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    // Obtener todos los mantenimientos
    @GET("mantenimientos")
    fun getMantenimientos(): Call<List<Mantenimiento>>

    // Crear un mantenimiento nuevo
    @POST("mantenimientos")
    fun createMantenimiento(@Body mantenimiento: Mantenimiento): Call<Mantenimiento>

    // Actualizar un mantenimiento por id
    @PUT("mantenimientos/{id}")
    fun updateMantenimiento(
        @Path("id") id: String,
        @Body mantenimiento: Mantenimiento
    ): Call<Mantenimiento>

    // Eliminar un mantenimiento por id
    @DELETE("mantenimientos/{id}")
    fun deleteMantenimiento(@Path("id") id: String): Call<Void>
}
