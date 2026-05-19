package com.frandroidfx.chessbotkotlin

import com.frandroidfx.chessbotkotlin.Backend.NetModules.PolyglotBook
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

import org.junit.Test
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PolyglotBookTest {
    private fun creaEntryBinaria(key : Long , move : Int, weight : Int): ByteArray{
        val buffer = ByteBuffer.allocate(16).order(ByteOrder.BIG_ENDIAN)
        buffer.putLong(key)
        buffer.putShort(move.toShort())
        buffer.putShort(weight.toShort())
        buffer.putInt(0)
        return buffer.array()
    }

    @Test
    fun cercaMossaRitornaStringa(){
        val  keyTest = 123456789L
        val fakeBytes = creaEntryBinaria(key = keyTest,move = 796,weight = 50)
        val book = PolyglotBook(fakeBytes)
        val moveFound = book.getMossaMigliore(keyTest)
        assertEquals("e2e4",moveFound)
    }

    @Test
    fun mossaPesoZeroIgnorata(){
        var keyTest = 555L
        val fakeBytes = creaEntryBinaria(key = keyTest,move = 796,weight = 0)
        val book = PolyglotBook(fakeBytes)
        val moveFound = book.getMossaMigliore(keyTest)
        assertNull(moveFound)
    }
}