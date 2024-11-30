package io.github.siretjorro.slidingpuzzle.game

import java.util.Collections
import kotlin.math.abs

class Game(private val gridSize: Int) {

    private lateinit var gameState: GameState

    fun startGame(): GameState {
        gameState = GameState(generatePuzzle(), gridSize)
        return gameState
    }

    // makes move and returns game state if the move is valid
    fun move(selectedIndex: Int): GameState? {
        if (isMoveValid(selectedIndex) && !gameState.isSolved) {
            Collections.swap(gameState.gameBoard, gameState.getEmptyIndex(), selectedIndex)
            return gameState
        }
        return null
    }

    private fun generatePuzzle(): MutableList<Int> {
        return (0 until gridSize * gridSize).toList().shuffled().toMutableList()
    }

    private fun isMoveValid(index: Int): Boolean {
        val selectedRow = index / gridSize
        val selectedCol = index % gridSize

        val emptyRow = gameState.getEmptyIndex() / gridSize
        val emptyCol = gameState.getEmptyIndex() % gridSize

        val areAdjacentHorizontally = selectedRow == emptyRow && abs(selectedCol - emptyCol) == 1
        val areAdjacentVertically = selectedCol == emptyCol && abs(selectedRow - emptyRow) == 1

        return areAdjacentHorizontally || areAdjacentVertically
    }
}