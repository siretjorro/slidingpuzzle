package io.github.siretjorro.slidingpuzzle

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PuzzleViewModel : ViewModel() {

    private val _action = MutableLiveData<Action>()
    val action = _action as LiveData<Action>

    private val _gameState: MutableLiveData<GameState> = MutableLiveData<GameState>()
    val gameState = _gameState as LiveData<GameState>

    private lateinit var game: Game

    fun onSelectImageClicked() {
        _action.value = Action.OpenImagePicker
    }

    fun onImageSelected(uri: Uri?) {
        uri?.let {
            game = Game(3)
            _action.value = Action.StartPuzzle(uri)
            _gameState.value = game.gameState
        }
    }

    fun onPieceClicked(index: Int) {
        game.move(index)?.let { gameState ->
            _gameState.value = gameState
        }
    }

    sealed class Action {
        data object OpenImagePicker : Action()
        data class StartPuzzle(val uri: Uri) : Action()
    }
}