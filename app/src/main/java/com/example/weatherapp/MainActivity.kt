package com.example.weatherapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.weatherapp.data.WeatherResponce
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {


   private lateinit var binding :ActivityMainBinding
   private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeRequestUsingOKHTTP("iraq")

        binding.inputName.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                makeRequestUsingOKHTTP(binding.inputName.text.toString())
                true
            } else {

                false
            }
        }


    }

    private fun makeRequestUsingOKHTTP(city: String) {

        val request= Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=8d27736fcb0eafc4d723f44873b44724").build()

        client.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
               Log.i("Fail","fail:${e.message}")
            }



            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let{jsonString ->
                 val resulte=Gson().fromJson(jsonString, WeatherResponce::class.java)
                    runOnUiThread{

                     binding.apply {
                         result.text=converToCelyzy(resulte.main.temperature.toFloat())
                         tempMin.text="Temp min : ${converToCelyzy(resulte.main.temperature_min.toFloat())}"
                         tempMax.text="Temp max : ${converToCelyzy(resulte.main.temperature_max.toFloat())}"
                         feelLike.text="Feel like : ${converToCelyzy(resulte.main.feels_like.toFloat())}"
                         cityName.text=city
                         windLabelValu.text="${resulte.wind.wind_speed} m/s"
                         humidityLabelValu.text="${resulte.main.humidity}%"
                         pressureLabelValu.text="${resulte.main.pressure} hPa"

                     }
                  }
                }

            }
        })
    }

    fun converToCelyzy(fahranyt:Float): String {
        var celezy  = (fahranyt - 273.15)
        val resulteOfConvert="%.0f".format(celezy)
        return "$resulteOfConvert°C"
    }
}











