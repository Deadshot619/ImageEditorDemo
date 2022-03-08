package com.example.imageeditordemo.utils.imageutils

import android.R.attr
import android.graphics.Bitmap

fun Bitmap.cropImage(): Bitmap {
    return if (width >= height)
        Bitmap.createBitmap(this, width/2 - height/2, 0, height, height)
    else
        Bitmap.createBitmap(this, 0, height/2 - width/2, width, width)
}