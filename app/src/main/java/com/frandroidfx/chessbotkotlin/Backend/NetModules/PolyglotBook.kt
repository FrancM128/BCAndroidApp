package com.frandroidfx.chessbotkotlin.BackendUX.NetModules

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PolyglotBook(bytes : ByteArray){
    private val bookMoves = mutableMapOf<Long,MutableList<Pair<String,Int>>>()


    init{
        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN)//codifica BIG ENDIAN

        while(buffer.hasRemaining()){
            val key = buffer.long
            val moveBinary = buffer.short.toInt() and 0xFFFF
            val weight = buffer.short.toInt() and 0xFFFF
            val learn = buffer.int

            if(weight > 0 ){
                val mossaUci = decodificaMossaPolyglot(moveBinary)
                bookMoves.getOrPut((key)){ mutableListOf()}.add(mossaUci to weight)
            }

        }

        bookMoves.values.forEach{lista->
            lista.sortByDescending { it.second }
        }
    }

    fun getMossaMigliore(zobristKey : Long): String? {
        val mossePossibili = bookMoves[zobristKey]
        return mossePossibili?.random()?.first
    }

    private fun decodificaMossaPolyglot(move : Int) : String{
        val toFile = move and 7
        val toRank = (move ushr 3) and 7
        val fromFile = (move ushr 6) and 7
        val fromRank = (move ushr 9) and 7
        val promotion = (move ushr 12) and 7

        val origine = "${('a' + fromFile)}${('1' + fromRank)}"
        val destinazione = "${('a' + toFile)}${('1' + toRank)}"
        val pezzoPromozione = when (promotion) {
            1 -> "n"
            2 -> "b"
            3 -> "r"
            4 -> "q"
            else -> ""
        }

        return "$origine$destinazione$pezzoPromozione"
    }

    fun convertiUciInMove(board: Board, mossaUci: String): Move {
        val partenza = Square.valueOf(mossaUci.substring(0, 2).uppercase())
        val arrivo = Square.valueOf(mossaUci.substring(2, 4).uppercase())


        if (mossaUci.length == 5) {
            val letteraPromozione = mossaUci[4].lowercaseChar()
            val pezzoPromosso = when (letteraPromozione) {
                'q' -> if (board.sideToMove == Side.WHITE) Piece.WHITE_QUEEN else Piece.BLACK_QUEEN
                'r' -> if (board.sideToMove == Side.WHITE) Piece.WHITE_ROOK else Piece.BLACK_ROOK
                'b' -> if (board.sideToMove == Side.WHITE) Piece.WHITE_BISHOP else Piece.BLACK_BISHOP
                'n' -> if (board.sideToMove == Side.WHITE) Piece.WHITE_KNIGHT else Piece.BLACK_KNIGHT
                else -> Piece.NONE
            }
            return Move(partenza, arrivo, pezzoPromosso)
        }

        return Move(partenza, arrivo)
    }
}

