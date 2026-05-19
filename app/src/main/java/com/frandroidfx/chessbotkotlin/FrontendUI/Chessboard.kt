package com.frandroidfx.chessbotkotlin.FrontendUI

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp



@Composable
fun Chessboard(
    pezzi: Array<String>,
    giocaComeBianco: Boolean = true,
    casellaSelezionata: String? = null,
    mossePossibili: List<String> = emptyList(),
    caselleEvidenziate: List<String> = emptyList(),

    onCasellaCliccata: (String) ->Unit

){
    val lightSquare = Color(0xFF00D9B5)
    val darkSquare = Color(0xFFB58863)
    val highlightSelection = Color(0x80FFF033)
    val highlightLastMove = Color(0x6000FF00)
    val dotColor = Color(0x80000000)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(2.dp , Color.DarkGray)
    ){
        var iterRighe = if(giocaComeBianco) 0..7 else 7 downTo 0
        var iterColonne = if(giocaComeBianco)0..7 else 7 downTo 0
        for (row in iterRighe){
            Row(modifier = Modifier.weight(1f)){
                for(col in iterColonne){
                    val isLight = (row + col) % 2 == 0
                    val baseSquareColor = if (isLight) lightSquare else darkSquare

                    val nomeFila = ('a' + col).toString()
                    val nomeTraversa = (8 - row).toString()
                    val nomeCasella = "$nomeFila$nomeTraversa"

                    val isSelezionata = nomeCasella == casellaSelezionata
                    val isEvidenziata = caselleEvidenziate.contains(nomeCasella)
                    val isMossaPossibile = mossePossibili.contains(nomeCasella)
                    val pezzoCorrente: Char = pezzi[row][col]

                    val coloreSfondoAttuale = when{
                        isSelezionata ->highlightSelection
                        isEvidenziata ->highlightLastMove
                        else ->baseSquareColor
                    }

                    Box(modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(coloreSfondoAttuale)
                            .clickable { onCasellaCliccata(nomeCasella) },
                        contentAlignment = Alignment.Center
                    ){
                        if(col == (if(giocaComeBianco) 0 else 7)){
                            Text(
                                text = nomeTraversa,
                                color = if(isLight) darkSquare else lightSquare,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.TopStart).padding(2.dp)
                            )
                        }
                        if(row == (if (giocaComeBianco) 7 else 0)){
                            Text(
                                text = nomeFila,
                                color = if(isLight) darkSquare else lightSquare,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.BottomEnd).padding(2.dp)
                            )
                        }
                        if(pezzoCorrente != ' '){
                            DisegnaPezzo(pezzoCorrente)
                        }
                        if(isMossaPossibile){
                            Box(modifier = Modifier.fillMaxSize(0.3f).background(dotColor, shape  = CircleShape))
                        }
                    }
                }
            }
        }
    }

}
@Composable
fun DisegnaPezzo(pezzoFen: Char) {
    //placeholder per immagini appena pronte
    val drawableId: Int? = when (pezzoFen) {
        'P' -> 0 // R.drawable.ic_wp
        'N' -> 0
        'B' -> 0
        'R' -> 0
        'Q' -> 0
        'K' -> 0
        'p' -> 0 // R.drawable.ic_bp
        'n' -> 0
        'b' -> 0
        'r' -> 0
        'q' -> 0
        'k' -> 0
        else -> null
    }
    if(drawableId != null && drawableId != 0 ){
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "Pezzo $pezzoFen",
            modifier = Modifier.fillMaxSize(0.85f)
        )
    }else{
        val coloreTesto = if(pezzoFen.isUpperCase())Color.White else Color.Black
        Text(
            text = pezzoFen.toString(),
            color = coloreTesto,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

fun FenToMap(fen: String) : Array<String>{
    val disposizionePezzi = fen.split(" ")[0]
    val righeFen = disposizionePezzi.split("/")
    return righeFen.map{
        riga -> val costruttoreRiga = StringBuilder()
        for(car in riga){
            if(car.isDigit()){
                val spaziVuoti = car.digitToInt()
                costruttoreRiga.append(" ".repeat(spaziVuoti))
            }else{
                costruttoreRiga.append(car)
            }
        }
        costruttoreRiga.toString()
    }.toTypedArray()
}