package com.example.filmlistapp.view

import android.content.Context
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.filmlistapp.R
import com.example.filmlistapp.api.Movie
import com.example.filmlistapp.databinding.ActivityFilmDetailsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class FilmDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilmDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()

        binding.btnClose.setOnClickListener {
            finish()
        }

    }

    fun initialize () {
        val extras = intent.extras

        binding.filmTitle.text = binding.filmTitle.text.toString() + " " + extras?.getString("title")
        binding.filmDate.text = binding.filmDate.text.toString() + " " + extras?.getString("date")
        binding.filmOverview.text = binding.filmOverview.text.toString() + " " + extras?.getString("overview")
        binding.filmLanguage.text = binding.filmLanguage.text.toString() + " " + extras?.getString("language")

        CoroutineScope (Dispatchers.IO).launch {
            try {
                val posterUrl = URL ("https://image.tmdb.org/t/p/w500/" + extras?.getString("path"))
                val connection = posterUrl.openConnection() as HttpURLConnection

                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)

                runOnUiThread {
                    binding.filmPoster.setImageBitmap(bitmap)
                }
            } catch (e: java.lang.Exception){
                e.printStackTrace()
            }
        }

    }
}