package io.github.siretjorro.slidingpuzzle.common

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

open class BaseFragment<BINDING : ViewBinding> : Fragment() {
    protected var _binding: BINDING? = null
    protected val binding get() = checkNotNull(_binding) { "viewBinding access outside of fragment lifecycle" }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun createBinding(binding: BINDING): View? {
        _binding = binding
        return binding.root
    }
}
