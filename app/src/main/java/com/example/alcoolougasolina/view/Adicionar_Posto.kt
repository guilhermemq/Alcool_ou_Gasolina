package com.example.alcoolougasolina.view

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.alcoolougasolina.data.Posto
import com.example.alcoolougasolina.data.Repository
import com.google.android.gms.location.LocationServices
import java.util.Locale
import android.Manifest
import android.R.attr.data
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.AlertDialog
import android.provider.Settings
import androidx.compose.ui.res.stringResource
import com.example.alcoolougasolina.R

@SuppressLint("MissingPermission")
fun CapturarEnderecoAtual(
    context: Context,
    onEnderecoEncontrado: (endereco: String, latitude: Double, longitude: Double) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    // Buscamos a localização atual do usuário por meio da latitiude e longitude
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude

            val geocoder = Geocoder(context, Locale.getDefault())
            // Traduzindo a latitude e longitude para um endereço
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                ) { addresses ->
                    val address = addresses.firstOrNull()
                    val rua = address?.thoroughfare ?: ""
                    val numero = address?.subThoroughfare ?: ""
                    val bairro = address?.subLocality ?: ""
                    val enderecoFormatado = if(rua.isNotBlank()) "$rua $numero - $bairro".trim()
                                              else "Latitude: $latitude, Longitude: $longitude"
                    onEnderecoEncontrado(enderecoFormatado, latitude, longitude)
                }
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                )
                val address = addresses?.firstOrNull()
                if (address != null) {
                    val rua = address.thoroughfare ?: ""
                    val numero = address.subThoroughfare ?: ""
                    val bairro = address.subLocality ?: ""
                    val enderecoFormatado = if(rua.isNotBlank()) "$rua $numero - $bairro".trim()
                                            else "Latitude: $latitude, Longitude: $longitude"
                    onEnderecoEncontrado(enderecoFormatado, latitude, longitude)
                }
            }
        }
    }
}

