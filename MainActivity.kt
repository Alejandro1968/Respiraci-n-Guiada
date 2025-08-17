package com.example.respiracionapp

import android.media.ToneGenerator
import android.media.AudioManager
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.*

class MainActivity : AppCompatActivity() {
    private lateinit var startButton: Button
    private lateinit var textView: TextView
    private lateinit var cyclesSpinner: Spinner
    private var totalCycles = 1
    private var currentCycle = 0
    private val vibrator by lazy { getSystemService(VIBRATOR_SERVICE) as Vibrator }
    private val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.startButton)
        textView = findViewById(R.id.textView)
        cyclesSpinner = findViewById(R.id.spinner)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, (1..10).toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cyclesSpinner.adapter = adapter

        startButton.setOnClickListener {
            totalCycles = cyclesSpinner.selectedItem as Int
            currentCycle = 0
            startCycle()
        }
    }

    private fun startCycle() {
        if (currentCycle >= totalCycles) {
            textView.text = "¡Finalizado!"
            return
        }
        currentCycle++

        textView.text = "INHALA"
        Handler(Looper.getMainLooper()).postDelayed({
            vibrateAndBeep()
            textView.text = "AGUANTA"
            Handler(Looper.getMainLooper()).postDelayed({
                vibrateAndBeep()
                textView.text = "EXHALA"
                Handler(Looper.getMainLooper()).postDelayed({
                    finalBeep()
                    startCycle()
                }, 8000)
            }, 7000)
        }, 4000)
    }

    private fun vibrateAndBeep() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(150)
        }
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
    }

    private fun finalBeep() {
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 400)
    }
}
