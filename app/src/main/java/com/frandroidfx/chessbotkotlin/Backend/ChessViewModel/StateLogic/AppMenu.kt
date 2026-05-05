package com.frandroidfx.chessbotkotlin.BackendUX.ChessViewModel.StateLogic

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppMenuScreen(onIniziaPartita: (ColoreScelto) -> Unit){
    var coloreSelezionato by remember {mutableStateOf(ColoreScelto.BIANCO)}
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text (
            text = "FrChess",
            fontSize = 32.sp,
            fontWeight = FontWeight.Light,
            modifier= Modifier.padding(bottom = 48.dp)
        )
        Text("Nuova Partita",fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            PulsanteSelezione(
                testo = "Bianco" ,
                selezionato = coloreSelezionato == ColoreScelto.BIANCO,
                onClick = {coloreSelezionato = ColoreScelto.BIANCO }
            )
            PulsanteSelezione(
                testo = "Nero" ,
                selezionato = coloreSelezionato == ColoreScelto.NERO,
                onClick = {coloreSelezionato = ColoreScelto.NERO }
            )
            PulsanteSelezione(
                testo = "CASUALE" ,
                selezionato = coloreSelezionato == ColoreScelto.CASUALE,
                onClick = {coloreSelezionato = ColoreScelto.CASUALE }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { onIniziaPartita(coloreSelezionato)},
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp)
        ){
            Text("NUOVA PARTITA" , fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth(0.6f)) {
            Text("Storico Match (wip)")
        }
    }
}

@Composable
fun PulsanteSelezione(testo : String, selezionato: Boolean , onClick : () -> Unit) {
    if (selezionato) {
        Button(onClick = onClick) {
            Text(testo)
        }
    } else {
        OutlinedButton(onClick = onClick) {
            Text(testo)
        }
    }
}