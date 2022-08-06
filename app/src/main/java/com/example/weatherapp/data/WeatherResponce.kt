package com.example.weatherapp.data

import com.google.gson.annotations.SerializedName

data class WeatherResponce(
        val name:String,
        val main:Main,
        val wind:WindInfo

)
