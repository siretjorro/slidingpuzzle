package io.github.siretjorro.slidingpuzzle.game

interface Game {
    fun startGame()
    fun move(position: Int)
    fun getGameBoard(): List<Int>
    fun getEmptyIndex(): Int
    fun isSolved(): Boolean
}