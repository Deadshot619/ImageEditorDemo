package com.example.imageeditordemo.utils.imageutils

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.InputStream

enum class RotationDegree(val degree: Float) {
    ROTATE_0 (0f),
    ROTATE_90 (90f),
    ROTATE_180 (180f),
    ROTATE_270 (270f),
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height,
        matrix, true
    )
}

fun getRotatedImage(bitmap: Bitmap, photoPath: InputStream): Bitmap? {
    return try {
        val ei = ExifInterface(photoPath)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, RotationDegree.ROTATE_90.degree)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, RotationDegree.ROTATE_180.degree)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, RotationDegree.ROTATE_270.degree)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
    } catch (e: Exception) {
        Log.e("hello", e.message.toString())
        null
    }
}