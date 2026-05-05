package com.frandroidfx.chessbotkotlin.BackendUX.ChessViewModel


import androidx.lifecycle.ViewModel
import com.frandroidfx.chessbotkotlin.BackendUX.ChessViewModel.StateLogic.AppMenu
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChessViewModel: ViewModel() {
    private val _uiState = MutableStateFlow<AppMenu>(AppMenu.MainMenu)
    val uiState: StateFlow<AppMenu> = _uiState.asStateFlow()

    fun impostaStato(nuovoStato: AppMenu){
        _uiState.value = nuovoStato
    }

}