package com.frandroidfx.chessbotkotlin.MatchDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Partita::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun partitaDao() : PartitaDAO
    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context : Context) : AppDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chess_bot_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}