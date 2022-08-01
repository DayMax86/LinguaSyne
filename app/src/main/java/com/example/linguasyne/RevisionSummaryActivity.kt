package com.example.linguasyne

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RevisionSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revision_summary)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        findViewById<Button>(R.id.submit_button).setOnClickListener{
            this.finish()
        }

        findViewById<TextView>(R.id.total_correct_number).text = RevisionSessionManager.current_session.totalCorrect.toString()
        findViewById<TextView>(R.id.total_wrong_number).text = RevisionSessionManager.current_session.totalIncorrect.toString()
    }

}