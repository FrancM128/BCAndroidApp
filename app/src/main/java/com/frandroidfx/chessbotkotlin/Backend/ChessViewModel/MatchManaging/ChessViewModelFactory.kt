package com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.MatchManaging

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frandroidfx.chessbotkotlin.MatchDataBase.AppDatabase
import com.frandroidfx.chessbotkotlin.MatchDataBase.RepositoryPartite

class ChessMatchViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChessMatchViewModel::class.java)) {
            val database = AppDatabase.getDatabase(application)
            val repository = RepositoryPartite(database.partitaDao)
            @Suppress("UNCHECKED_CAST")
            return ChessMatchViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}