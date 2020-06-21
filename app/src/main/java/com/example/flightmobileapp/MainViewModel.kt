package com.example.flightmobileapp

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightmobileapp.Models.RetrofitObj
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {
    val screenshot: MutableLiveData<Bitmap> by lazy {
        MutableLiveData<Bitmap>()
    }
    fun getscreenshot() {
        viewModelScope.launch(Dispatchers.Main) {
            while (true) {
               setBitmapFrom(screenshot)
            }
        }
    }
    suspend fun setBitmapFrom(mm:MutableLiveData<Bitmap>)  {

        return withContext(Dispatchers.IO) {

            RetrofitObj.getBitmapFrom(mm) {
                val bitmap: Bitmap
                bitmap = if (it != null) it else {
                    // create empty bitmap
                    val w = 1
                    val h = 1
                    val conf = Bitmap.Config.ARGB_8888
                    Bitmap.createBitmap(w, h, conf)
                }

                return@getBitmapFrom bitmap
            }


        }
    }
}