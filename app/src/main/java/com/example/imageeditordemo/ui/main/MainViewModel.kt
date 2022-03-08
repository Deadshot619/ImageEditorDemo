package com.example.imageeditordemo.ui.main

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.imageeditordemo.model.Event
import com.example.imageeditordemo.ui.base.BaseViewModel
import com.example.imageeditordemo.utils.imageutils.RotationDegree
import com.example.imageeditordemo.utils.imageutils.addImageToGallery
import com.example.imageeditordemo.utils.imageutils.cropImage
import com.example.imageeditordemo.utils.imageutils.rotateImage
import kotlinx.coroutines.*

class MainViewModel: BaseViewModel() {

    private var editOperationJob: Job? = null

    private val _bitmapImage = MutableLiveData<Event<Bitmap>>()
    val bitmapImage: LiveData<Event<Bitmap>>
        get() = _bitmapImage

    private val _editedImage = MutableLiveData<Bitmap?>()
    val editedImage: LiveData<Bitmap?>
        get() = _editedImage

   private val _imageSaved = MutableLiveData<Event<Boolean?>>()
    val imageSaved: LiveData<Event<Boolean?>>
        get() = _imageSaved


    fun setSelectedImage(image: Bitmap){
        _bitmapImage.value = Event(image)
        resetEditedImage()
    }

    fun rotateSelectedImage(){
        if (editOperationJob?.isActive == true)
            return
        editOperationJob = viewModelScope.launch(Dispatchers.Default) {
            _editedImage.postValue(rotateImage(_editedImage.value ?: _bitmapImage.value?.peekContent()!!, RotationDegree.ROTATE_90.degree)!!)
        }
    }

    fun undoSelectedImage(){
        _editedImage.value = _bitmapImage.value?.peekContent()
    }

    fun cropSelectedImage(){
        if (editOperationJob?.isActive == true)
            return
        editOperationJob = viewModelScope.launch(Dispatchers.Default) {
            _editedImage.postValue((_editedImage.value ?: _bitmapImage.value?.peekContent())?.cropImage())
        }
    }

    fun saveSelectedImage(context: Context){
        if (editOperationJob?.isActive == true)
            return
        editOperationJob = viewModelScope.launch(Dispatchers.Default) {
            _imageSaved.postValue(
                Event(addImageToGallery(FILENAME, context, _editedImage.value ?: _bitmapImage.value?.peekContent()!!))
            )
        }
    }

    private fun resetEditedImage(){
        _editedImage.value = null
        _imageSaved.value = Event(null)
    }

    override fun onCleared() {
        super.onCleared()
        editOperationJob?.cancel()
    }

    companion object {
        private const val FILENAME = "My_Image"
    }
}