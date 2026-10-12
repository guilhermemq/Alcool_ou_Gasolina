package com.example.alcoolougasolina.view

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.alcoolougasolina.R

// Função que calcula qual combustível vale mais a pena
fun calcularPreco(precoA: Double, precoG: Double, ativado: Boolean): Int {
    if (ativado) { // 75%
        if (precoA / precoG > 0.75) {
            return R.string.gasolina
        } else {
            return R.string.alcool
        }
    } else { // 70%
        if (precoA / precoG > 0.70) {
            return R.string.gasolina
        } else {
            return R.string.alcool
        }
    }
}
@Composable
fun TelaPrincipal(navController: NavHostController) {
    var precoAlcool by rememberSaveable { mutableStateOf("") }
    var precoGasolina by rememberSaveable { mutableStateOf("") }
    var nomePosto by rememberSaveable { mutableStateOf("")}
    var ativado by rememberSaveable { mutableStateOf(false) }
    var melhorPreco by rememberSaveable { mutableStateOf<Int?>(null) }


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
            text = stringResource(R.string.alcool_ou_gasolina),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(
            modifier = Modifier.height(96.dp)
        )
        OutlinedTextField( // Campo para inserir o nome do posto
            value = nomePosto,
            onValueChange = { nomePosto = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(stringResource(R.string.nome_do_posto_opcional)) }
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        OutlinedTextField( // Campo para inserir o preço do álcool
            value = precoAlcool,
            onValueChange = { precoAlcool = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(stringResource(R.string.preco_do_alcool)) }
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        OutlinedTextField( // Campo para inserir o preço da gasolina
            value = precoGasolina,
            onValueChange = { precoGasolina = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(stringResource(R.string.preco_da_gasolina)) }
        )
        Spacer(
            modifier = Modifier.height(24.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.porcentagem_do_alcool),
                fontSize = 20.sp,
                color = Color.White
            )
            Switch( // Switch que controla a porcentagem de álcool utilizada no cálculo
                checked = ativado,
                onCheckedChange = { ativado = it },
                thumbContent = {
                    Text(
                        text = if (ativado) "75" else "70",
                        fontSize = 12.sp
                    )
                }
            )
        }
        Spacer(
            modifier = Modifier.height(24.dp)
        )
        Button(
            onClick = {
                val pAlcool = precoAlcool.replace(",", ".").toDoubleOrNull() ?: 0.0
                val pGasolina = precoGasolina.replace(",", ".").toDoubleOrNull() ?: 0.0

                if (pAlcool > 0.0 && pGasolina > 0.0) {
                    melhorPreco = calcularPreco(pAlcool, pGasolina, ativado)
                } else { // Tratamento de erro, caso em que o usuário não preenche os campos corretamente
                    melhorPreco = R.string.preencha_os_precos_dos_combustiveis
                }
            }
        ) {
            Text(stringResource(R.string.calcular))
        }
        if (melhorPreco != null) {
            Spacer(
                modifier = Modifier.height(24.dp)
            )
            Text(
                text = stringResource(melhorPreco!!),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(
            modifier = Modifier.height(36.dp)
        )
        Button( // Botão que leva para o Lista_Postos.kt
            onClick = { navController.navigate("lista_postos")}
        ) {
            Text(stringResource(R.string.ver_lista_de_postos))
        }
        Spacer(
            modifier = Modifier.height(24.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton( // Botão que leva para o Adicionar_Posto.kt com os parâmetros nome, alcool e gasolina
                onClick ={ navController.navigate("adicionar_posto?nome=$nomePosto&alcool=$precoAlcool&gasolina=$precoGasolina") }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Posto"
                )
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun TelaPrincipalPreview() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        TelaPrincipal(navController = NavHostController(LocalContext.current))
    }
}