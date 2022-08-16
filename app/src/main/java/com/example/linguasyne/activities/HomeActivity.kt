package com.example.linguasyne.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.linguasyne.*
import com.google.firebase.auth.FirebaseAuth
import com.example.linguasyne.databinding.HomeActivityBinding

class HomeActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = HomeActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        /* Below is a temporary fix, before release the whole app needs to be dark mode compatible */
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        findViewById<TextView>(R.id.home_term_base_text).setOnClickListener {
            val intent = Intent(this, VocabSearchActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.vocab_lesson_button).setOnClickListener {
            LessonManager.createLesson(LessonTypes.VOCAB)
            val intent = Intent(this, DisplayTerm::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.home_revision_button).setOnClickListener {
            RevisionSessionManager.createSession()
            val intent = Intent(this, ReviewTermActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.user_profile_imageview).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            val PICK_IMAGE = 1
            try {
                startActivityForResult(intent, PICK_IMAGE)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
                Toast.makeText(this, "Error launching camera", Toast.LENGTH_LONG)
            }
        }
        //Temporarily loading vocab straight away to see if async error is present
        FirebaseManager.loadVocabFromFirebase()

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            Toast.makeText(this, "Image has been selected", Toast.LENGTH_LONG)
            Log.d("ImageSelector", "Image has been selected")

            val uri = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            findViewById<ImageView>(R.id.user_profile_imageview).setImageDrawable(BitmapDrawable(bitmap))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sign_out -> {
                signOut()
            }
            R.id.menu_csv_import -> {
                CSVManager.importVocabCSV(this.applicationContext)
            }
            R.id.menu_search -> {
                val intent = Intent(this, VocabSearchActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_test_item -> {
                RevisionSessionManager.createSession()
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

