package com.frandroidfx.chessbotkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.setContent
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.MatchManaging.ChessMatchViewModel
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.StateLogic.SchermataAttuale
import com.frandroidfx.chessbotkotlin.FrontendUI.ScreenPlayers.SchermataMenu
import com.frandroidfx.chessbotkotlin.FrontendUI.ScreenPlayers.SchermataPartita
import com.frandroidfx.chessbotkotlin.MatchDataBase.AppDatabase
import com.frandroidfx.chessbotkotlin.MatchDataBase.RepositoryPartite
import com.frandroidfx.chessbotkotlin.ui.theme.MyApplicationTheme
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.StateLogic.ColoreScelto

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = RepositoryPartite(db.partitaDao())

        val viewModelFactory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass : Class<T>) : T{
                return ChessMatchViewModel(application, repository) as T
            }
        }


        setContent{
            val viewModel : ChessMatchViewModel = viewModel(factory = viewModelFactory)
            MyApplicationTheme{
                val schermata by viewModel.schermata.collectAsState()
                Surface (modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background){
                    when(schermata){
                        SchermataAttuale.MENU_PRINCIPALE -> {
                            SchermataMenu(
                                viewModel = viewModel,
                                onNuovaPartita = {colore -> viewModel.avviaNuovaPartita(colore)},
                                onAnalisiPartita = {partita -> viewModel.avviaAnalisi(partita)}
                            )
                        }
                        else -> {
                            SchermataPartita(viewModel = viewModel,
                                onTornaAlMenu = {viewModel.tornaAlMenu()})
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}