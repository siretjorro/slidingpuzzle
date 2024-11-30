package io.github.siretjorro.slidingpuzzle

import java.util.Collections
import kotlin.math.abs

class Game(val size: Int) {

    lateinit var gameState: GameState

    init {
        startGame()
    }

    private fun startGame() {
        gameState = GameState(generatePuzzle(), size)
    }

    private fun generatePuzzle(): MutableList<Int> {
        return (0 until size * size).toList().shuffled().toMutableList()
    }

    fun move(selectedIndex: Int): GameState? {
        if (isMoveValid(selectedIndex)) {
            Collections.swap(gameState.state, gameState.getEmptyIndex(), selectedIndex)
            return gameState
        }
        return null
    }

    private fun isMoveValid(index: Int): Boolean {
        val selectedRow = index / size
        val selectedCol = index % size

        val emptyRow = gameState.getEmptyIndex() / size
        val emptyCol = gameState.getEmptyIndex() % size

        val areAdjacentHorizontally = selectedRow == emptyRow && abs(selectedCol - emptyCol) == 1
        val areAdjacentVertically = selectedCol == emptyCol && abs(selectedRow - emptyRow) == 1

        return areAdjacentHorizontally || areAdjacentVertically
    }
}