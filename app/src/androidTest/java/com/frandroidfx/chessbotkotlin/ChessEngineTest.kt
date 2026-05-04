package com.frandroidfx.chessbotkotlin

import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.frandroidfx.chessbotkotlin.BackendUX.NetModules.ChessEngine
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.File


@RunWith(AndroidJUnit4::class)
class ChessEngineTest {
    @Test
    fun verificaInizializzazioneMotore() = runBlocking {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val engine = ChessEngine()

        try{
            engine.inizializza(appContext)

            val fileModello = File(appContext.filesDir,"nnue10M100E.pte")

            assertTrue("ERRORE CRITICO: Il file .pte non è stato estratto nella memoria interna!",
                fileModello.exists())

            println("Test Superato: File estratto correttamente (${fileModello.length()} bytes).")
            println("Motore e Rete neurale si sono avviati perfettamente")
        }catch (e : Exception){
            fail("l'avvio del motore è fallito : ${e.stackTraceToString()}")
        }
    }
}