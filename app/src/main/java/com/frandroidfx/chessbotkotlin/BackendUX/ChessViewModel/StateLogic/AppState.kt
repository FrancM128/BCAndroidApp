package com.frandroidfx.chessbotkotlin.BackendUX.ChessViewModel.StateLogic

enum class ColoreScelto {BIANCO , NERO}

sealed interface  AppState {
    data object Setup: AppState
    data class InGioco(val coloreGiocatore : ColoreScelto) : AppState
}