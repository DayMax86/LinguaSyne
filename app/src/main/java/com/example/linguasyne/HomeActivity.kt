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

    //Ideally this should go in a separate CSV Importer class and be called from here, but that was proving difficult. Added to to-do list.
    private fun importVocabCSV() {
        val inputStream = resources.openRawResource(R.raw.test)

        csvReader().open(inputStream) {
            readAllAsSequence().forEach { row ->
                Log.d("HomeActivity", "$row")

                //Create temporary Term instance
                var term = Vocab( "",  0, Gender.NO, TermTypes.NOUN)
                //Split items in row at commas
                val row_array: List<String> = row.toString().split(",")

                //Loop through array and map values to corresponding attributes of the temporary Term object
                for (i in 0..8){
                    when (i){
                        /* id           */0 -> {term.id = row_array[0]}
                        /* name         */1 -> {term.name = row_array[1]}
                        /* unlock level */2 -> {term.unlock_level = row_array[2].toInt()}
                        /* translations */3 -> {val tr_array: List<String> = row_array[3].toString().split("/")
                                                    for (j in 0..tr_array.size-1){
                                                        term.translations.add(tr_array[j])
                                                    }
                                                }
                        /* mnemonics    */4 -> {val mn_array: List<String> = row_array[4].toString().split("/")
                                                    for (k in 0..mn_array.size-1){
                                                        term.translations.add(mn_array[k])
                                                    }
                                                }
                        /* gender       */5 -> {when (row_array[5]){
                                                        "m" -> {term.gender = Gender.M}
                                                        "f" -> {term.gender = Gender.F}
                                                        "mf" -> {term.gender = Gender.MF}
                                                        "no" -> {term.gender = Gender.NO}
                                                     }
                                                }
                        /* types        */6 -> {val t_array: List<String> = row_array[6].toString().split("/")
                                                    for (l in 0..t_array.size-1){
                                                        when (t_array[l]){
                                                            "n" -> {term.types.add(TermTypes.NOUN)}
                                                            "adj" -> {term.types.add(TermTypes.ADJ)}
                                                            "interj" -> {term.types.add(TermTypes.INTERJ)}
                                                            "adv" -> {term.types.add(TermTypes.ADV)}
                                                        }
                                                    }
                                                }
                        /* is_part_of   */7 -> {val po_array: List<String> = row_array[7].toString().split("/")
                                                    for (m in 0..po_array.size-1){
                                                        term.is_part_of_id.add(po_array[m])
                                                    }
                                                }
                        /* constituent..*/8 -> {val co_array: List<String> = row_array[8].toString().split("/")
                                                    for (n in 0..co_array.size-1){
                                                        term.constituent_terms_id.add(co_array[n])
                                                    }
                                                }
                    }

                }

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
                //Do something}
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

