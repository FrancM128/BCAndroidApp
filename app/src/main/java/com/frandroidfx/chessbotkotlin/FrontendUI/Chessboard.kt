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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.frandroidfx.chessbotkotlin.R


@Composable
fun Chessboard(
    pezzi: Array<String>,
    giocaComeBianco: Boolean = true,
    casellaSelezionata: String? = null,
    mossePossibili: List<String> = emptyList(),
    caselleEvidenziate: List<String> = emptyList(),

    onCasellaCliccata: (String) ->Unit

){
    val lightSquare = Color(0xFFFFCA01)
    val darkSquare = Color(0xFF6B3300)
    val highlightSelection = Color(0x9000FFFF)
    val highlightLastMove = Color(0x7AFF0000)
    val dotColor = Color(0x809A9A9A)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(2.dp, Color.DarkGray)
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
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(2.dp)
                            )
                        }
                        if(row == (if (giocaComeBianco) 7 else 0)){
                            Text(
                                text = nomeFila,
                                color = if(isLight) darkSquare else lightSquare,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(2.dp)
                            )
                        }
                        if(pezzoCorrente != ' '){
                            DisegnaPezzo(pezzoCorrente)
                        }
                        if(isMossaPossibile){
                            Box(modifier = Modifier
                                .fillMaxSize(0.3f)
                                .background(dotColor, shape = CircleShape))
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
        'P' -> R.drawable.wp
        'N' -> R.drawable.wn
        'B' -> R.drawable.wb
        'R' -> R.drawable.wr
        'Q' -> R.drawable.wq
        'K' -> R.drawable.wk
        'p' -> R.drawable.bp
        'n' -> R.drawable.bn
        'b' -> R.drawable.bb
        'r' -> R.drawable.br
        'q' -> R.drawable.bq
        'k' -> R.drawable.bk
        else -> null
    }
    if(drawableId == null) return
    Image(
        painter = painterResource(id = drawableId),
        contentDescription = "Pezzo $pezzoFen",
        modifier = Modifier.fillMaxSize()
    )

}
@Composable
fun BarraValutazione(score: Float){
    val clampedScore = score.coerceIn(-5f,5f)
    val percentualeBianco = (clampedScore +5f) /10f

    Column( modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)){
        Text(
            if(score >= 0) "+${String.format("%.1f", score)}" else String.format("%.1f", score),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Box(modifier = Modifier .fillMaxWidth() .height(10.dp).background(Color.Black)){
            Box(modifier = Modifier.fillMaxWidth(percentualeBianco).fillMaxHeight().background(Color.White).align(Alignment.CenterEnd))
            Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.Black).align(Alignment.Center))
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

