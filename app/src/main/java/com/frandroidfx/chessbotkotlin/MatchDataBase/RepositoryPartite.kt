package com.frandroidfx.chessbotkotlin.MatchDataBase

import kotlinx.coroutines.flow.Flow

class RepositoryPartite(private val matches : PartitaDAO) {
    val tutteLePartite: Flow<List<Partita>> = matches.getPartite()

    suspend fun salvaPartita(partita : Partita){
        matches.salvaPartita(partita)
    }

}