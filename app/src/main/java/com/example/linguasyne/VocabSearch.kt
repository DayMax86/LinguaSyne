package com.example.linguasyne

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
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

        findViewById<EditText>(R.id.vocab_search_input_edittext)
            .addTextChangedListener {
                performSearch(
                    findViewById<EditText>(R.id.vocab_search_input_edittext).text.toString(),
                    vocab_collection
                )
                updateAdapter()
            }

//        findViewById<ImageView>(R.id.term_search_background_rectangle)
//            .setOnClickListener {
//                val intent = Intent(this, DisplayTerm::class.java)
//                intent.putExtra(VOCAB_KEY, it.tag.toString())
//                startActivity(intent)
//            }

    }

    fun launchTermDisplay(vocab: Vocab) {
        val intent = Intent(this, DisplayTerm::class.java)
        intent.putExtra(VOCAB_KEY, vocab.id)
        startActivity(intent)
    }

    private val vocab_collection = mutableListOf<Vocab>()
    private var vocab_to_display = mutableListOf<Vocab>()
    val adapter = VocabSearchRecyclerAdapter()


    private fun initRecyclerView() {
        findViewById<RecyclerView>(R.id.vocab_search_recycler_view).apply {
            layoutManager = LinearLayoutManager(this@VocabSearch)
            adapter = VocabSearchRecyclerAdapter().apply {
                clickListener = {
                    launchTermDisplay(it)
                }
            }
        }
    }


    private fun populateVocabSearch() {
        val ref = FirebaseFirestore.getInstance()
        findViewById<RecyclerView>(R.id.vocab_search_recycler_view).adapter = adapter


        ref.collection("vocab")
            .get()
            .addOnSuccessListener { documents ->
                vocab_collection.clear()
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
                    vocab_collection.add(vocab)
                }
                adapter.submitList(vocab_collection
                    .sortedBy { it.name })
                updateAdapter()
            }
            .addOnFailureListener { exception ->
                Log.d("VocabSearch", "OnFailureListener triggered!")
                Log.w("VocabSearch", "Error getting documents: ", exception)
            }
    }

    private fun updateAdapter() {
        if (findViewById<EditText>(R.id.vocab_search_input_edittext).text.toString() == "") {
            adapter.submitList(vocab_collection)
        } else {
            adapter.submitList(vocab_to_display)
        }

        adapter.notifyDataSetChanged()
    }


    private fun performSearch(search_term: String, result_list: List<Vocab>) {
        val results = result_list
            .filter { it.name.contains(search_term) }
            .sortedBy { it.id }
            .sortedBy { it.name }
        vocab_to_display = results.toMutableList()
        updateAdapter()
    }


}