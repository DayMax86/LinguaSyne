package com.example.linguasyne

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField

class VocabSearch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocab_search)

        initRecyclerView()
        populateVocabSearch()


    }

    private fun initRecyclerView() {
        findViewById<RecyclerView>(R.id.vocab_search_recycler_view).apply {
            layoutManager = LinearLayoutManager(this@VocabSearch)
            adapter = VocabSearchRecyclerAdapter()
        }
    }


    private fun populateVocabSearch() {
        val ref = FirebaseFirestore.getInstance()

        val adapter = VocabSearchRecyclerAdapter()
        findViewById<RecyclerView>(R.id.vocab_search_recycler_view).adapter = adapter

        val vocab_to_display = mutableListOf<Vocab>()

        ref.collection("vocab")
            .get()
            .addOnSuccessListener { documents ->
                vocab_to_display.clear()
                Log.d("VocabSearch", "OnSuccessListener triggered!")
                for (document in documents) {
                    Log.d("VocabSearch", "${document.id} => ${document.data}")
                    val vocab_translations = document.get("translations")
                    val vocab_mnemonics = document.get("mnemonics")
                    val vocab_genders = document.get("genders")
                    val vocab_types = document.get("types")
                    val vocab = Vocab(
                        document.getField<String>("id").toString(),
                        document.getField<String>("name").toString(),
                        document.getField<Int>("unlock_level")!!.toInt(),
                        vocab_translations as List<String>,
                        vocab_mnemonics as List<String>,
                        vocab_genders as List<String>,
                        vocab_types as List<String>
                    )
                    vocab_to_display.add(vocab)
                }
                adapter.submitList(vocab_to_display
                    .sortedBy{ it.id })
                adapter.notifyDataSetChanged()

            }
            .addOnFailureListener { exception ->
                Log.d("VocabSearch", "OnFailureListener triggered!")
                Log.w("VocabSearch", "Error getting documents: ", exception)
            }
    }


    private fun performSearch(search_term: String, result_list: List<Vocab>): List<Vocab> {
        val results_to_display = result_list
            .filter { it.name.contains("$search_term") }
            .sortedBy{ it.id }
            .sortedBy { it.name }
        return results_to_display
    }


}