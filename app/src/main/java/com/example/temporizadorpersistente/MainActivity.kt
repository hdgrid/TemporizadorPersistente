package com.example.temporizadorpersistente

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.temporizadorpersistente.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var dataHelper: DataHelper

    private val timer = Timer()


    //Metodo que declara variables para el uso de la interfaz
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataHelper = DataHelper(applicationContext)

        binding.startButton.setOnClickListener{ startStopAction() }
        binding.resetButton.setOnClickListener{ resetAction() }

        if(dataHelper.timerCounting()) {
            startTimer()
        }
        else {
            stopTimer()
            if(dataHelper.startTime() != null && dataHelper.stopTime() != null) {
                val time = Date().time - calcRestartTime().time
                binding.tiempoAqui.text = timeStringFromLong(time)
            }

        }

        timer.scheduleAtFixedRate(TimeTask(), 0, 500)

    }

  //Clasde interna usada para actualizar el temporizador cada segundo
    private inner class TimeTask: TimerTask() {
        override fun run() {
            if(dataHelper.timerCounting()) {
                val time = Date().time - dataHelper.startTime()!!.time
                binding.tiempoAqui.text = timeStringFromLong(time)
            }
        }
    }

    //Metodo que se usa para resetear el cronometro
    private fun resetAction() {
        dataHelper.setStopTime(null)
        dataHelper.setStartTime(null)
        stopTimer()
        binding.tiempoAqui.text = timeStringFromLong( 0 )
    }

    //metodo que se usa para parar el cronometro
    private fun stopTimer() {
        dataHelper.setTimerCounting(false)
        binding.startButton.text = getString(R.string.iniciar)
    }


    //Metodo que se usa para empezar el cronometro
    private fun startTimer() {
        dataHelper.setTimerCounting(true)
        binding.startButton.text = getString(R.string.parar)
    }

    //Metodo que se usa para la accion de empezar o parar el cronometro
    private fun startStopAction() {
        if(dataHelper.timerCounting()) {
            dataHelper.setStopTime(Date())
            stopTimer()
        }
        else
        {
            if(dataHelper.stopTime() != null) {
                dataHelper.setStartTime(calcRestartTime())
                dataHelper.setStopTime(null)
            }
            else {
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    //
    private fun calcRestartTime(): Date {
        val diff = dataHelper.startTime()!!.time - dataHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }

    //Convertir tiempo desde inicio del temporizador a un string con formato hora, minuto, segundo
    private fun timeStringFromLong(ms: Long): String? {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String
    {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}