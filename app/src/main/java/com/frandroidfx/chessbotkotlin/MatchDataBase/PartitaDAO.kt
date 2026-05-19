package com.frandroidfx.chessbotkotlin.MatchDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface PartitaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvaPartita(partita : Partita)

    @Query("SELECT * FROM storico_partite ORDER BY timestamp DESC")
    fun getPartite(): Flow<List<Partita>>
}