package com.frandroidfx.chessbotkotlin.FrontendUI

enum class ColoreScelto {BIANCO , NERO}

sealed interface  AppState {
    data object Setup: AppState
    data class InGioco(val coloreGiocatore : ColoreScelto) : AppState
}