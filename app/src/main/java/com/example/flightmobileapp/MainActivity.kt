package com.example.flightmobileapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ConnectButton.setOnClickListener() {
            val database = getSharedPreferences("database", Context.MODE_PRIVATE)
            database.edit().apply() {
                putString("url", UrlInput.text.toString())
                    //LocalHost(url)
            }.apply()
            if (UrlInput.text.length == 0) {
                Toast.makeText(this, "You Must Fill in A valid URL", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show()
                val second = Intent(this, Second::class.java)
                startActivity(second)
                button.setOnClickListener() {
                    UrlInput.setText(database.getString("url", "no value"))
                }
            }
        }
        UrlDatabase.get(application)



    }
}