package com.example.filmlistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import com.example.filmlistapp.api.MovieListResponse
import com.example.filmlistapp.api.RetrofitInstance.service

import com.example.filmlistapp.databinding.ActivityListFilmsBinding
import retrofit2.Call
import retrofit2.Response


class ListFilmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListFilmsBinding

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

    }

    private fun searchMovie(inputText: String) {
        val call = service.searchMovie("2adee9cc3aeb30e836d5bb3db4bf6f1f", inputText)
        call.enqueue(object: retrofit2.Callback<MovieListResponse>{
            override fun onResponse(
                call: Call<MovieListResponse>,
                response: Response<MovieListResponse>
            ) {
                if (response.isSuccessful){
                    val movieList = response.body()?.results
                    if (movieList != null){
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
        val call = service.getPopularMovies("2adee9cc3aeb30e836d5bb3db4bf6f1f")
        call.enqueue(object: retrofit2.Callback<MovieListResponse>{
            override fun onResponse(
                call: Call<MovieListResponse>,
                response: Response<MovieListResponse>
            ) {
                if (response.isSuccessful){
                    val movieList = response.body()?.results
                    if (movieList != null){
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