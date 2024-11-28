package io.github.siretjorro.slidingpuzzle

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PuzzleViewModel : ViewModel() {

    private val _action = MutableLiveData<Action>()
    val action = _action as LiveData<Action>

    private val _state: MutableLiveData<State> = MutableLiveData<State>()
    val state = _state as LiveData<State>

    fun onSelectImageClicked() {
        _action.value = Action.OpenImagePicker
    }

    fun onImageSelected(uri: Uri?) {
        uri?.let {
            _action.value = Action.StartPuzzle(uri)
        }
    }

    sealed class State {
        data object Default : State()
        data object Progress : State()
        data object Error : State()
    }

    sealed class Action {
        data object OpenImagePicker : Action()
        data class StartPuzzle(val uri: Uri) : Action()
    }
}