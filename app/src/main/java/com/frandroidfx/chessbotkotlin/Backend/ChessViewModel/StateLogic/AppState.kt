package com.frandroidfx.chessbotkotlin.BackendUX.ChessViewModel.StateLogic


enum class SchermataAttuale{ MENU_PRINCIPALE,IN_PARTITA,FINE_PARTITA}
enum class RisultatoPartita { IN_CORSO,VITTORIA_PLAYER,VITTORIA_CPU,PATTA}
enum class ColoreScelto {BIANCO , NERO , CASUALE}


sealed interface  AppState {
    data object Setup: AppState
    data class InGioco(val coloreGiocatore : ColoreScelto) : AppState
}