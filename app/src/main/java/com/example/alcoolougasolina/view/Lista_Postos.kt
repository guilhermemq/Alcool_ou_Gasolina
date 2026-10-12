package com.example.alcoolougasolina.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.alcoolougasolina.R
import com.example.alcoolougasolina.data.Posto
import com.example.alcoolougasolina.data.Repository

@Composable
fun ListaPostos(navController: NavHostController) {
    val context = LocalContext.current
    val repository = remember(context) { Repository(context) }
    // Buscamos todos os postos cadastrados
    var listaPostos by remember { mutableStateOf(repository.obterTodos()) }
    var postoEmExclusao by remember { mutableStateOf<Posto?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(56.dp)
        )
        Text(
            text = stringResource(R.string.lista_postos),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(
            modifier = Modifier.height(32.dp)
        )
        if (listaPostos.isEmpty()) { // Se não houver postos cadastrados, exibimos uma mensagem
            Text(
                text = stringResource(R.string.nenhum_posto_cadastrado),
                color = Color.Gray,
                fontSize = 18.sp
            )
        } else {
            LazyColumn( // Se houver postos cadastrados, exibimos uma lista com todos eles
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(
                    items = listaPostos,
                    key = { posto -> posto.id }
                ) {
                    posto ->
                    PostoItemCard(
                        posto = posto,
                        onCardClick = { // Ao clicar no card, navega para a tela de detalhes do posto
                            navController.navigate("posto_detalhe/${posto.id}")
                        },
                        onEditarClick = { // Ao clicar no botão de editar, navega para a tela de adicionar/editar posto
                            navController.navigate("adicionar_posto?postoId=${posto.id}")
                        },
                        onExcluirClick = { // Ao clicar no botão de excluir, exibe um diálogo de confirmação
                            postoEmExclusao = posto
                        }
                    )
                }
            }
        }

    }
    if (postoEmExclusao != null) { // Caixa de alerta de confirmação de exclusão
        AlertDialog(
            onDismissRequest = { postoEmExclusao = null },
            title = { Text(stringResource(R.string.confirmar_exclusao)) },
            text = { Text(stringResource(R.string.voce_realmente_deseja_excluir_o_posto)) },
            confirmButton = {
                Button(
                    onClick = {
                        postoEmExclusao?.let {
                            repository.excluir(it.id)
                            listaPostos = repository.obterTodos()
                        }
                        postoEmExclusao = null
                    }
                ) {
                    Text(stringResource(R.string.sim))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { postoEmExclusao = null }
                ) {
                    Text(stringResource(R.string.nao))
                }
            }
        )
    }
}