package io.github.siretjorro.slidingpuzzle

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PuzzleViewModel : ViewModel() {

    private val _gameBoard: MutableLiveData<List<Bitmap>> = MutableLiveData<List<Bitmap>>()
    val gameBoard = _gameBoard as LiveData<List<Bitmap>>

    private val _emptyIndex: MutableLiveData<Int> = MutableLiveData<Int>()
    val emptyIndex = _emptyIndex as LiveData<Int>

    private val _isSolved: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSolved = _isSolved as LiveData<Boolean>

    private lateinit var game: Game
    private lateinit var bitmaps: List<Bitmap>

    fun onPieceClicked(index: Int) {
        game.move(index)?.let { gameState ->
            updateGameState(gameState)
        }
    }

    fun onImageSelected(pieces: List<Bitmap>) {
        bitmaps = pieces
        game = Game(3)
        updateGameState(game.gameState)
    }

    private fun updateGameState(gameState: GameState) {
        _gameBoard.value = getSortedBitmaps(gameState.gameBoard)
        if (!gameState.isSolved) {
            _emptyIndex.value = gameState.getEmptyIndex()
        }
        _isSolved.value = gameState.isSolved
    }

    private fun getSortedBitmaps(indices: List<Int>): List<Bitmap> {
        val sortedBitmaps = mutableListOf<Bitmap>()

        for (i in indices.indices) {
            sortedBitmaps.add(bitmaps[indices[i]])
        }

        return sortedBitmaps
    }
}