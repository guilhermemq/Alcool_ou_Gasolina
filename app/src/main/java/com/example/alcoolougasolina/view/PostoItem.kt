package com.example.alcoolougasolina.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.alcoolougasolina.R
import com.example.alcoolougasolina.data.Posto

@Composable
fun PostoItemCard(
    posto: Posto,
    onCardClick: () -> Unit,
    onEditarClick: () -> Unit,
    onExcluirClick: () -> Unit
) {
    Card( // Card que contém as informações do posto
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onCardClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text( // Nome do posto
                    text = posto.nome,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier.height(6.dp)
                )
                Text( // Preço do álcool
                    text = stringResource(R.string.preco_do_alcool_formatado, posto.precoAlcool)
                )
                Spacer(
                    modifier = Modifier.height(6.dp)
                )
                Text( // Preço da gasolina
                    text = stringResource(R.string.preco_da_gasolina_formatado, posto.precoGasolina)
                )
            }
            Row {
                IconButton(
                    onClick = onEditarClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFF64B5F6)
                    )
                }
                IconButton(
                    onClick = onExcluirClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir",
                        tint = Color(0xFFE57373)
                    )
                }
            }
        }
    }
}
