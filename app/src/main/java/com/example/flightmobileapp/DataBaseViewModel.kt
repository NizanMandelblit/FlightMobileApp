package com.example.flightmobileapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DataBaseViewModel : ViewModel() {
    val my_localhosts: MutableLiveData<List<LocalHost>> by lazy {
        MutableLiveData<List<LocalHost>>()
    }
    fun getLocalHosts(context: Context) {
        viewModelScope.launch (Dispatchers.Main){
            my_localhosts.value = getSuspendLocalHost(my_localhosts, context)
        }
    }
    suspend fun getSuspendLocalHost(lh:MutableLiveData<List<LocalHost>>, context: Context) : List<LocalHost>{
        return withContext(Dispatchers.IO) {
             return@withContext UrlDatabase.getInstance(context).functionsDatabaseDao.getLast5()
        }
    }
}