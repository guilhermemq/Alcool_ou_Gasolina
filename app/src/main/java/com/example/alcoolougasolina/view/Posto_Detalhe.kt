package com.example.alcoolougasolina.view

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.alcoolougasolina.R
import com.example.alcoolougasolina.data.Repository

fun AbrirNoGoogleMaps(
    context: Context,
    latitude: Double?,
    longitude: Double?,
    nomeDoPosto: String
) {
    if(latitude != null && longitude != null) { // Verificamos se a latitude e longitude existem
        val uriIntent = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude(${Uri.encode(nomeDoPosto)})") // Construímos a URI para abrir o Google Maps
        val mapIntent = Intent(Intent.ACTION_VIEW, uriIntent) // Criamos o Intent para abrir o mapa
        mapIntent.setPackage("com.google.android.apps.maps") // Especificamos o pacote do Google Maps

        try { // Tentamos abrir o mapa no Google Maps
            context.startActivity(mapIntent)
        } catch (e: Exception) { // Se não conseguir, abrimos o mapa no navegador
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
            )
            context.startActivity(webIntent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostoDetalhe(
    navController: NavHostController,
    postoId: String?
) {
    val context = LocalContext.current
    val repository = remember(context) { Repository(context) }
    val posto = remember(postoId) { // Buscamos o posto e suas informações pelo ID
        repository.obterTodos().find{ it.id == postoId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(stringResource(R.string.detalhes_do_posto), color = Color.White)},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Botão de voltar
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (posto == null) { // Se o posto não for encontrado, exibimos uma mensagem
            Text(stringResource(R.string.posto_nao_encontrado), color = Color.Gray)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = posto.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White
                )
                if (posto.localizacao.isNotBlank()) { // Se o posto tiver um endereço cadastrado, exibimos ele
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Text(
                        text = "\uD83D\uDCCD ${posto.localizacao}",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                } else { // Se não tiver, exibimos uma mensagem
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Text(
                        text = stringResource(R.string.sem_endereco_cadastrado),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                if (posto.latitude != null && posto.longitude != null) { // Se o posto tiver uma latitude e longitude,
                    Spacer(                                              //exibimos um botão para abrir no Google Maps
                        modifier = Modifier.height(8.dp)
                    )
                    Button(
                        onClick = {
                            AbrirNoGoogleMaps(
                                context = context,
                                latitude = posto.latitude,
                                longitude = posto.longitude,
                                nomeDoPosto = posto.nome
                            )
                        }
                    ) {
                        Text("Google Maps")
                    }
                }
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                Text( // Exibimos a data de criação do posto
                    text = stringResource(R.string.cadastrado_em, posto.dataCriacao),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text( // Exibimos o preço do álcool
                            text = stringResource(R.string.preco_do_alcool_formatado, posto.precoAlcool),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )
                        Text( // Exibimos o preço da gasolina
                            text = stringResource(R.string.preco_da_gasolina_formatado, posto.precoGasolina),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
