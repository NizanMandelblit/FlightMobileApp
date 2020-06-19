package com.example.flightmobileapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_second.*

class Second : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val prefrences = getSharedPreferences("database", Context.MODE_PRIVATE)
        val savedUrl = prefrences.getString("url", "This value doesn't exist")
        viewUrl.text = "URL:" + savedUrl



        seekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                num1.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                num1.text =  seekBar.progress.toString()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                num1.text = seekBar.progress.toString()
            }
        })

        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                num2.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                num2.text =  seekBar.progress.toString()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                num2.text = seekBar.progress.toString()
            }
        })


/*
        seekBar3.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                num3.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                num3.text =  seekBar.progress.toString()
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
                num4.text =  seekBar.progress.toString()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                num4.text = seekBar.progress.toString()
            }
        })

*/
    }
}