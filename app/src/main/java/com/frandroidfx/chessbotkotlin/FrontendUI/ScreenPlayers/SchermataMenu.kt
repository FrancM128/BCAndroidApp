package com.frandroidfx.chessbotkotlin.FrontendUI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.MatchManaging.ChessMatchViewModel
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.StateLogic.ColoreScelto
import com.frandroidfx.chessbotkotlin.MatchDataBase.Partita
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SchermataMenu(
    viewModel: ChessMatchViewModel,
    onNuovaPartita: (ColoreScelto) -> Unit,
    onAnalisiPartita : (Partita)-> Unit,
){
    val partite by viewModel.partiteSalvate.collectAsState(initial = emptyList())
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.getDefault())

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(16.dp)){
        Text("Chess Bot",style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 16.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),horizontalArrangement = Arrangement.SpaceBetween){

            Button(onClick = {onNuovaPartita(ColoreScelto.BIANCO)}, modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)){
                Text("Nuova Partita Bianco")
            }
            Button(onClick = {onNuovaPartita(ColoreScelto.NERO)}, modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)){
                Text("Nuova Partita Nero")
            }
            Button(onClick = {onNuovaPartita(ColoreScelto.CASUALE)}, modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)){
                Text("Nuova Partita Casuale")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Storico Partite",style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))
        LazyColumn(modifier = Modifier.weight(1f)){
            items(partite){
                partita ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onAnalisiPartita(partita) }
                ){
                    Row(modifier = Modifier.padding(16.dp),horizontalArrangement = Arrangement.SpaceBetween){
                        Column{
                            Text("Risultato: ${partita.risultato}", fontSize = 16.sp)
                            Text("Data : ${dateFormat.format(Date(partita.timestamp))}")
                        }
                        Text(partita.coloreGiocato,color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
