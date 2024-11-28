package io.github.siretjorro.slidingpuzzle.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

object BitmapUtil {
    fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun splitBitmap(original: Bitmap, rows: Int, cols: Int): List<Bitmap> {
        val pieceWidth = original.width / cols
        val pieceHeight = original.height / rows

        val pieces = mutableListOf<Bitmap>()

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val x = col * pieceWidth
                val y = row * pieceHeight

                val piece = Bitmap.createBitmap(original, x, y, pieceWidth, pieceHeight)
                pieces.add(piece)
            }
        }

        return pieces
    }

    fun cropBitmapToSquare(original: Bitmap): Bitmap {
        val width = original.width
        val height = original.height

        return if (width > height) {
            Bitmap.createBitmap(original, (width - height) / 2, 0, height, height)
        } else if (width < height) {
            Bitmap.createBitmap(original, 0, (width - height) / 2, width, width)
        } else {
            original
        }
    }
}