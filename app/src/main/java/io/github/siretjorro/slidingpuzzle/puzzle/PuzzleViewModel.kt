package io.github.siretjorro.slidingpuzzle.puzzle

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.siretjorro.slidingpuzzle.game.Game
import io.github.siretjorro.slidingpuzzle.game.GameImpl

class PuzzleViewModel : ViewModel() {
    private val _gameBoard: MutableLiveData<List<Bitmap>> = MutableLiveData<List<Bitmap>>()
    val gameBoard = _gameBoard as LiveData<List<Bitmap>>

    private val _emptyIndex: MutableLiveData<Int> = MutableLiveData<Int>()
    val emptyIndex = _emptyIndex as LiveData<Int>

    private val _isSolved: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSolved = _isSolved as LiveData<Boolean>

    private val _time: MutableLiveData<Long> = MutableLiveData<Long>()
    val time = _time as LiveData<Long>

    private lateinit var game: Game
    private lateinit var bitmaps: List<Bitmap>

    private var timeStarted: Long = 0
    private var timeElapsed: Long = 0
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var stopwatchRunnable: Runnable =
        object : Runnable {
            override fun run() {
                timeElapsed = System.currentTimeMillis() - timeStarted
                _time.value = timeElapsed
                handler.postDelayed(this, 1000)
            }
        }

    fun init(gridSize: Int) {
        game = GameImpl(gridSize)
    }

    fun onImageSelected(pieces: List<Bitmap>) {
        bitmaps = pieces
        game.startGame()
        updateGameState()
        startStopWatch()
    }

    fun onPieceClicked(position: Int) {
        game.move(position)
        updateGameState()
    }

    private fun startStopWatch() {
        timeStarted = System.currentTimeMillis()
        _time.value = timeStarted
        handler.post(stopwatchRunnable)
    }

    private fun updateGameState() {
        _gameBoard.value = getSortedBitmaps(game.getGameBoard())
        _isSolved.value = game.isSolved()
        if (isSolved.value != true) {
            _emptyIndex.value = game.getEmptyIndex()
        } else {
            handler.removeCallbacks(stopwatchRunnable)
        }
    }

    private fun getSortedBitmaps(indices: List<Int>): List<Bitmap> {
        val sortedBitmaps = mutableListOf<Bitmap>()
        for (i in indices.indices) {
            sortedBitmaps.add(bitmaps[indices[i]])
        }

        return sortedBitmaps
    }
}
