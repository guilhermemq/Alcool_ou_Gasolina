package com.example.alcoolougasolina

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.alcoolougasolina.view.TelaPrincipal
import com.example.alcoolougasolina.ui.theme.AlcoolOuGasolinaTheme
import com.example.alcoolougasolina.view.AdicionarPosto
import com.example.alcoolougasolina.view.ListaPostos
import com.example.alcoolougasolina.view.PostoDetalhe

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.v( "Ciclo de vida","Atividade sendo criada")
        setContent {
            AlcoolOuGasolinaTheme {
                val navController : NavHostController = rememberNavController() // Controlador de navegação entre telas
                NavHost(navController = navController, startDestination = "calculo_combustivel") { // Definimos a primeira tela
                    composable("calculo_combustivel") { TelaPrincipal(navController) } // Tela principal de cálculo de combustível
                    composable("adicionar_posto?postoId={postoId}&nome={nome}&alcool={alcool}&gasolina={gasolina}",
                        arguments = listOf( // Definimos os argumentos da tela de adicionar/editar posto, caso tenha
                            navArgument("postoId") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            }
                        )
                    ) { backStackEntry ->
                        val postoId = backStackEntry.arguments?.getString("postoId")
                        val nome = backStackEntry.arguments?.getString("nome") ?: ""
                        val alcool = backStackEntry.arguments?.getString("alcool") ?: ""
                        val gasolina = backStackEntry.arguments?.getString("gasolina") ?: ""

                        AdicionarPosto(
                            navController = navController,
                            postoId = postoId,
                            nome = nome,
                            alcool = alcool,
                            gasolina = gasolina
                        )
                    }
                    composable("lista_postos") { ListaPostos(navController) } // Tela de lista de postos
                    composable("posto_detalhe/{postoId}", // Tela de detalhes de um posto
                        arguments = listOf(
                            navArgument("postoId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val postoId = backStackEntry.arguments?.getString("postoId")
                        PostoDetalhe(navController = navController, postoId = postoId)
                    }
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        Log.v( "Ciclo de vida","Atividade iniciando")
    }
    override fun onResume() {
        super.onResume()
        Log.v( "Ciclo de vida","Atividade em resumo")
    }
    override fun onPause() {
        super.onPause()
        Log.v( "Ciclo de vida","Atividade sendo pausada")
    }
    override fun onStop() {
        super.onStop()
        Log.v( "Ciclo de vida","Atividade parada")
    }
    override fun onRestart() {
        super.onRestart()
        Log.v( "Ciclo de vida","Atividade reiniciada")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.v( "Ciclo de vida","Atividade destruida")
    }

}
