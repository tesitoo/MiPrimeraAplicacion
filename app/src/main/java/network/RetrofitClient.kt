package com.example.miprimeraaplicacion.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // URL base de MockAPI
    private const val BASE_URL = "https://69320f6811a8738467d165bd.mockapi.io/"

    // Instancia de Retrofit que usaré en toda la app
    val instance: APIService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Para convertir JSON
            .build()
            .create(APIService::class.java)
    }
}
