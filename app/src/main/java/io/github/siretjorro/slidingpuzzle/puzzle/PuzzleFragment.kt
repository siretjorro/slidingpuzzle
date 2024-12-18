package io.github.siretjorro.slidingpuzzle.puzzle

import android.graphics.Bitmap
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
import androidx.fragment.app.viewModels
import io.github.siretjorro.slidingpuzzle.R
import io.github.siretjorro.slidingpuzzle.common.BaseFragment
import io.github.siretjorro.slidingpuzzle.databinding.FragmentPuzzleBinding
import io.github.siretjorro.slidingpuzzle.util.BitmapUtil
import io.github.siretjorro.slidingpuzzle.util.getMinutes
import io.github.siretjorro.slidingpuzzle.util.getSeconds

class PuzzleFragment : BaseFragment<FragmentPuzzleBinding>() {
    private val viewModel: PuzzleViewModel by viewModels()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                onImageSelected(uri)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = createBinding(FragmentPuzzleBinding.inflate(inflater, container, false))

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        initViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpUI() {
        binding.selectImageButton.setOnClickListener { openMediaPicker() }
        binding.stopwatchTextView.text = resources.getString(R.string.stopwatch, 0, 0)
        binding.puzzleGridLayout.viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val gridLayoutWidth = binding.puzzleGridLayout.width
                    val gridLayoutParams = binding.puzzleGridLayout.layoutParams
                    gridLayoutParams.width = gridLayoutWidth
                    gridLayoutParams.height = gridLayoutWidth
                    binding.puzzleGridLayout.layoutParams = gridLayoutParams

                    binding.puzzleGridLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            },
        )
    }

    private fun initViewModel() {
        viewModel.init(resources.getInteger(R.integer.grid_size))
        viewModel.gameBoard.observe(viewLifecycleOwner) { gameBoard -> showBoard(gameBoard) }
        viewModel.emptyIndex.observe(viewLifecycleOwner) { emptyIndex -> hideEmpty(emptyIndex) }
        viewModel.time.observe(viewLifecycleOwner) { time -> showStopwatch(time) }
        viewModel.isSolved.observe(viewLifecycleOwner) { isSolved -> showSolved(isSolved) }
    }

    private fun openMediaPicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun onImageSelected(uri: Uri?) {
        uri?.let {
            val originalBitmap = BitmapUtil.getBitmapFromUri(uri, requireContext())
            originalBitmap?.let {
                val pieces =
                    BitmapUtil.splitBitmap(
                        BitmapUtil.cropBitmapToSquare(originalBitmap),
                        resources.getInteger(R.integer.grid_size),
                        resources.getInteger(R.integer.grid_size),
                    )

                viewModel.onImageSelected(pieces)
            }
        }
    }

    private fun showBoard(gameBoard: List<Bitmap>) {
        for (i in gameBoard.indices) {
            val imageView = binding.puzzleGridLayout.getChildAt(i) as ImageView
            imageView.setImageBitmap(gameBoard[i])
            imageView.setOnClickListener { viewModel.onPieceClicked(i) }
        }
    }

    private fun hideEmpty(emptyIndex: Int) {
        val emptyImageView = binding.puzzleGridLayout.getChildAt(emptyIndex) as ImageView
        emptyImageView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.empty_piece,
            ),
        )
    }

    private fun showStopwatch(time: Long) {
        binding.stopwatchTextView.text =
            resources.getString(R.string.stopwatch, time.getMinutes(), time.getSeconds())
    }

    private fun showSolved(isSolved: Boolean) {
        binding.infoTextView.visibility = if (isSolved) View.VISIBLE else View.INVISIBLE
    }
}
