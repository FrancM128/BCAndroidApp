package com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.MatchManaging

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.StateLogic.ColoreScelto
import com.github.bhlangonijr.chesslib.Side
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.StateLogic.SchermataAttuale
import com.frandroidfx.chessbotkotlin.Backend.NetModules.ChessEngine
import com.frandroidfx.chessbotkotlin.MatchDataBase.Partita
import com.frandroidfx.chessbotkotlin.MatchDataBase.RepositoryPartite
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ChessMatchViewModel(
    application : Application,
    private val repository: RepositoryPartite
    )  : AndroidViewModel(application){
    private val engine = ChessEngine()
    var board = Board( )
        private set

    private val _fenAttuale = MutableStateFlow(board.fen)
    val fenAttuale: StateFlow<String> = _fenAttuale.asStateFlow()
    private val _schermata = MutableStateFlow(SchermataAttuale.MENU_PRINCIPALE)
    val schermata : StateFlow<SchermataAttuale> = _schermata.asStateFlow()

    private val _colorePlayer = MutableStateFlow(ColoreScelto.BIANCO)
    val colorePlayer: StateFlow<ColoreScelto> = _colorePlayer.asStateFlow()

    private val _coloreDefinitivo = MutableStateFlow(Side.WHITE)
    val coloreDefinitivo: StateFlow<Side> = _coloreDefinitivo.asStateFlow()

    private val _staPensando = MutableStateFlow(false)
    val staPensando : StateFlow<Boolean> = _staPensando.asStateFlow()

    private val _depthEngine = MutableStateFlow(3)
    val depthEngine: StateFlow<Int> = _depthEngine.asStateFlow()

    private val _valutazioneAttuale = MutableStateFlow(0f)
    val valutazioneAttuale: StateFlow<Float> = _valutazioneAttuale.asStateFlow()

    private val _cronologiaMosse = MutableStateFlow<List<String>>(emptyList())
    val cronologiaMosse: StateFlow<List<String>> = _cronologiaMosse.asStateFlow()


    val partiteSalvate: kotlinx.coroutines.flow.Flow<List<Partita>> = repository.tutteLePartite


    init{
        viewModelScope.launch{
            engine.inizializza(application.applicationContext)
        }
    }

    fun salvaFenNellaStroia(nuovaFen: String){
        _cronologiaMosse.value =_cronologiaMosse.value + nuovaFen
    }
    fun impostaProfondita(nuovaProfondita : Int){
        if(nuovaProfondita in 3..15){
            _depthEngine.value = nuovaProfondita
        }
    }
    fun getMosseLegaliPerCella(nomeCasella : String?): List<String>{
        if(nomeCasella == null) return emptyList()

        return try{
            val casellaPartenza = Square.fromValue(nomeCasella.uppercase())
            val tutteLeMosse = board.legalMoves()
            tutteLeMosse
                .filter { mossa-> mossa.from == casellaPartenza}
                .map{mossa -> mossa.to.toString().lowercase()}
        }catch(e: IllegalArgumentException){
            emptyList()
        }
    }
    fun avviaNuovaPartita(scelta : ColoreScelto){
        board = Board()
        val coloreDefinitivo = when(scelta) {
            ColoreScelto.BIANCO -> Side.WHITE
            ColoreScelto.NERO -> Side.BLACK
            ColoreScelto.CASUALE -> if(Random.nextBoolean()) Side.WHITE else Side.BLACK
        }
        _coloreDefinitivo.value = coloreDefinitivo
        _schermata.value = SchermataAttuale.IN_PARTITA

        if(coloreDefinitivo == Side.BLACK){
            faiPensareMotore()
        }
    }

    fun avviaAnalisi(partita: Partita){
        board = Board()
        val mosseUci = partita.pgn.split(" ").filter{it.isNotEmpty()}

        val listaFen = mutableListOf(board.fen)
        for(mossa in mosseUci){
            board.doMove(mossa)
            listaFen.add(board.fen)
        }
        _cronologiaMosse.value = listaFen
        _fenAttuale.value = board.fen
        _schermata.value = SchermataAttuale.ANALISI
    }

    fun aggiornaValutazione(fen: String){
        viewModelScope.launch(Dispatchers.Default){
            val score = engine.nnue.evaluate(fen)
            _valutazioneAttuale.value = score
        }
    }

    fun tornaAlMenu() {
        _schermata.value = SchermataAttuale.MENU_PRINCIPALE
    }
    fun giocaMossaPlayer(mossaUci : String){
        if(_staPensando.value || board.sideToMove != _coloreDefinitivo.value) return
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
    fun salvaNelDatabase(risultatoFinale: String){
        val pgnAttuale = board.history.joinToString(" "){it.toString()}
        val coloreStr = if(_coloreDefinitivo.value == Side.WHITE) "BIANCO" else "NERO"
        val tempoattuale = System.currentTimeMillis()

        val nuovaPartita = Partita(
            pgn = pgnAttuale,
            risultato = risultatoFinale,
            coloreGiocato = coloreStr,
            timestamp =  tempoattuale
        )
        viewModelScope.launch(Dispatchers.IO){
            repository.salvaPartita(nuovaPartita)
        }
    }
    fun terminaPartita(){
        salvaNelDatabase(risultatoFinale = "Abb.")
        _schermata.value = SchermataAttuale.FINE_PARTITA
    }
}