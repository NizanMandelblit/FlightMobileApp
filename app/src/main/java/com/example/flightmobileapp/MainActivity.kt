package com.example.flightmobileapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
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
            }.apply()
            if (UrlInput.text.length == 0) {
                Toast.makeText(this, "You Must Fill in A valid URL", Toast.LENGTH_SHORT).show()
            } else {
                val localhost = LocalHost(UrlInput.text.toString())
                UrlDatabase.get(application).getDao2().insert(localhost)
                Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show()
                val second = Intent(this, Second::class.java)
                startActivity(second)

            }
        }

        val listUrls = UrlDatabase.get(application).getDao2().getLast5()
        button.setOnClickListener() {
            if (listUrls.size >= 1) {
                UrlInput.setText(listUrls[0].urlAdress)
            }
        }
        button2.setOnClickListener() {
            if (listUrls.size >= 2) {
                UrlInput.setText(listUrls[1].urlAdress)
            }
        }
        button3.setOnClickListener() {
            if (listUrls.size >= 3) {
                UrlInput.setText(listUrls[2].urlAdress)
            }
        }
        button4.setOnClickListener() {
            if (listUrls.size >= 4) {
                UrlInput.setText(listUrls[3].urlAdress)
            }
        }
        button5.setOnClickListener() {
            if (listUrls.size == 5) {
                UrlInput.setText(listUrls[4].urlAdress)
            }
        }

    }
}