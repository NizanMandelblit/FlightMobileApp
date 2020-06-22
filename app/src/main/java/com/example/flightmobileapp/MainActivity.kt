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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private var my_urls_data: List<LocalHost>? = null
    private var my_vm:DataBaseViewModel? = null
    override fun onStart() {
        super.onStart()
        val my_vm_temp: DataBaseViewModel by lazy { ViewModelProvider(this).get(DataBaseViewModel::class.java) }

        my_vm_temp.my_localhosts.observe(this, Observer<List<LocalHost>>{newval->
            my_urls_data = newval
        })
        my_vm = my_vm_temp
        my_vm?.getLocalHosts(application)



        ConnectButton.setOnClickListener() {
            val database = getSharedPreferences("database", Context.MODE_PRIVATE)
            database.edit().apply() {
                putString("url", UrlInput.text.toString())
            }.apply()
            if (UrlInput.text.length == 0) {
                Toast.makeText(this, "You Must Fill in A valid URL", Toast.LENGTH_SHORT).show()
            } else {
                val localhost = LocalHost(UrlInput.text.toString())
                var myActualList=my_urls_data!!.toMutableList()
                val temp1=myActualList.get(4)
                myActualList.remove(temp1)
                for(x in 0 until 4){
                    val temp=myActualList.get(3)
                    myActualList.remove(temp)
                    myActualList.add(0,temp)
                }
                myActualList.add(0,localhost)
                my_urls_data=myActualList
                Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show()
                val second = Intent(this, Second::class.java)
                startActivity(second)

            }
        }

        button.setOnClickListener() {
            val listUrls = my_urls_data?.toList()
            if(listUrls == null)
                return@setOnClickListener
            if (listUrls.size >= 1) {
                UrlInput.setText(listUrls[0].urlAdress)
            }
        }
        button2.setOnClickListener() {
            val listUrls = my_urls_data?.toList()
            if(listUrls == null)
                return@setOnClickListener
            if (listUrls.size >= 2) {
                UrlInput.setText(listUrls[1].urlAdress)
            }
        }
        button3.setOnClickListener() {
            val listUrls = my_urls_data?.toList()
            if(listUrls == null)
                return@setOnClickListener
            if (listUrls.size >= 3) {
                UrlInput.setText(listUrls[2].urlAdress)
            }
        }
        button4.setOnClickListener() {
            val listUrls = my_urls_data?.toList()
            if(listUrls == null)
                return@setOnClickListener
            if (listUrls.size >= 4) {
                UrlInput.setText(listUrls[3].urlAdress)
            }
        }
        button5.setOnClickListener() {
           val listUrls = my_urls_data?.toList()
            if(listUrls == null)
                return@setOnClickListener
            if (listUrls.size >= 5) {
                UrlInput.setText(listUrls[4].urlAdress)
            }
        }
        /*
        var viewModelJob = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        uiScope.launch {
// this will execute under IO context
             my_urls_data = suspendFunction()

        }
*/
    }


    suspend fun suspendFunction(): MutableList<LocalHost>? {

        withContext(Dispatchers.IO) {
             val z = UrlDatabase.getInstance(application).functionsDatabaseDao.getLast5()
            return@withContext z
        }
        return null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onStop() {
        super.onStop()
        var myActualList=my_urls_data!!.toMutableList()
        for(localhost in myActualList){
            insertData(localhost, application).execute()
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
/*
class bringData(val application: Application) : AsyncTask<Void, Void, List<LocalHost>>() {
    override fun doInBackground(vararg params: Void?): List<LocalHost>? {
        return UrlDatabase.getInstance(application).functionsDatabaseDao.getLast5()
    }
}*/
