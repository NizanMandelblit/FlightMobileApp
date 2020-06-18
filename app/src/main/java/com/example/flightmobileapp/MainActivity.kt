package com.example.flightmobileapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
//import com.example.flightmobileapp.UrlDatabase.Companion


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ConnectButton.setOnClickListener() {
            val database = getSharedPreferences("datebase", Context.MODE_PRIVATE)
            database.edit().apply() {
                putString("url", UrlInput.text.toString())
            }.apply()

            button.setOnClickListener() {
                UrlInput.setText(database.getString("url", "no value"))
            }
        }
        UrlDatabase.get(application)


    }
}