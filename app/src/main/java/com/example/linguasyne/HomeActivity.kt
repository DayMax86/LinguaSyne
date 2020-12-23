package com.example.linguasyne

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.linguasyne.enums.Gender
import com.example.linguasyne.enums.TermTypes
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.InputStream
import java.io.InputStreamReader

class HomeActivity : AppCompatActivity() {

    var all_vocab = mutableListOf<Vocab>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        //TESTING ONLY
        findViewById<Button>(R.id.logged_in_test_button).setOnClickListener{
            findViewById<Button>(R.id.logged_in_test_button).text = FirebaseAuth.getInstance().currentUser?.email.toString()
        }
        //TESTING ONLY
        findViewById<Button>(R.id.import_csv_button).setOnClickListener{
            importVocabCSV()
        }
    }

    //Ideally this should go in a separate CSV Importer class and be called from there, but that was proving difficult. Added to to-do list.
    private fun importVocabCSV() {
        val inputStream = resources.openRawResource(R.raw.test)

        csvReader().open(inputStream) {
            readAllAsSequence().forEach { row ->
                Log.d("HomeActivity", "$row")

                val term = Vocab(
                        row[0],                          //id
                        row[1],                          //name
                        row[2].toInt(),                  //unlock level
                        row[3].split("/"),    //translations
                        row[4].split("/"),    //mnemonics
                        row[5].split("/"),    //gender
                        row[6].split("/")     //types
                )
                all_vocab.add(term)
                addVocabToFirebase(term)
            }

        }

    }


    private fun addVocabToFirebase(term: Term){
        FirebaseFirestore.getInstance()
            .collection("vocab")
            .add(term)
            .addOnSuccessListener {
                Log.d("HomeActivity", "Vocab item #" + "$term.id"+ " added to firebase")
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
                //Do something
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

