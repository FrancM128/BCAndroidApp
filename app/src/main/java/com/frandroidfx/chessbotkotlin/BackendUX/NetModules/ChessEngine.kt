package com.frandroidfx.chessbotkotlin.BackendUX.NetModules
import android.content.Context
import com.facebook.soloader.SoLoader
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.PieceType
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


class ChessEngine {
    private var nnue: ChessNet = ChessNet()
    private var usaLibro: Boolean = true
    private var libroAperture: PolyglotBook? = null
    var depth: Int = 3
    //inizializzazione prima del match
    suspend fun inizializza(context: Context){
        withContext(Dispatchers.IO){
            try{
                println("accensione motore...")
                //aperture
                println("Caricamento aperture...")
                val bookBytes = context.assets.open("gm2001.bin").use {it.readBytes()}
                libroAperture = PolyglotBook(bookBytes)
                usaLibro = true
                //rete neurale
                SoLoader.init(context,false)
                val nomeModello = "nnue10M100E.pte"
                val fileModello = File(context.filesDir, nomeModello)

                if(!fileModello.exists()){
                    println("Estrazione rete neurale")
                    context.assets.open(nomeModello).use {
                        inputStream -> FileOutputStream(fileModello).use { outputStream ->
                            inputStream.copyTo(outputStream)
                    }
                    }
                }
                nnue.loadModel(fileModello.absolutePath)
                print("Rete neurale pronta")
            }catch (e : Exception){
                println("errore nel caricamento assets")
                e.printStackTrace()
                usaLibro = false
            }
        }
    }


    //ordinamento mosse legali
    //Prescoring: Scacchi - Most Valuable Victim - Least Valuable Attacker
    fun getValorePezzo(tipoPezzo : PieceType): Int{
        return when (tipoPezzo){
            PieceType.QUEEN->900
            PieceType.ROOK->500
            PieceType.BISHOP->320
            PieceType.KNIGHT->300
            PieceType.PAWN->100
            else -> 0
        }
    }

    fun getDistanzaCentro(square: Square) : Int{
        val fileDist = intArrayOf(7,5,3,1,1,3,5,7)[square.file.ordinal]
        val rankDist = intArrayOf(7,5,3,1,1,3,5,7)[square.rank.ordinal]
        return fileDist+rankDist
    }

    private fun calcolaPunteggioPriorita(mossa: Move, board : Board) : Int{
        var score = 0
        val attaccante = board.getPiece(mossa.from).pieceType
        var vittima = board.getPiece(mossa.to).pieceType

        if (mossa.to == board.enPassantTarget && attaccante == PieceType.PAWN){
            vittima = PieceType.PAWN
        }

        board.doMove(mossa)
        val isScacco = board.isKingAttacked
        board.undoMove()
        if(isScacco){
            score+=20000
        }

        if (vittima != PieceType.NONE){
            val valVittima = getValorePezzo(vittima)
            val valAttaccante = getValorePezzo(attaccante)

            score += 10000 + (valVittima * 10)- valAttaccante
        }

        if(!isScacco && vittima == PieceType.NONE){
            score+= (14-getDistanzaCentro(mossa.to))
        }
        return score
    }
    fun ordinaMosse(board: Board): List<Move>{
        val mosseLegali = board.legalMoves()
        val punteggiMosse = mosseLegali.associateWith{mossa-> calcolaPunteggioPriorita(mossa,board)}
        return mosseLegali.sortedByDescending { mossa->punteggiMosse[mossa]?: 0 }
    }

    //ricerca e mossa migliore
    fun negamax(board: Board,
                alpha: Float = Float.NEGATIVE_INFINITY, beta: Float = Float.POSITIVE_INFINITY,
                depth: Int = 3): Pair<Move? , Float> {
        if (board.isMated() ) { return Pair(null, Float.NEGATIVE_INFINITY + depth)}
        if (board.isDraw || board.isStaleMate){return Pair(null,0.0f)}

        var max_score: Float = Float.NEGATIVE_INFINITY
        var localAlpha : Float = alpha

        if (depth == 0){
            max_score = nnue.evaluate(board.getFen())
            var molt = if(board.sideToMove == Side.WHITE) 1 else -1
            return Pair(null,molt * max_score)
        }

        var best_move: Move? = null
        val mosseOrdinate : List<Move> =  ordinaMosse(board)

        for (mossa in mosseOrdinate){
            board.doMove(mossa)
            var (_ , score) = negamax(board,-beta,-localAlpha,depth-1)
            score = -score
            board.undoMove()
            if (score > max_score){
                max_score = score
                best_move = mossa
            }
            localAlpha = maxOf(localAlpha , max_score)
            if (localAlpha >= beta) break

        }

        return Pair(best_move,max_score)
    }

    fun scegliMossaMigliore(board : Board,depth: Int = 3): Move?{
        var mossaUci = libroAperture!!.getMossaMigliore(board.zobristKey)

        if(usaLibro && mossaUci != null ){
            var mossa = libroAperture!!.convertiUciInMove(board , mossaUci)
            println("mossa trovata  nel libro")
            return mossa
        }else{
            usaLibro = false
            var p : Pair <Move? , Float> = negamax(board,depth = depth)
            return p.first
        }
    }

}

