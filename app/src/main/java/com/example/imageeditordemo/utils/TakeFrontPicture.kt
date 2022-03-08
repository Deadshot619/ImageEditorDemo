package com.example.imageeditordemo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts

class TakeFrontPicture: ActivityResultContracts.TakePicture() {
    override fun createIntent(context: Context, input: Uri): Intent {
        super.createIntent(context, input)
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, input).apply {
                putExtra("com.google.assistant.extra.USE_FRONT_CAMERA", true)
                putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
                putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
                putExtra("android.intent.extras.CAMERA_FACING", 1)

                // Samsung
                putExtra("camerafacing", "front")
                putExtra("previous_mode", "front")

                // Huawei
                putExtra("default_camera", "1")
                putExtra("default_mode", "com.huawei.camera2.mode.photo.PhotoMode")
            }

    }
}