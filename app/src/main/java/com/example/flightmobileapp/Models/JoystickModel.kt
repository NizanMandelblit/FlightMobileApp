package com.example.flightmobileapp.Models

import com.google.gson.annotations.SerializedName

data class JoystickModel(
    @SerializedName("aileron") val aileron: Float?,
    @SerializedName("rudder") val rudder:Float,
    @SerializedName("elevator") val elevator:Float,
    @SerializedName("throttle") val throttle:Float?
)