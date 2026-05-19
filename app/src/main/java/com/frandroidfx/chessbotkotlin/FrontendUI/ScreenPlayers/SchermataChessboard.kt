package com.frandroidfx.chessbotkotlin.FrontendUI.ScreenPlayers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.MatchManaging.ChessMatchViewModel
import com.frandroidfx.chessbotkotlin.FrontendUI.Chessboard
import com.frandroidfx.chessbotkotlin.FrontendUI.FenToMap
import com.github.bhlangonijr.chesslib.Side


@Composable
fun SchermataPartita(viewModel: ChessMatchViewModel) {
    val fenAttuale by viewModel.fenAttuale.collectAsState()
    val cronologiaMosse by viewModel.cronologiaMosse.collectAsState()
    val schermata by viewModel.schermata.collectAsState()
    val colorePlayer by viewModel.colorePlayer.collectAsState()

    var selezione by remember { mutableStateOf<String?>(null) }
    var scacchieraRoutata by remember { mutableStateOf(false) }
    var indiceVisualizzato by remember { mutableStateOf(-1) }

    val isGuardandoPassato = indiceVisualizzato != -1
    val fenDaMostrare = if (isGuardandoPassato && cronologiaMosse.isNotEmpty()) {
        cronologiaMosse.getOrNull(indiceVisualizzato) ?: fenAttuale
    } else fenAttuale

    val playerIsWhite = (colorePlayer.name == "WHITE")
    val giocaComeBianco = if (scacchieraRoutata) !playerIsWhite else playerIsWhite

    val testoIntestazione =
        if (isGuardandoPassato) "Mossa: ${indiceVisualizzato + 1}" else "In corso..."
    val coloreIntestazione = if (isGuardandoPassato) Color.Green else Color.Gray



    LayoutPartitaBase(
        fenDaMostrare = fenDaMostrare,
        giocaComeBianco = giocaComeBianco,
        casellaSelezionata = if(isGuardandoPassato) null else selezione,
        mossePossibili = if(isGuardandoPassato) emptyList() else viewModel.getMosseLegaliPerCella(selezione),
        testoIntestazione = testoIntestazione,
        coloreIntestazione = coloreIntestazione,
        isPartitaInCorso = (schermata.name =="IN_GIOCO"),
        isPartitaFinita = (schermata.name == "FINE_PARTITA"),
        onCasellaCliccata ={casellaCliccata->
                           gestisciMossaUtente(
                               casellaCliccata = casellaCliccata,
                               selezioneAttuale = selezione,
                               mosseLegali = viewModel.getMosseLegaliPerCella(selezione),
                               isGuardandoPassato = isGuardandoPassato,
                               mappaPezzi = FenToMap(fenDaMostrare),
                               onAggiornaSelezione = {nuovaSelezione -> selezione = nuovaSelezione},
                               onEseguiMossa = {mossaUci -> viewModel.giocaMossaPlayer(mossaUci)}
                           )
        },
        onNavInizio = { indiceVisualizzato = 0},
        onNavIndietro = {
            if (indiceVisualizzato == -1 && cronologiaMosse.isNotEmpty()) indiceVisualizzato = cronologiaMosse.size - 2
            else if (indiceVisualizzato > 0) indiceVisualizzato--},
        onNavAvanti = {
            if(indiceVisualizzato != -1){
                if(indiceVisualizzato < cronologiaMosse.size - 1) indiceVisualizzato++
                else indiceVisualizzato = -1
            }
                      },
        onNavUltima = { indiceVisualizzato = -1 },
        onRuotaCliccato = { scacchieraRoutata = ! scacchieraRoutata },
        onAbbandonaCliccato = { viewModel.terminaPartita() },
        onTornaMenuCliccato= { /* TODO */})
    }




@Composable
fun LayoutPartitaBase (
    fenDaMostrare : String,
    giocaComeBianco: Boolean,
    casellaSelezionata : String? ,
    mossePossibili : List <String>,

    testoIntestazione: String,
    coloreIntestazione: Color,
    isPartitaInCorso: Boolean,
    isPartitaFinita : Boolean,

    onCasellaCliccata: (String) -> Unit,
    onNavInizio: ()-> Unit,
    onNavIndietro: ()-> Unit,
    onNavAvanti: ()-> Unit,
    onNavUltima: ()-> Unit,
    onRuotaCliccato: ()-> Unit,
    onAbbandonaCliccato: ()-> Unit,
    onTornaMenuCliccato: ()-> Unit,
)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    )
    {
        Text(
            text = testoIntestazione,
            fontWeight =  FontWeight.Normal,
            fontSize = 20.sp,
            color = coloreIntestazione
        )

        Chessboard(
            pezzi = FenToMap(fenDaMostrare),
            giocaComeBianco = giocaComeBianco,
            casellaSelezionata = casellaSelezionata,
            mossePossibili = mossePossibili,
            onCasellaCliccata = onCasellaCliccata
        )

        Row(
          modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceEvenly

        ){
            IconButton(onClick = onNavInizio) { Text("<<", fontWeight = FontWeight.Bold) }
            IconButton(onClick = onNavIndietro) { Text("<", fontWeight = FontWeight.Bold) }
            IconButton(onClick = onNavAvanti) { Text(">", fontWeight = FontWeight.Bold) }
            IconButton(onClick = onNavUltima) { Text(">>", fontWeight = FontWeight.Bold) }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ){
            if(isPartitaInCorso){
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)){
                    Button(onClick = onRuotaCliccato){Text("@")}
                    Button(
                        onClick = onAbbandonaCliccato,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red))
                    {Text("|[]",color = Color.White)}
                }

            }   else if (isPartitaFinita){Button(onClick = onTornaMenuCliccato){Text("Menu")} }
        }
    }
}

private fun gestisciMossaUtente(
    casellaCliccata: String,
    selezioneAttuale: String?,
    mosseLegali : List<String>,
    isGuardandoPassato: Boolean,
    mappaPezzi: Array<String>,
    onAggiornaSelezione: (String?)->Unit,
    onEseguiMossa : (String)->Unit
){
    if (isGuardandoPassato) return
    if (selezioneAttuale == null){ onAggiornaSelezione(casellaCliccata)}
    else if(mosseLegali.contains(casellaCliccata)){
        var mossaUci = selezioneAttuale + casellaCliccata
        val colPartenza = selezioneAttuale[0]-'a'
        val rigaPartenza = 8-selezioneAttuale[1].digitToInt()

        val pezzoMosso = mappaPezzi[rigaPartenza][colPartenza]
        val traversaArrivo = casellaCliccata[1]
        //ipotizzata promozione a Regina sottointesa, ALLERT: la rete non ne è coscente
        if(pezzoMosso == 'P' && traversaArrivo == '8'){mossaUci += "q"}
        else if(pezzoMosso == 'p' && traversaArrivo == '1'){mossaUci += "q"}
        onEseguiMossa(mossaUci)
        onAggiornaSelezione(null)
    }
    else{onAggiornaSelezione(casellaCliccata)}
}