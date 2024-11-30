package io.github.siretjorro.slidingpuzzle

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
import androidx.core.view.isVisible
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
            onImageSelected(uri)
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
        binding.selectImageButton.setOnClickListener { openMediaPicker() }
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
            })
    }

    private fun initViewModel() {
        viewModel.gameBoard.observe(viewLifecycleOwner) { gameBoard -> showBoard(gameBoard) }
        viewModel.emptyIndex.observe(viewLifecycleOwner) { emptyIndex -> hideEmpty(emptyIndex) }
        viewModel.isSolved.observe(viewLifecycleOwner) { isSolved -> showSolved(isSolved) }
    }

    private fun openMediaPicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun onImageSelected(uri: Uri?) {
        uri?.let {
            val originalBitmap = BitmapUtil.getBitmapFromUri(uri, requireContext())
            originalBitmap?.let {
                val pieces = BitmapUtil.splitBitmap(
                    BitmapUtil.cropBitmapToSquare(originalBitmap),
                    resources.getInteger(R.integer.rows),
                    resources.getInteger(R.integer.rows)
                )

                viewModel.onImageSelected(pieces)
            }
        }
    }

    private fun startStopWatch() {
        // TODO
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
                R.drawable.empty_piece
            )
        )
    }

    private fun showSolved(isSolved: Boolean) {
        binding.infoTextView.isVisible = isSolved

        // TODO: remove click listeners
    }
}