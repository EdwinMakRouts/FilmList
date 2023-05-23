package com.example.filmlistapp.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ContextThemeWrapper
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.example.filmlistapp.BuildConfig
import com.example.filmlistapp.R
import com.example.filmlistapp.api.Movie
import com.example.filmlistapp.api.MovieListResponse
import com.example.filmlistapp.api.RetrofitInstance.service

import com.example.filmlistapp.databinding.ActivityListFilmsBinding
import retrofit2.Call
import retrofit2.Response


class ListFilmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListFilmsBinding
    private lateinit var movies: List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListFilmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPopularMovies()

        binding.searchInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val inputText = p0.toString()
                if (!inputText.equals("")){
                    searchMovie(inputText)
                } else {
                    getPopularMovies()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.information.setOnClickListener {
            val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.alert_style))
            builder.setTitle("INFORMATION")
            builder.setMessage("Project made by: Edwin Makoveev Routskaia \nAPI: themoviedb (TMDB)")
            builder.setPositiveButton("OK") { _, _ ->
                //Nothing
            }
            val alertDialog = builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
        }

        binding.list.setOnItemClickListener { adapterView, view, i, l ->
            val movie = movies.get(i)
            var intent = Intent(this, FilmDetailsActivity::class.java)
            intent.putExtra("title", movie.title)
            intent.putExtra("language", movie.original_language)
            intent.putExtra("date", movie.release_date)
            intent.putExtra("overview", movie.overview)
            intent.putExtra("path", movie.poster_path)
            startActivity(intent)
        }

    }

    private fun searchMovie(inputText: String) {
        val call = service.searchMovie(BuildConfig.API, inputText)
        call.enqueue(object: retrofit2.Callback<MovieListResponse>{
            override fun onResponse(
                call: Call<MovieListResponse>,
                response: Response<MovieListResponse>
            ) {
                if (response.isSuccessful){
                    val movieList = response.body()?.results
                    if (movieList != null){
                        movies = movieList
                        val titlesArray = movieList.map { movie -> movie.title }.toTypedArray()
                        binding.list.adapter = ArrayAdapter (this@ListFilmsActivity, android.R.layout.simple_list_item_1, titlesArray)
                    }
                }
            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                println("Failed to get popular movies")
            }

        })
    }

    private fun getPopularMovies (){
        val call = service.getPopularMovies(BuildConfig.API)
        call.enqueue(object: retrofit2.Callback<MovieListResponse>{
            override fun onResponse(
                call: Call<MovieListResponse>,
                response: Response<MovieListResponse>
            ) {
                if (response.isSuccessful){
                    val movieList = response.body()?.results
                    if (movieList != null){
                        movies = movieList
                        val titlesArray = movieList.map { movie -> movie.title }.toTypedArray()
                        binding.list.adapter = ArrayAdapter (this@ListFilmsActivity, android.R.layout.simple_list_item_1, titlesArray)
                    }
                }
            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                println("Failed to get popular movies")
            }

        })
    }

}