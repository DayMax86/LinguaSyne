package com.example.linguasyne

import android.app.UiModeManager.MODE_NIGHT_NO
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        /* Below is a temporary fix, before release the whole app needs to be darkmode compatible */
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        findViewById<TextView>(R.id.home_term_base_text).setOnClickListener{
            val intent = Intent(this, VocabSearchActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.vocab_lesson_button).setOnClickListener{
            LessonManager.createLesson(LessonTypes.VOCAB)
            val intent = Intent(this, DisplayTerm::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sign_out -> {
                signOut()
            }
            R.id.menu_test_item -> {
                CSVManager.importVocabCSV()
            }
            R.id.menu_search -> {
                val intent = Intent(this, VocabSearchActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        Log.d("HomeActivity", "User signed out")
        Log.d("HomeActivity", "Current user id: ${user?.uid}")
        //Return to login activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

