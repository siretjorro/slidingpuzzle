package io.github.siretjorro.slidingpuzzle

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import io.github.siretjorro.slidingpuzzle.databinding.FragmentStartBinding
import io.github.siretjorro.slidingpuzzle.util.BitmapUtil

class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            imageSelected(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.selectImageButton.setOnClickListener { selectClicked() }
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

//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding = null
//    }

    private fun selectClicked() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun imageSelected(uri: Uri?) {
        if (uri != null) {
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
            }
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
}