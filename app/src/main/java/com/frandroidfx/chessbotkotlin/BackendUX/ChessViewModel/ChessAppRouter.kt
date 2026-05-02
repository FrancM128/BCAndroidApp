package com.frandroidfx.chessbotkotlin.FrontendUI

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.frandroidfx.chessbotkotlin.BackendUX.ChessViewModel.ChessViewModel
import com.frandroidfx.chessbotkotlin.BackendUX.ChessViewModel.StateLogic.AppMenu

@Composable
fun ChessAppRouter(viewModel: ChessViewModel){
    val statoCorrente by viewModel.uiState.collectAsState()

    when(val stato = statoCorrente){
        
        is AppMenu.MainMenu -> {
            // TODO: Implementare UI Menu
        }
        is AppMenu.CurrentGame -> {
            // TODO: Implementare UI Scacchiera
        }
        is AppMenu.RichiestaSalvataggio -> {
            // TODO: Implementare UI Salvataggio
        }
        is AppMenu.ArchivioPartite -> {
            // TODO: Implementare UI Lista Archivio
        }
        is AppMenu.AnalisiPartita -> {
            // TODO: Implementare UI Analisi
        }
    }
}


