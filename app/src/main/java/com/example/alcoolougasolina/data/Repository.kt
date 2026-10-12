package com.example.alcoolougasolina.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Repository(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("postos", Context.MODE_PRIVATE) // SharedPreferences para salvar os postos
    private val gson = Gson() // Gson para converter os objetos Posto em JSON e vice-versa
    private val CHAVE_POSTOS = "lista_postos_json"

    fun obterTodos(): List<Posto> { // Função para obter todos os postos cadastrados
        val json = sharedPreferences.getString(CHAVE_POSTOS, null) ?: return emptyList()
        val tipoLista = object : TypeToken<List<Posto>>() {}.type
        return gson.fromJson(json, tipoLista) ?: emptyList()
    }

    private fun salvarTodos(postos: List<Posto>) { // Função para salvar todos os postos cadastrados
        val json = gson.toJson(postos)
        sharedPreferences.edit().putString(CHAVE_POSTOS, json).apply()
    }

    fun salvar(posto: Posto) { // Função para salvar um novo posto cadastrado
        val postos = obterTodos().toMutableList()
        postos.add(posto)
        salvarTodos(postos)
    }

    fun atualizar(posto: Posto) { // Função para atualizar um posto cadastrado
        val postos = obterTodos().toMutableList()
        val index = postos.indexOfFirst { it.id == posto.id }
        if (index != -1) {
            postos[index] = posto
            salvarTodos(postos)
        }
    }

    fun excluir(idDoPosto: String) { // Função para excluir um posto cadastrado
        val postos = obterTodos().toMutableList()
        postos.removeAll { it.id == idDoPosto }
        salvarTodos(postos)
    }
}