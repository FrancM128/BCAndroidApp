package com.frandroidfx.chessbotkotlin.Backend.ChessViewModel

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.MatchManaging.ChessMatchViewModel
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.StateLogic.AppMenuScreen
import com.frandroidfx.chessbotkotlin.Backend.ChessViewModel.StateLogic.ColoreScelto

@Composable
fun ChessAppRouter(viewModel: ChessViewModel){
    val navController = rememberNavController()

    NavHost(navController =  navController , startDestination = "menu"){
        composable("menu"){
            AppMenuScreen(
                onIniziaPartita = { coloreScelto ->
                    navController.navigate("partita/${coloreScelto.name}")
                }
            )
        }
        composable(
            route = "partita/{colore}",
            arguments = listOf(navArgument("colore") {type = NavType.StringType})
        ){backStackEntry ->
            val coloreStr = backStackEntry.arguments?.getString("colore") ?: "CASUALE"
            val coloreScelto = ColoreScelto.valueOf(coloreStr)

            val matchViewModel: ChessMatchViewModel = viewModel()

            SchermataPartita(
                viewModel = matchViewModel,
                coloreIniziale = coloreScelto,
                onTornaAlMenu = {
                    navController.popBackStack()
                }
            )
        }

    }
}


