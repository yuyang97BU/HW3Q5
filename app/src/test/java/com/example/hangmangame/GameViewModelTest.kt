package com.example.hangmangame

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.*
import org.junit.Test

class GameViewModelTest{
    @Test
    fun underscoreWordTest(){
        val gameViewModel=GameViewModel()
        assertEquals("------",GameManager().generateUnderScore(6))
        gameViewModel.currentWordToGuess="rocket"
        gameViewModel.playGame('R')
        assertEquals("R-----",gameViewModel.currentUnderscoreWord)

    }

    @Test
    fun startTest() {
        val gameViewModel=GameViewModel()
        gameViewModel.startNewGame()
        assertEquals(gameViewModel.currentHintCount, 0)
        assertEquals(gameViewModel.currentLettersUsed, "")
        assertEquals(gameViewModel.currentTries, 0)
        assertEquals(gameViewModel.currentGameState, 2)
    }
}