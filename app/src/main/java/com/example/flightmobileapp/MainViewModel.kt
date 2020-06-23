package com.example.flightmobileapp

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
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
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.locks.ReentrantLock

class MainViewModel: ViewModel() {
    @Volatile private var context:Context? = null
    @Volatile private var joystickModel:JoystickModel? = null
    private val lock = ReentrantLock()
    @Volatile private var there_is_new_val:Boolean = false
    @Volatile var my_url:String = ""
    @Volatile var _my_url:String? =null
    set(value) {
        //RetrofitObj.my_url = value
        my_url = value!!
        field = value
    }

init {
    this.context = context
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
    val message: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
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
        //Toast.makeText( Context,"toast", Toast.LENGTH_SHORT).show()

        viewModelScope.launch(Dispatchers.Main) {
            endlessLoop(message, joystickModel)
            /*
            while (true) {
                var my_joystick:JoystickModel? = null
               setBitmapFrom(screenshot, message)
                if (there_is_new_val){
                    lock.lock()
                    there_is_new_val = false
                    my_joystick = joystickModel
                    lock.unlock()
                    sendValusTosim(message, my_joystick)
                }

            }

             */
        }
    }
    suspend fun endlessLoop(messageFromSim:MutableLiveData<String>, my_joystick:JoystickModel?) {
        return withContext(Dispatchers.IO) {
            while (true) {
                var my_joystick:JoystickModel? = null
                setBitmapFrom(screenshot, message)
                if (there_is_new_val){
                    lock.lock()
                    there_is_new_val = false
                    my_joystick = joystickModel
                    lock.unlock()
                    sendValusTosim(message, my_joystick)
                }

            }
        }
    }
    fun sendValusTosim(messageFromSim:MutableLiveData<String>, my_joystick:JoystickModel?) {

            //Log.d("TAG", elevator.toString())
            //RetrofitObj.my_url = my_url
            try {
                //Log.d("TAG","unable to send values to server")

                RetrofitObj.sendValsToSim(my_joystick, messageFromSim, my_url)

            }catch (t:Throwable) {

                message.postValue(t.toString())

            }

    }

    fun setBitmapFrom(mm:MutableLiveData<Bitmap>, messageFromSim:MutableLiveData<String>)  {
            //message.value = "dfsdfdf"
            //RetrofitObj.my_url = my_url
            try {
                RetrofitObj.getBitmapFrom(mm, messageFromSim, my_url  ) {
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

            } catch (t: Throwable) {
                message.postValue(t.toString())

            }



    }
}