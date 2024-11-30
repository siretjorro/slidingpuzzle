package io.github.siretjorro.slidingpuzzle


data class GameState(val state: MutableList<Int>, val size: Int) {
    val isSolved: Boolean
        get() {
            for (i in 0 until state.size - 1) {
                if (state[i] > state[i + 1]) {
                    return false
                }
            }
            return true
        }

    fun getEmptyIndex(): Int {
        return state.indexOf(size * size - 1)
    }
}