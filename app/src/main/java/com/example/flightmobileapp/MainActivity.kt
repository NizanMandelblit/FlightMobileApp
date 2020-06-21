package com.example.flightmobileapp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.webkit.URLUtil
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        var viewModelJob = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        uiScope.launch {
// this will execute under IO context
            val data = suspendFunction()

        }

    }


    suspend fun suspendFunction(): List<LocalHost> {
        withContext(Dispatchers.IO) {
             z = UrlDatabase.getInstance(application).functionsDatabaseDao.getLast5()
            return@withContext z
        }

    }


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
                insertData(localhost, application).execute()
                //Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show()
                //val second = Intent(this, Second::class.java)
                //startActivity(second)

            }
        }

        button.setOnClickListener() {
            val listUrls = UrlDatabase.getInstance(application).functionsDatabaseDao.getLast5()
            if (listUrls.size >= 1) {
                UrlInput.setText(listUrls[0].urlAdress)
            }
        }
        button2.setOnClickListener() {
            val listUrls = UrlDatabase.getInstance(application).functionsDatabaseDao.getLast5()
            if (listUrls.size >= 2) {
                UrlInput.setText(listUrls[1].urlAdress)
            }
        }
        button3.setOnClickListener() {
            val listUrls = UrlDatabase.getInstance(application).functionsDatabaseDao.getLast5()
            if (listUrls.size >= 3) {
                UrlInput.setText(listUrls[2].urlAdress)
            }
        }
        button4.setOnClickListener() {
            val listUrls = UrlDatabase.getInstance(application).functionsDatabaseDao.getLast5()
            if (listUrls.size >= 4) {
                UrlInput.setText(listUrls[3].urlAdress)
            }
        }
        button5.setOnClickListener() {
            val listUrls = UrlDatabase.getInstance(application).functionsDatabaseDao.getLast5()
            if (listUrls.size == 5) {
                UrlInput.setText(listUrls[4].urlAdress)
            }
        }

    }
}

class insertData(val localhost: LocalHost, val application: Application) :
    AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        UrlDatabase.getInstance(application).functionsDatabaseDao.insert1(localhost)
        return null
    }
}

class bringData(val application: Application) : AsyncTask<Void, Void, List<LocalHost>>() {
    override fun doInBackground(vararg params: Void?): List<LocalHost>? {
        return UrlDatabase.getInstance(application).functionsDatabaseDao.getLast5()
    }
}
