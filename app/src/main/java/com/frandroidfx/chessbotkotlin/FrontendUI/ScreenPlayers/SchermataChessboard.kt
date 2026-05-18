package com.frandroidfx.chessbotkotlin.FrontendUI.ScreenPlayers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.MatchManaging.ChessMatchViewModel
import com.frandroidfx.chessbotkotlin.FrontendUI.Chessboard
import com.frandroidfx.chessbotkotlin.FrontendUI.FenToMap

@Composable
fun SchermataPartita(viewModel: ChessMatchViewModel){
    var selezione by remember{ mutableStateOf<String?>(null)}
    Chessboard(
        pezzi = FenToMap(viewModel.fenAttuale.collectAsState().value),
        casellaSelezionata = selezione,
        mossePossibili = viewModel.getMosseLegaliPerCella(selezione),
        onCasellaCliccata = {quadratoCliccato ->
            val mosseLegaliPezzo = viewModel.getMosseLegaliPerCella(selezione)
            if(selezione == null){
                selezione = quadratoCliccato
            }else if (mosseLegaliPezzo.contains(quadratoCliccato)){
                val mossaUci = selezione!! + quadratoCliccato
                viewModel.giocaMossaPlayer(mossaUci)
                selezione = null
            }
            else {
                selezione = quadratoCliccato
            }
        }
    )
}
/*
@Composable
fun SchermataAnalisi(viewModel : AnalysisViewModel)
* */
