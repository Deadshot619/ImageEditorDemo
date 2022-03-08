package com.example.imageeditordemo.utils.imageutils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.BufferedOutputStream
import kotlin.Exception

fun addImageToGallery(
    fileName: String,
    context: Context,
    bitmap: Bitmap
): Boolean {
    return try {

        val values = ContentValues()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        }
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName)
        values.put(MediaStore.Images.ImageColumns.TITLE, fileName)

        val uri: Uri? = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        uri?.let {
            context.contentResolver.openOutputStream(uri)?.let { stream ->
                val oStream =
                    BufferedOutputStream(stream)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream)
                oStream.close()
            }
        }

        true
    } catch (e: Exception){
        false
    }
}