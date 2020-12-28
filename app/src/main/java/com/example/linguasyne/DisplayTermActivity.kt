package com.example.linguasyne

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField

class DisplayTerm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_term)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        displayVocabData()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    private fun displayVocabData() {
        if (Repository.currentVocab.size == 1) {
            val v = Repository.currentVocab[0]

            findViewById<TextView>(R.id.term_name_textbox).text =
                v.name
            findViewById<TextView>(R.id.term_unlock_level_textbox).text =
                v.unlock_level.toString()
            for (translations in v.translations) {
                findViewById<TextView>(R.id.translations_scroll_view).text =
                    (findViewById<TextView>(R.id.translations_scroll_view).text.toString()
                        .plus(translations.toString()))
            }
            for (mnemonics in v.mnemonics) {
                findViewById<TextView>(R.id.mnemonics_scroll_view).text =
                    (findViewById<TextView>(R.id.mnemonics_scroll_view).text.toString()
                        .plus(mnemonics.toString()))
            }
            findViewById<TextView>(R.id.term_search_current_level_textview).text =
                v.current_level_term.toString()
            findViewById<TextView>(R.id.term_search_next_level_textview).text =
                (v.current_level_term + 1).toString()
            findViewById<TextView>(R.id.next_review_date).text =
                v.next_review.toString()
        } else {
            Log.d(
                "DisplayTermActivity",
                "The vocab collection hasn't been filtered down to one item!!!"
            )

        }
    }


//    private fun fetchVocabData(id_tag: String): Vocab {
//        val ref = FirebaseFirestore.getInstance()
//        ref.collection("vocab")
//            .get()
//            .addOnSuccessListener { documents ->
//                Log.d("VocabSearchActivity", "OnSuccessListener triggered!")
//                for (document in documents) {
//                    if (document.getField<String>("id").toString() == id_tag) {
//                        val vocab_translations = document.get("translations")
//                        val vocab_mnemonics = document.get("mnemonics")
//                        val vocab_genders = document.get("genders")
//                        val vocab_types = document.get("types")
//
//                        vocab = Vocab(
//                            document.getField<String>("id").toString(),
//                            document.getField<String>("name").toString(),
//                            document.getField<Int>("unlock_level")!!.toInt(),
//                            vocab_translations as List<String>,
//                            vocab_mnemonics as List<String>,
//                            vocab_genders as List<String>,
//                            vocab_types as List<String>
//                        )
//                    }
//
//                }
//            }
//        return vocab
//    }

}

