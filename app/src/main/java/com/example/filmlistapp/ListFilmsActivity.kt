package com.example.filmlistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.filmlistapp.databinding.ActivityListFilmsBinding

class ListFilmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListFilmsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListFilmsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


}