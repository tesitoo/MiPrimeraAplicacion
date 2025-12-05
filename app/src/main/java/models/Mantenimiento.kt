package com.example.miprimeraaplicacion.models

data class Mantenimiento(
    var id: String? = null,
    val nombre: String,
    val tipo: String,
    val fecha: String
)
