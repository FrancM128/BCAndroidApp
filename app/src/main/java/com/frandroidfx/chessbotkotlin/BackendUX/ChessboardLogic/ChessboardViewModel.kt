package com.frandroidfx.chessbotkotlin.BackendUX.ChessboardLogic

import androidx.lifecycle.ViewModel
import com.frandroidfx.chessbotkotlin.BackendUX.ChessViewModel.StateLogic.AppMenu
import com.frandroidfx.chessbotkotlin.BackendUX.NetModules.ChessEngine
import com.frandroidfx.chessbotkotlin.BackendUX.NetModules.ChessNet

class ChessboardViewModel : ViewModel() {
    private var casellaOrigine: String? = null
    private var caselleDestinazioneLegali: List<String> = emptyList()

    fun toccoUtente(casellaToccata: String, statoApp: AppMenu, engine: ChessEngine){

    }
}