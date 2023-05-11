package com.example.temporizadorpersistente

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class DataHelper (context: Context) {
    //Shared pref es para preferencias como el formato de la fecha
    private var sharedPref :SharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    private var dateFormat = SimpleDateFormat("MM/DD/YYYY HH:mm:ss", Locale.getDefault())

    private var timerCounting = false
    private var startTime: Date? = null
    private var stopTime: Date? = null

    //Inicializar variables
    init
    {
        timerCounting = sharedPref.getBoolean(COUNTING_KEY, false)

        val startString = sharedPref.getString(START_TIME_KEY, null)
        if (startString != null)
            startTime = dateFormat.parse(startString)

        val stopString = sharedPref.getString(STOP_TIME_KEY, null)
        if (stopString != null)
            stopTime = dateFormat.parse(stopString)
    }

    //startTime, stopTime, y TimerCounting son para acceder y actualizar a los valores de las variables
    fun startTime(): Date? = startTime
    fun setStartTime(date: Date?)
    {
        startTime = date
        with(sharedPref.edit())
        {
            val stringDate = if (date == null) null else dateFormat.format(date)
            putString(START_TIME_KEY,stringDate)
            apply()
        }
    }
    fun stopTime(): Date? = startTime


    //setStopTime, stopTime para actualizar los valores de las variables, manteniendo un registro
    //de cuanto tiempo lleva el cronometro

    fun setStopTime(date: Date?)
    {
        stopTime = date
        with(sharedPref.edit())
        {
            val stringDate = if (date == null) null else dateFormat.format(date)
            putString(STOP_TIME_KEY,stringDate)
            apply()
        }
    }

    fun timerCounting(): Boolean = timerCounting

    //Actualizar el cronometro sobre si esta contando o no
    fun setTimerCounting(value: Boolean)
    {
        timerCounting = value
        with(sharedPref.edit())
        {
            putBoolean(COUNTING_KEY,value)
            apply()
        }
    }

    companion object {
        const val PREFERENCES = "prefs"
        const val START_TIME_KEY = "startKey"
        const val STOP_TIME_KEY = "stopKey"
        const val COUNTING_KEY = "countingKey"

    }

}