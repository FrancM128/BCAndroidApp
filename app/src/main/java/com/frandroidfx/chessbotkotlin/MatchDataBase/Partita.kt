package com.frandroidfx.chessbotkotlin.MatchDataBase

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "storico_partite")
data class Partita(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pgn:String,
    val risultato: String,
    val coloreGiocato: String,
    val timestamp: Long,//Secondi
)
