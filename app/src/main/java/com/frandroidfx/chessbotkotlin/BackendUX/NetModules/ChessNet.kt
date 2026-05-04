package com.frandroidfx.chessbotkotlin.BackendUX.NetModules
import org.pytorch.executorch.Module
import org.pytorch.executorch.Tensor
import org.pytorch.executorch.EValue

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Square
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChessNet {
    /*alla inizializzazione va caricato,
    */
    private var module: Module? = null

    fun fenToTensor(fen: String): Tensor {
        val board = Board()
        board.loadFromFen(fen)

        val indices = mutableListOf<Long>()

        val pieceMap = mapOf(Piece.WHITE_PAWN to 0L, Piece.WHITE_KNIGHT to 1L,
                            Piece.WHITE_BISHOP to 2L , Piece.WHITE_ROOK to 3L ,
                            Piece.WHITE_QUEEN to 4L , Piece.WHITE_KING to 5L,

                            Piece.BLACK_PAWN to 6L, Piece.BLACK_KNIGHT to 7L,
                            Piece.BLACK_BISHOP to 8L , Piece.BLACK_ROOK to 9L ,
                            Piece.BLACK_QUEEN to 10L, Piece.BLACK_KING to 11L)

        for (i in 0 until 64){
            val square = Square.squareAt(i)
            val piece = board.getPiece(square)

            if(piece != Piece.NONE){
                val pieceIndex = pieceMap[piece]!!
                val idx = pieceIndex * 64 + i
                indices.add(idx)
            }
        }
        while(indices.size < 32){
            indices.add(768L)
        }

        val longArray = indices.toLongArray()
        val shape = longArrayOf(1,32)
        return Tensor.fromBlob(longArray , shape)
    }
    fun loadModel(modelPath: String){
        require(modelPath.isNotBlank()){"Controllo percorso"}
        require(modelPath.endsWith(".pte")) {"Controllo formato"}
        module = Module.load(modelPath)
    }

    fun tryModel(inputTensor : Tensor) : Float{
        val currentModule = module ?: throw IllegalStateException("Exception : Rete non caricata")

        val inputEValue = EValue.from(inputTensor)
        val outputEValues : Array<EValue> = currentModule.forward(inputEValue)
        val outputTensor = outputEValues[0].toTensor()

        return outputTensor.dataAsFloatArray[0]
    }


    fun evaluate(inputFen : String): Float{
        return tryModel(fenToTensor(inputFen))
    }
}
