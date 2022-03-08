package com.example.imageeditordemo.utils.permission

interface PermissionsCallback {
    
    // Pass request granted status i.e true or false
    fun onPermissionRequest(granted: Boolean)
    
}