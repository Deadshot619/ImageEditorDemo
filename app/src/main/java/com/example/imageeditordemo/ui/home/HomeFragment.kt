package com.example.imageeditordemo.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.imageeditordemo.R
import com.example.imageeditordemo.databinding.HomeFragmentBinding
import com.example.imageeditordemo.ui.base.BaseFragment
import com.example.imageeditordemo.ui.main.MainViewModel
import com.example.imageeditordemo.utils.TakeFrontPicture
import com.example.imageeditordemo.utils.extensions.showToast
import com.example.imageeditordemo.utils.imageutils.getRotatedImage
import com.example.imageeditordemo.utils.permission.PermissionUtils
import com.example.imageeditordemo.utils.permission.PermissionsCallback
import com.google.android.material.button.MaterialButton
import java.io.File


class HomeFragment : BaseFragment<MainViewModel, HomeFragmentBinding>() {
    override val mViewModel: MainViewModel by activityViewModels()
    override fun getViewBinding() = HomeFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvPreview: AppCompatImageView
    private lateinit var mBtnUploadPic: MaterialButton
    private lateinit var mBtnTakeSelfie: MaterialButton

    private lateinit var file: File
    private lateinit var mImageUri: Uri

    private lateinit var takePicture: ActivityResultLauncher<Uri>

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpObservers()
        registerForPicture()
    }

    private fun initViews(){
        mViewBinding.run {
            mBtnTakeSelfie = btnTakeSelfie
            mBtnUploadPic = btnUpload
            mIvPreview = ivPreview
        }
    }

    private fun registerForPicture(){
        try{
            file = File(activity?.filesDir, "picFromCamera")
            mImageUri = FileProvider.getUriForFile(
                requireContext(), context?.applicationContext?.packageName.toString(), file
            )
        }catch(e: Exception){
            showToast(e.message.toString())
            Log.i("hello", e.message.toString())
        }
        takePicture = registerForActivityResult(TakeFrontPicture()) { success: Boolean ->
            if (success) {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, mImageUri)
                val rotatedImage = getRotatedImage(bitmap, context?.contentResolver?.openInputStream(mImageUri)!!)
                mViewModel.setSelectedImage(rotatedImage!!)
            }
        }

    }

    private fun setUpClickListeners(){
        mBtnUploadPic.setOnClickListener {
            PermissionUtils.requestCameraPermission(requireContext(), object: PermissionsCallback {
                override fun onPermissionRequest(granted: Boolean) {
                    if (granted)
                        getDataFromDevice()
                    else
                        showToast(getString(R.string.permission_denied_camera))
                }
            })
        }

        mBtnTakeSelfie.setOnClickListener {
            PermissionUtils.requestCameraPermission(requireContext(), object: PermissionsCallback {
                override fun onPermissionRequest(granted: Boolean) {
                    if (granted)
                        clickSelfie()
                    else
                        showToast(getString(R.string.permission_denied_camera))
                }
            })
        }
    }


    private fun setUpObservers() {
        mViewModel.bitmapImage.observe(viewLifecycleOwner){
            it.contentIfNotHandled?.let {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToEditFragment())
            }
        }

        mViewModel.imageSaved.observe(viewLifecycleOwner){
            it.contentIfNotHandled?.let {
                if (it)
                    mIvPreview.setImageBitmap(mViewModel.editedImage.value)
            }
        }
    }

    private fun getDataFromDevice(){
        val photoPickerIntent = Intent(Intent.ACTION_PICK).apply {
            type = TYPE_IMAGE
        }
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
    }

    private fun clickSelfie(){
        takePicture.launch(mImageUri)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST -> {
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data?.data)
                        mViewModel.setSelectedImage(bitmap)
                    } catch (e: Exception) {
                        showToast(e.message.toString())
                    }
                }
            }
        }
    }

    companion object {
        private const val TYPE_IMAGE = "image/*"
        private const val GALLERY_REQUEST = 69
    }
}