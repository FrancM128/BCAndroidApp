package com.frandroidfx.chessbotkotlin

import android.app.Application
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.MatchManaging.ChessMatchViewModel
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.StateLogic.ColoreScelto
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.StateLogic.SchermataAttuale
import com.frandroidfx.chessbotkotlin.MatchDataBase.Partita
import com.frandroidfx.chessbotkotlin.MatchDataBase.RepositoryPartite
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import com.github.bhlangonijr.chesslib.Side
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ChessMatchViewModelTest {
    private lateinit var viewModel: ChessMatchViewModel
    private val repository: RepositoryPartite = mockk(relaxed = true)
    private val application : Application = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup (){
        Dispatchers.setMain(testDispatcher)
        viewModel = ChessMatchViewModel(application, repository)
    }
    @Test
    fun controlloStart(){
        assertEquals(SchermataAttuale.MENU_PRINCIPALE,viewModel.schermata.value)
    }
    @Test
    fun nuovaPartitaTest(){
        viewModel.avviaNuovaPartita(ColoreScelto.BIANCO)
        assertEquals(SchermataAttuale.IN_PARTITA,viewModel.schermata.value)
        assertEquals(Side.WHITE,viewModel.coloreDefinitivo.value)
    }
    @Test
    fun avviaAnalisiTest(){
        val partitaMock = Partita(pgn = "e2e4 e7e5", risultato = "1-0", coloreGiocato = "BIANCO", timestamp = System.currentTimeMillis())
        viewModel.avviaAnalisi(partitaMock)
        assertEquals(SchermataAttuale.ANALISI,viewModel.schermata.value)
        assertEquals(3,viewModel.cronologiaMosse.value.size)
    }

    @Test
    fun tornaAlMenuTest(){
        viewModel.avviaNuovaPartita(ColoreScelto.NERO)
        viewModel.tornaAlMenu()
        assertEquals(SchermataAttuale.MENU_PRINCIPALE,viewModel.schermata.value)
    }
}