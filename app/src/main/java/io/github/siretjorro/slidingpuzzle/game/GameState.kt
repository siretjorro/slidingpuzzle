package io.github.siretjorro.slidingpuzzle.game

data class GameState(
    val gameBoard: MutableList<Int>,
    val gridSize: Int
) {
    val isSolved: Boolean
        get() {
            for (i in 0 until gameBoard.size - 1) {
                if (gameBoard[i] > gameBoard[i + 1]) {
                    return false
                }
            }
            return true
        }

    fun getEmptyIndex(): Int = gameBoard.indexOf(gridSize * gridSize - 1)
}
