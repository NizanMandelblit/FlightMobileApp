package com.example.flightmobileapp

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_second.*
import kotlin.math.round


class Second : AppCompatActivity(), JoystickView.JoystickListener {

    @RequiresApi(Build.VERSION_CODES.O)
    private lateinit var model: MainViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_second)
        model =  ViewModelProvider(this).get(MainViewModel::class.java)

        val joystick = JoystickView(this)
        val prefrences = getSharedPreferences("database", Context.MODE_PRIVATE)
        val savedUrl = prefrences.getString("url", "This value doesn't exist")
        viewUrl.text = "URL:" + savedUrl

        model.screenshot?.observe(this, Observer<Bitmap> { newval ->
            imageView.setImageBitmap(newval)
        })
        //model.my_url = savedUrl!!
        model.my_url = "http://10.0.2.2:5000/"
        model.getscreenshot()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                throttleNum.text =
                    (round(getConvertedValue(seekBar.progress) * 100) / 100).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                throttleNum.text =
                    (round(getConvertedValue(seekBar.progress) * 100) / 100).toString()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                throttleNum.text =
                    (round(getConvertedValue(seekBar.progress) * 100) / 100).toString()
            }
        })

        rudderSeekBar.max = 200;
        // rudder seek bar  movement
        rudderSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                rudderNum.text = getConvertedValue(-100 + progress).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                rudderNum.text = getConvertedValue(-100 + seekBar.progress).toString()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                rudderNum.text = getConvertedValue(-100 + seekBar.progress).toString()
            }
        })

    }


    //Joystick movement
    override fun onJoystickMoved(xPercent: Float, yPercent: Float, id: Int) {

        when (id) {
            R.id.joystickView -> Log.d(
                "Right Joystick",
                "X percent: $xPercent Y percent: $yPercent"
            )
        }


        model._aileron = xPercent
        model._elevator = yPercent
        //aileronNum.text = xPercent.toString()
        //elevatorNum.text = yPercent.toString()
        aileronNum.text = (round(xPercent * 100) / 100).toString()
        elevatorNum.text = (round(yPercent * 100) / 100).toString()

    }

    // convert number to float in range
    fun getConvertedValue(intVal: Int): Float {
        var floatVal = 0.0f
        floatVal = .01f * intVal
        return floatVal
        val i=0
    }
}
