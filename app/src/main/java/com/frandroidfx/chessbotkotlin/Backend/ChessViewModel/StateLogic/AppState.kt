package com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.StateLogic


enum class SchermataAttuale{ MENU_PRINCIPALE,IN_PARTITA,FINE_PARTITA,ANALISI}
enum class RisultatoPartita { IN_CORSO,VITTORIA_PLAYER,VITTORIA_CPU,PATTA}
enum class ColoreScelto {BIANCO , NERO , CASUALE}


sealed interface  AppState {
    data object Setup: AppState
    data class InGioco(val coloreGiocatore : ColoreScelto) : AppState
}