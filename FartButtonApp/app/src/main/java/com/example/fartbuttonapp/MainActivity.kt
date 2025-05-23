package com.example.fartbuttonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlin.random.Random
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fartButton = findViewById<Button>(R.id.fartButton)
        val sounds = listOf(
            R.raw.fart_sound_1,
            R.raw.fart_sound_2,
            R.raw.fart_sound_3
        )

        fartButton.setOnClickListener {
            if (sounds.isNotEmpty()) {
                val randomIndex = Random.nextInt(sounds.size)
                val selectedSoundId = sounds[randomIndex]
                val selectedSoundName = resources.getResourceEntryName(selectedSoundId)
                Log.d("MainActivity", "Attempting to play sound: $selectedSoundName (ID: $selectedSoundId)")

                // Release any existing MediaPlayer
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null

                mediaPlayer = MediaPlayer.create(this@MainActivity, selectedSoundId)

                if (mediaPlayer == null) {
                    Log.e("MainActivity", "Error creating MediaPlayer for sound ID: $selectedSoundId. File might be missing or corrupt.")
                    return@setOnClickListener
                }

                mediaPlayer?.setOnCompletionListener { mp ->
                    Log.d("MainActivity", "Sound $selectedSoundName finished playing.")
                    mp.release()
                    mediaPlayer = null
                }

                mediaPlayer?.start()
                Log.d("MainActivity", "Playing sound: $selectedSoundName")
            } else {
                Log.d("MainActivity", "Sound list is empty.")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy called. Releasing MediaPlayer.")
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
