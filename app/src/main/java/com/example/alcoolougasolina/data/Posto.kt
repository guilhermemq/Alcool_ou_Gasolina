package com.example.alcoolougasolina.data

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class Posto( // Classe para representar um posto de gasolina
    val id: String = UUID.randomUUID().toString(), // ID único do posto
    val nome: String,
    val precoAlcool: Double,
    val precoGasolina: Double,
    val localizacao: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val dataCriacao: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()) // Data de criação do posto
) : Serializable