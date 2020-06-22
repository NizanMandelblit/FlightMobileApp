package com.example.flightmobileapp.Models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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

object RetrofitObj {
    private val client = OkHttpClient.Builder().build()
    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
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

    private val api : API by lazy  { provideRetrofit().create(API::class.java) }

    fun sendValsToSim(joystickModel: JoystickModel?) {
        val response = api.sendSteeringData(joystickModel!!).execute()

    }
    fun getBitmapFrom(mb: MutableLiveData<Bitmap>, onComplete: (Bitmap?) -> Bitmap)  {

        api.getImageData().enqueue(object : retrofit2.Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response == null || !response.isSuccessful || response.body() == null || response.errorBody() != null) {
                    return
                }

                val bytes = response?.body()!!.bytes()
                mb.value = onComplete(
                    BitmapFactory.decodeByteArray(
                        bytes,
                        0,
                        bytes.size
                    )
                )
            }
        })


    }
}