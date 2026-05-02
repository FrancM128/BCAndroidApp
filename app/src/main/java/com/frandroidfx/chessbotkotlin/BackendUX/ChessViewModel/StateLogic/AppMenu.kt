package com.frandroidfx.chessbotkotlin.BackendUX.StateLogic

import com.frandroidfx.chessbotkotlin.FrontendUI.ColoreScelto

sealed interface AppMenu {
    data object MainMenu: AppMenu
    data class CurrentGame(
        val coloreGiocatore: ColoreScelto,
        val depthCPU: Int = 3
    ) : AppMenu

    data class RichiestaSalvataggio(val datiPartita: DatiPartita) : AppMenu
    data object ArchivioPartite : AppMenu
    data class AnalisiPartita(val partitaSalvata: DatiPartita) : AppMenu
}

