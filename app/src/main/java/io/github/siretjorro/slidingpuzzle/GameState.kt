package io.github.siretjorro.slidingpuzzle


data class GameState(val state: MutableList<Int>, val size: Int, var isSolved: Boolean) {
    fun getEmptyIndex(): Int {
        return state.indexOf(size * size - 1)
    }
}