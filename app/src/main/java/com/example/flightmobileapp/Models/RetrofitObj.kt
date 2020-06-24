package com.example.flightmobileapp.Models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlinx.android.synthetic.main.activity_second.*
import java.net.SocketTimeoutException


object RetrofitObj {
    private val client = OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build()
    private fun provideRetrofit(my_url:String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(my_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private interface API {
        @GET("/screenshot")
        fun getImageData(): Call<ResponseBody>
        @Headers("Content-Type: application/json")
        @POST("/api/command")
        fun sendSteeringData(@Body joystickModel: JoystickModel?): Call<ResponseBody>
    }

   // private val api : API by lazy  { provideRetrofit().create(API::class.java) }

    fun sendValsToSim(joystickModel: JoystickModel?, message:MutableLiveData<String>, my_url:String)  {
        val api : API by lazy  { provideRetrofit(my_url).create(API::class.java) }
            val response = api.sendSteeringData(joystickModel!!).execute()
        if (!response.isSuccessful || response.errorBody() != null) {
            message.postValue("unable to send values to server")
            return
        }

        return
    }
    fun getBitmapFrom(mb: MutableLiveData<Bitmap>, message:MutableLiveData<String>, my_url:String, onComplete: (Bitmap?) -> Bitmap)  {
        val api : API by lazy  { provideRetrofit(my_url).create(API::class.java) }
        val response =  api.getImageData().execute()
        if (response == null || !response.isSuccessful || response.body() == null || response.errorBody() != null) {
            if(response.code() == 500)
                message.postValue("there is a problem with the simulator. status 500")
            else if(response.code() == 502)
                message.postValue("there was a problem with the server status 502")
            else
                message.postValue("there was a problem with getting the image")
            return
        }
        val bytes = response?.body()!!.bytes()
        mb.postValue(onComplete(
            BitmapFactory.decodeByteArray(
                bytes,
                0,
                bytes.size
            )
        ))
    }
    fun tryGetImage(my_url:String) : Boolean{
        val api : API by lazy  { provideRetrofit(my_url).create(API::class.java) }

        val response = api.getImageData().execute()
        if (response == null || !response.isSuccessful || response.body() == null || response.errorBody() != null) {
            return false
        }
        return true
    }

}