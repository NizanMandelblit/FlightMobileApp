package com.example.flightmobileapp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.util.Log
import android.webkit.URLUtil
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.flightmobileapp.Models.RetrofitObj
import com.example.flightmobileapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var my_urls_data: List<LocalHost>
    private lateinit var my_vm:DataBaseViewModel
    private lateinit var binding: ActivityMainBinding
    private var num_of_localhosts:Int = 0
    private var trying_to_connect:Boolean = false

    override fun onStart() {
        super.onStart()
        var is_db_done:Boolean = false


        my_vm.my_localhosts.observe(this, Observer<List<LocalHost>>{newval->
            //my_urls_data = newval
            var counter:Int = 0
            for (lh in newval) {
                my_urls_data[counter].urlAdress = lh.urlAdress
                my_urls_data[counter].startTime = lh.startTime
                counter++
            }
            binding.button.text = my_urls_data[0].urlAdress
            binding.button2.text = my_urls_data[1].urlAdress
            binding.button3.text = my_urls_data[2].urlAdress
            binding.button4.text = my_urls_data[3].urlAdress
            binding.button5.text = my_urls_data[4].urlAdress
            num_of_localhosts = counter
            is_db_done = true
        })
        my_vm.getLocalHosts(application)




        ConnectButton.setOnClickListener() {
            val database = getSharedPreferences("database", Context.MODE_PRIVATE)
            database.edit().apply() {
                putString("url", UrlInput.text.toString())
            }.apply()
            val localhost = LocalHost(UrlInput.text.toString())
            if (num_of_localhosts < 5) {
                my_urls_data[num_of_localhosts].urlAdress = localhost.urlAdress
                my_urls_data[num_of_localhosts].startTime = localhost.startTime

            } else {
                my_urls_data[4].urlAdress = localhost.urlAdress
                my_urls_data[4].startTime = localhost.startTime
            }
            binding.button.text = UrlInput.text.toString()
            trying_to_connect = true
            checkIfUrlIsOk(UrlInput.text.toString())
        }


       // val listUrls = UrlDatabase.get(application).getDao2().getLast5()
        button.setOnClickListener() {
            if (!is_db_done)
                return@setOnClickListener
            if (my_urls_data[0].urlAdress.equals("empty"))
                return@setOnClickListener
            UrlInput.setText(my_urls_data[0].urlAdress)
        }
        button2.setOnClickListener() {
            if (!is_db_done)
                return@setOnClickListener
            if (my_urls_data[1].urlAdress.equals("empty"))
                return@setOnClickListener
            UrlInput.setText(my_urls_data[1].urlAdress)
        }
        button3.setOnClickListener() {
            if (!is_db_done)
                return@setOnClickListener
            if (my_urls_data[2].urlAdress.equals("empty"))
                return@setOnClickListener
            UrlInput.setText(my_urls_data[2].urlAdress)
        }
        button4.setOnClickListener() {
            if (!is_db_done)
                return@setOnClickListener
            if (my_urls_data[3].urlAdress.equals("empty"))
                return@setOnClickListener
            UrlInput.setText(my_urls_data[3].urlAdress)
        }
        button5.setOnClickListener() {
            if (!is_db_done)
                return@setOnClickListener
            if (my_urls_data[4].urlAdress.equals("empty"))
                return@setOnClickListener
            UrlInput.setText(my_urls_data[4].urlAdress)
        }

    }
    var flag:Boolean = false
    fun checkIfUrlIsOk(my_url:String)  {

        val temp = lifecycleScope.launch(Dispatchers.Default) {
             flag =  tryGetImage(my_url)
        }
        temp.invokeOnCompletion {
            if (flag) {
                //Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show()
                trying_to_connect = false
                val second = Intent(this, Second::class.java)
                startActivity(second)
            } else {
                //Toast.makeText(this, "worng...", Toast.LENGTH_SHORT).show()
                trying_to_connect = false

                Log.d("TAG", "failllllllll\n")

            }

        }
    }
    suspend fun tryGetImage(my_url: String) : Boolean {
        return withContext(Dispatchers.IO) {
            RetrofitObj.my_url = my_url
            try {
                return@withContext RetrofitObj.tryGetImage()

            } catch (e:IllegalArgumentException) {
                return@withContext false
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        my_vm =  ViewModelProvider(this).get(DataBaseViewModel::class.java)
        my_urls_data = listOf(LocalHost("empty"), LocalHost("empty"),
            LocalHost("empty"),LocalHost("empty"),LocalHost("empty"))
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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

