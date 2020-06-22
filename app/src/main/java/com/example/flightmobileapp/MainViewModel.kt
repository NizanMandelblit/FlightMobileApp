package com.example.flightmobileapp

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightmobileapp.Models.JoystickModel
import com.example.flightmobileapp.Models.RetrofitObj
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.locks.ReentrantLock

class MainViewModel: ViewModel() {
    @Volatile private var joystickModel:JoystickModel? = null
    private val lock = ReentrantLock()
    @Volatile private var there_is_new_val:Boolean = false
    @Volatile var my_url:String = ""
    @Volatile var _my_url:String? =null
    set(value) {
        RetrofitObj.my_url = value
        my_url = value!!
        field = value
    }



    private var throttle:Float = 0.0F
    var _throttle:Float
    get() {
        return throttle
    }
    set(value) {
        if (!checkIfBiggerThenOnePrecent(throttle, value))
            return
        lock.lock()
        there_is_new_val = true
        throttle = value
        createJoystickObject()
        lock.unlock()
    }
    private var rudder:Float = 0.0F
    var _rudder:Float
        get() {
            return rudder
        }
        set(value) {
            if (!checkIfBiggerThenOnePrecent(rudder, value))
                return
            lock.lock()
            there_is_new_val = true
            rudder = value
            createJoystickObject()
            lock.unlock()
        }
    private var aileron:Float = 0.0F
    var _aileron:Float
        get() {
            return aileron
        }
        set(value) {
            if (!checkIfBiggerThenOnePrecent(aileron, value))
                return
            lock.lock()
            there_is_new_val = true
            aileron = value
            createJoystickObject()
            lock.unlock()
        }
    private var elevator:Float = 0.0F
    var _elevator:Float
        get() {
            return elevator
        }
        set(value) {
            if (!checkIfBiggerThenOnePrecent(elevator, value))
                return
            lock.lock()
            there_is_new_val = true
            elevator = value
            createJoystickObject()
            lock.unlock()
        }


    val screenshot: MutableLiveData<Bitmap> by lazy {
        MutableLiveData<Bitmap>()
    }

    fun createJoystickObject()  {
        joystickModel = JoystickModel(aileron, rudder, elevator, throttle)
    }

    fun checkIfBiggerThenOnePrecent(oldval:Float, newval:Float) : Boolean {
        if (oldval >= 0) {
            if (newval > oldval) {
                if (oldval + oldval * 0.01 <= newval)
                    return true
                return false
            } else {
                if (oldval - oldval * 0.01 >= newval)
                    return true
                return false
            }
        } else {
            if (newval < oldval) {
                if (oldval + oldval * 0.01 >= newval)
                    return true
                return false
            } else {
                if (oldval - oldval * 0.01 <= newval)
                    return true
                return false
            }
        }

    }
    fun getscreenshot() {

        viewModelScope.launch(Dispatchers.Main) {
            while (true) {
               setBitmapFrom(screenshot)
                lock.lock()
                if (there_is_new_val){
                    there_is_new_val = false
                    sendValusTosim()
                }
                lock.unlock()
            }
        }
    }
    suspend fun sendValusTosim() {
        return withContext(Dispatchers.IO) {
            //Log.d("TAG", elevator.toString())
            RetrofitObj.my_url = my_url

            RetrofitObj.sendValsToSim(joystickModel)
        }
    }
    suspend fun setBitmapFrom(mm:MutableLiveData<Bitmap>)  {

        return withContext(Dispatchers.IO) {
            RetrofitObj.my_url = my_url

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