package com.example.flightmobileapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_second.*


class Second : AppCompatActivity(), JoystickView.JoystickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_second)
        val model: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

        val joystick = JoystickView(this)
        val prefrences = getSharedPreferences("database", Context.MODE_PRIVATE)
        val savedUrl = prefrences.getString("url", "This value doesn't exist")
        viewUrl.text = "URL:" + savedUrl

        model.screenshot?.observe(this, Observer<Bitmap> { newval ->
            imageView.setImageBitmap(newval)
        })
        model.getscreenshot()

        seekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                throttleNum.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                throttleNum.text = seekBar.progress.toString()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                throttleNum.text = seekBar.progress.toString()
            }
        })

        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                rudderNum.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                rudderNum.text = seekBar.progress.toString()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                rudderNum.text = seekBar.progress.toString()
            }
        })

/*
        seekBar3.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                num3.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                num3.text = seekBar.progress.toString()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                num3.text = seekBar.progress.toString()
            }
        })

        seekBar4.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                num4.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                num4.text = seekBar.progress.toString()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                num4.text = seekBar.progress.toString()
            }
        })

*/
    }

    override fun onJoystickMoved(xPercent: Float, yPercent: Float, id: Int) {
        when (id) {
            R.id.joystickView -> Log.d(
                "Right Joystick",
                "X percent: $xPercent Y percent: $yPercent"
            )
        }
        //aileronNum.text = xPercent.toString()
        //elevatorNum.text = yPercent.toString()

    }
}
