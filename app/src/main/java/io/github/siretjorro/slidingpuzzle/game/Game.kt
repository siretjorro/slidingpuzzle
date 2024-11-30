package io.github.siretjorro.slidingpuzzle.game

import java.util.Collections
import kotlin.math.abs

class Game(
    private val gridSize: Int
) {
    private lateinit var gameState: GameState

    fun startGame(): GameState {
        gameState = GameState(generateSolvablePuzzle(gridSize).toMutableList(), gridSize)
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

    private fun generateSolvablePuzzle(gridSize: Int): List<Int> {
        val totalTiles = gridSize * gridSize
        var puzzle: List<Int>
        do {
            puzzle = (0 until totalTiles).toList().shuffled()
        } while (!isSolvable(puzzle, gridSize))

        return puzzle
    }

    private fun isSolvable(
        puzzle: List<Int>,
        gridSize: Int
    ): Boolean {
        val inversionCount = countInversions(puzzle)
        val emptyTileRowFromBottom = findEmptyTileRowFromBottom(puzzle, gridSize)

        return if (gridSize % 2 != 0) {
            inversionCount % 2 == 0
        } else {
            (inversionCount % 2 == 0 && emptyTileRowFromBottom % 2 != 0) ||
                (inversionCount % 2 != 0 && emptyTileRowFromBottom % 2 == 0)
        }
    }

    private fun countInversions(puzzle: List<Int>): Int {
        var inversions = 0
        for (i in 0 until puzzle.size - 1) {
            for (j in i + 1 until puzzle.size) {
                if (puzzle[i] > puzzle[j] && puzzle[i] != 0 && puzzle[j] != 0) {
                    inversions++
                }
            }
        }

        return inversions
    }

    private fun findEmptyTileRowFromBottom(
        puzzle: List<Int>,
        gridSize: Int
    ): Int {
        val emptyTileIndex = puzzle.indexOf(gridSize * gridSize - 1)
        val emptyTileRow = emptyTileIndex / gridSize

        return gridSize - emptyTileRow
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
