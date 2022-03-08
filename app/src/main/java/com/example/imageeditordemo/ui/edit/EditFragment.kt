package com.example.imageeditordemo.ui.edit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.imageeditordemo.R
import com.example.imageeditordemo.databinding.EditFragmentBinding
import com.example.imageeditordemo.ui.base.BaseFragment
import com.example.imageeditordemo.ui.main.MainViewModel
import com.example.imageeditordemo.utils.extensions.showToast
import com.example.imageeditordemo.utils.permission.PermissionUtils
import com.example.imageeditordemo.utils.permission.PermissionsCallback
import com.google.android.material.button.MaterialButton

class EditFragment : BaseFragment<MainViewModel, EditFragmentBinding>() {
    override val mViewModel: MainViewModel by activityViewModels()
    override fun getViewBinding() = EditFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvPreview: AppCompatImageView
    private lateinit var mBtnUndo: MaterialButton
    private lateinit var mBtnRotate: MaterialButton
    private lateinit var mBtnCrop: MaterialButton
    private lateinit var mBtnSave: MaterialButton

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpObservers()
    }

    private fun initViews() {
        mViewBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel

            mIvPreview = ivPreview
            mBtnCrop = btnCrop
            mBtnRotate = btnRotate
            mBtnUndo = btnUndo
            mBtnSave = btnSave
        }
    }

    private fun setUpClickListeners() {
        mBtnSave.setOnClickListener {
            PermissionUtils.requestStoragePermission(requireContext(), object: PermissionsCallback {
                override fun onPermissionRequest(granted: Boolean) {
                    if (granted)
                        mViewModel.saveSelectedImage(requireContext())
                    else
                        showToast(getString(R.string.permission_denied_storage))
                }
            })
        }
    }

    private fun setUpObservers() {
        mViewModel.bitmapImage.observe(viewLifecycleOwner){
            it?.peekContent()?.let {
                mIvPreview.setImageBitmap(it)
            }
        }

        mViewModel.editedImage.observe(viewLifecycleOwner){
            it?.let {
                mIvPreview.setImageBitmap(it)
            }
        }

        mViewModel.imageSaved.observe(viewLifecycleOwner){
            it.peekContent()?.let {
                if (it){
                    showToast(getString(R.string.text_image_save))
                    findNavController().navigateUp()
                } else {
                    showToast(getString(R.string.text_image_not_saved))
                }
            }
        }
    }
}