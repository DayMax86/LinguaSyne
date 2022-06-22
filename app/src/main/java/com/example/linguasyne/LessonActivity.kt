package com.example.linguasyne

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class LessonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        //Populate the data fields on the layout for the first term in the lesson

        findViewById<ImageView>(R.id.right_arrow_image).setOnClickListener{
            // Go to next term in the lesson
        }
        findViewById<ImageView>(R.id.left_arrow_image).setOnClickListener{
            //Go to previous term in the lesson
        }
    }
}