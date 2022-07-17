package com.example.linguasyne

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ReviewTermActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_term)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}