@Composable
fun AdicionarPosto(
    navController: NavHostController,
    postoId: String? = null,
    nome: String = "",
    alcool: String = "",
    gasolina: String = ""
) {
    val context = LocalContext.current
    val repository = remember(context) { Repository(context) }

    var listaPostos by remember { mutableStateOf(repository.obterTodos()) }

    var nomeDoPosto by rememberSaveable { mutableStateOf(nome) }
    var precoAlcool by rememberSaveable { mutableStateOf(alcool) }
    var precoGasolina by rememberSaveable { mutableStateOf(gasolina) }
    var localizacao by rememberSaveable { mutableStateOf("") }
    var latitudePosto by rememberSaveable { mutableStateOf<Double?>(null) }
    var longitudePosto by rememberSaveable { mutableStateOf<Double?>(null) }

    var postoEmEdicao by remember { mutableStateOf<Posto?>(null) }
    var postoInvalido by remember { mutableStateOf(false) }
    var permissaoDialogo by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions -> // Pedimos a permissão do usuário para acessar a localização aproximada e a precisa
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) { // Se o usuário aceitou a permissão, capturamos a localização atual
            CapturarEnderecoAtual(context) { endereco, latitude, longitude ->
                localizacao = endereco
                latitudePosto = latitude
                longitudePosto = longitude
            }
        } else { // Se o usuário não aceitou a permissão, exibimos um diálogo que avisa que não será possível usar a localização,
            permissaoDialogo = true // além disso, o usuário é aconselhado a mudar as configurações de permissão
        }
    }

    LaunchedEffect(postoId) { // Buscamos as informações do posto que está sendo editado
        if (postoId != null) {
            val postoEncontrado = repository.obterTodos().find { it.id == postoId }
            if (postoEncontrado != null) {
                postoEmEdicao = postoEncontrado
                nomeDoPosto = postoEncontrado.nome
                precoAlcool = postoEncontrado.precoAlcool.toString()
                precoGasolina = postoEncontrado.precoGasolina.toString()
                localizacao = postoEncontrado.localizacao
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(56.dp)
        )
        Text( // Título do formulário, muda de acordo com a ação que está sendo realizada
            text = if (postoEmEdicao == null) stringResource(R.string.adicionar_posto) else stringResource(R.string.editar_posto),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(
            modifier = Modifier.height(96.dp)
        )
        OutlinedTextField(
            value = nomeDoPosto,
            onValueChange = { nomeDoPosto = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(stringResource(R.string.nome_do_posto)) }
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        OutlinedTextField(
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
        OutlinedTextField(
            value = precoGasolina,
            onValueChange = { precoGasolina = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(stringResource(R.string.preco_da_gasolina)) }
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        OutlinedTextField(
            value = localizacao,
            onValueChange = { localizacao = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(stringResource(R.string.localizacao_do_posto)) },
            readOnly = true
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        OutlinedButton(
            onClick = {
                permissionLauncher.launch( // Pedimos a permissão do usuário para acessar a localização aproximada e a precisa
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        ) {
            Text(stringResource(R.string.usar_minha_localizacao_atual))
        }
        Spacer(
            modifier = Modifier.height(36.dp)
        )
        Button(
            onClick = {
                val pAlcool = precoAlcool.replace(",", ".").toDoubleOrNull() ?: 0.0
                val pGasolina = precoGasolina.replace(",", ".").toDoubleOrNull() ?: 0.0

                if (nomeDoPosto.isNotBlank() && pAlcool > 0.0 && pGasolina > 0.0) { // Verificamos se os campos estão preenchidos corretamente
                    if (postoEmEdicao == null) { // Se o posto não estiver sendo editado, criamos um novo posto
                        val novoPosto = Posto(
                            nome = nomeDoPosto,
                            precoAlcool = pAlcool,
                            precoGasolina = pGasolina,
                            localizacao = localizacao,
                            latitude = latitudePosto,
                            longitude = longitudePosto
                        )
                        repository.salvar(novoPosto)
                    } else { // Se o posto estiver sendo editado, atualizamos as informações do posto
                        val postoAtualizado = postoEmEdicao!!.copy(
                            nome = nomeDoPosto,
                            precoAlcool = pAlcool,
                            precoGasolina = pGasolina,
                            localizacao = localizacao,
                            latitude = latitudePosto ?: postoEmEdicao!!.latitude,
                            longitude = longitudePosto ?: postoEmEdicao!!.longitude
                        )
                        repository.atualizar(postoAtualizado)
                        postoEmEdicao = null
                    }

                    listaPostos = repository.obterTodos()

                    nomeDoPosto = ""
                    precoAlcool = ""
                    precoGasolina = ""
                    localizacao = ""

                    navController.navigate("lista_postos") { // Navegamos para a tela de lista de postos
                        popUpTo("lista_postos") {
                            inclusive = true
                        }
                    }
                } else { // Se os campos estiverem vazios ou indevidos, marcamos o erro
                    postoInvalido = true
                }
            }
        ) {
            Text(if (postoEmEdicao == null) stringResource(R.string.salvar) else stringResource(R.string.atualizar))
        }
        if (postoInvalido) { // Se o posto for inválido, exibimos uma mensagem de erro
            Text(
                text = stringResource(R.string.porfavor_preencha_todos_os_campos_corretamente),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
    if (permissaoDialogo) { // Caixa de alerta sobre a permissão da localização
        AlertDialog(
            onDismissRequest = { permissaoDialogo = false },
            title = { Text(stringResource(R.string.localizacao)) },
            text = { Text(stringResource(R.string.para_preencher_o_endereco_automaticamente)) },
            confirmButton = {
                Button(
                    onClick = {
                        permissaoDialogo = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Text(stringResource(R.string.configuracoes))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { permissaoDialogo = false }
                ) {
                    Text(stringResource(R.string.cancelar))
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AdicionarPostoPreview() {
    AdicionarPosto(navController = NavHostController(LocalContext.current))
}

