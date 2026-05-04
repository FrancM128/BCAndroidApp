package com.frandroidfx.chessbotkotlin.FrontendUI
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Chessboard(
    casellaSelezionata: String? = null,
    mossePossibili: List<String> = emptyList(),
    pezzi: Array<String>,
    onCasellaCliccata: (String) ->Unit

){
    val lightSquare = Color(0xFF00D9B5)
    val darkSquare = Color(0xFFB58863)
    val highlightColor = Color(0x80FFF033)
    val dotColor = Color(0x80000000)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(2.dp , Color.DarkGray)
    ){
        for(row in 0 until 8){
            Row(
                modifier = Modifier.weight(1f)
            ){
                for(col in 0 until 8){
                    val isLight = (row + col) %2 == 0
                    val baseSquareColor = if (isLight) lightSquare else darkSquare

                    val nomeFila = ('a' + col).toString()
                    val nomeTraversa = (8-row).toString()
                    val nomeCasella = "$nomeFila$nomeTraversa"

                    val isSelezionata = nomeCasella == casellaSelezionata
                    val isMossaPossibile = mossePossibili.contains(nomeCasella)
                    val pezzoCorrente: Char = pezzi[row][col]
                    Box(modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(if (isSelezionata) highlightColor else baseSquareColor)
                        .clickable{onCasellaCliccata(nomeCasella)},
                            contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = nomeCasella,
                            color = if (isLight) darkSquare.copy(alpha = 0.6f) else lightSquare.copy(alpha = 0.6f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.TopStart).padding(2.dp)
                        )
                        if (pezzoCorrente != ' '){
                            //qui inserire immagini E. Image(painter = painterResource(id = R.drawable...
                            val coloreTesto = if(pezzoCorrente.isUpperCase()) Color.White else Color.Black
                            Text(
                                text = pezzoCorrente.toString(),
                                color = coloreTesto,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (isMossaPossibile){
                            Box(modifier = Modifier
                                .size(16.dp)
                                .background(dotColor,shape = CircleShape))
                        }
                        Text(text = nomeCasella,
                            color = if(isLight) darkSquare else lightSquare,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light)
                    }
                }
            }
        }
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