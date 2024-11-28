package io.github.siretjorro.slidingpuzzle

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.siretjorro.slidingpuzzle.databinding.FragmentPuzzleBinding
import io.github.siretjorro.slidingpuzzle.util.BitmapUtil

class PuzzleFragment : Fragment() {

    private lateinit var binding: FragmentPuzzleBinding
    private val viewModel: PuzzleViewModel by viewModels()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.onImageSelected(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPuzzleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        initViewModel()
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding = null
//    }

    private fun setUpUI() {
        binding.selectImageButton.setOnClickListener { viewModel.onSelectImageClicked() }
        binding.gridLayout.viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val pWidth = binding.gridLayout.width
                    val pParams: ViewGroup.LayoutParams = binding.gridLayout.layoutParams
                    pParams.width = pWidth
                    pParams.height = pWidth
                    binding.gridLayout.layoutParams = pParams

                    binding.gridLayout.viewTreeObserver.removeOnGlobalLayoutListener(this);
                }
            })
    }

    private fun initViewModel() {
        viewModel.action.observe(viewLifecycleOwner) { action ->
            when (action) {
                PuzzleViewModel.Action.OpenImagePicker -> openMediaPicker()
                is PuzzleViewModel.Action.StartPuzzle -> startPuzzle(action.uri)
            }
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                PuzzleViewModel.State.Default -> {

                }
                PuzzleViewModel.State.Progress -> {

                }
                PuzzleViewModel.State.Error -> {

                }
            }
        }
    }

    private fun openMediaPicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startPuzzle(uri: Uri) {
        val originalBitmap = BitmapUtil.getBitmapFromUri(uri, requireContext())

        originalBitmap?.let {
            val imagePieces = BitmapUtil.splitBitmap(
                BitmapUtil.cropBitmapToSquare(originalBitmap),
                resources.getInteger(R.integer.rows),
                resources.getInteger(R.integer.rows)
            ).shuffled()

            for (i in imagePieces.indices) {
                val imageView = binding.gridLayout.getChildAt(i) as ImageView
                imageView.setImageBitmap(imagePieces[i])
            }

            val lastImageView = binding.gridLayout.getChildAt(resources.getInteger(R.integer.rows) * resources.getInteger(R.integer.rows) - 1) as ImageView
            lastImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.empty_piece))
        }
    }
}