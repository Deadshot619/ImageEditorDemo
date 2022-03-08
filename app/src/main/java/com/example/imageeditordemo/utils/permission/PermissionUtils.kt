package com.example.imageeditordemo.utils.permission

import android.Manifest
import android.content.Context
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

object PermissionUtils {

    fun requestStoragePermission(context: Context, callback: PermissionsCallback) {
        requestSinglePermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, callback)
    }
    
    fun requestCameraPermission(context: Context, callback: PermissionsCallback) {
        requestSinglePermission(context, Manifest.permission.CAMERA, callback)
    }
    
    private fun requestSinglePermission(context: Context, permission: String, callback: PermissionsCallback) {
        Dexter.withContext(context)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    // User has granted the permission
                    callback.onPermissionRequest(granted = true)
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    // User previously denied the permission, request them again
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    // User has denied the permission
                    callback.onPermissionRequest(granted = false)
                }
            })
            .check()
    }

}