package io.github.siretjorro.slidingpuzzle

import java.util.Collections
import kotlin.math.abs

class Game(private val gridSize: Int) {

    lateinit var gameState: GameState

    init {
        startGame()
    }

    private fun startGame() {
        gameState = GameState(generatePuzzle(), gridSize)
    }

    private fun generatePuzzle(): MutableList<Int> {
        return (0 until gridSize * gridSize).toList().shuffled().toMutableList()
    }

    // makes move and returns game state if the move is valid
    fun move(selectedIndex: Int): GameState? {
        if (isMoveValid(selectedIndex)) {
            Collections.swap(gameState.gameBoard, gameState.getEmptyIndex(), selectedIndex)
            return gameState
        }
        return null
    }

    fun isSolved(): Boolean {
        return gameState.isSolved
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