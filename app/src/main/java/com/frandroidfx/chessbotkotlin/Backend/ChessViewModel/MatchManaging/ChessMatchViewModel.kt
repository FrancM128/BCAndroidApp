package com.frandroidfx.chessbotkotlin.BackendUX.ChessViewModel.MatchManaging

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.frandroidfx.chessbotkotlin.BackendUX.ChessViewModel.StateLogic.ColoreScelto
import com.github.bhlangonijr.chesslib.Side
import com.frandroidfx.chessbotkotlin.BackendUX.ChessViewModel.StateLogic.SchermataAttuale
import com.frandroidfx.chessbotkotlin.BackendUX.NetModules.ChessEngine
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.move.Move
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ChessMatchViewModel(application : Application)  : AndroidViewModel(application){
    private val engine = ChessEngine()
    var board = Board( )
        private set
    private val _schermata = MutableStateFlow(SchermataAttuale.MENU_PRINCIPALE)
    val schermata : StateFlow<SchermataAttuale> = _schermata.asStateFlow()

    private val _colorePlayer = MutableStateFlow(Side.WHITE)
    val colorePlayer: StateFlow<Side> = _colorePlayer.asStateFlow()

    private val _staPensando = MutableStateFlow(false)
    val staPensando : StateFlow<Boolean> = _staPensando.asStateFlow()

    private val _depthEngine = MutableStateFlow(3)
    val depthEngine: StateFlow<Int> = _depthEngine.asStateFlow()

    init{
        viewModelScope.launch{
            engine.inizializza(application.applicationContext)
        }
    }

    fun impostaProfondita(nuovaProfondita : Int){
        if(nuovaProfondita in 3..15){
            _depthEngine.value = nuovaProfondita
        }
    }

    fun avviaNuovaPartita(scelta : ColoreScelto){
        board = Board()
        val coloreDefinitivo = when(scelta) {
            ColoreScelto.BIANCO -> Side.WHITE
            ColoreScelto.NERO -> Side.BLACK
            ColoreScelto.CASUALE -> if(Random.nextBoolean()) Side.WHITE else Side.BLACK
        }
        _colorePlayer.value = coloreDefinitivo
        _schermata.value = SchermataAttuale.IN_PARTITA

        if(coloreDefinitivo == Side.BLACK){
            faiPensareMotore()
        }
    }

    fun giocaMossaPlayer(mossaUci : String){
        if(_staPensando.value || board.sideToMove != _colorePlayer.value) return
        val mossa = Move(mossaUci , board.sideToMove)
        if(board.isMoveLegal(mossa,true)){
            board.doMove(mossa)
            if(board.isMated || board.isDraw){
                _schermata.value = SchermataAttuale.FINE_PARTITA
                return
            }
            faiPensareMotore()
        }
    }

    private fun faiPensareMotore(){
        _staPensando.value = true
        viewModelScope.launch(Dispatchers.Default){
            val currentDepth = _depthEngine.value
            val mossaMotore = engine.scegliMossaMigliore(board,depth = currentDepth)

            withContext(Dispatchers.Main){
                if(mossaMotore != null){
                    board.doMove(mossaMotore)
                    if(board.isMated || board.isDraw){
                        _schermata.value = SchermataAttuale.FINE_PARTITA
                    }
                }else {
                    _schermata.value = SchermataAttuale.FINE_PARTITA
                }
                _staPensando.value = false
            }
        }
    }
}