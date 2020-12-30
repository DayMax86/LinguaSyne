package com.example.linguasyne

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView

class VocabSearchActivity : AppCompatActivity() {

    lateinit var vocabAdapter: VocabSearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocab_search)

        vocabAdapter = VocabSearchRecyclerAdapter().apply {
            clickListener = {
                Repository.filterRepositoryById(it.id)
                launchTermDisplay()
            }
        }
        findViewById<RecyclerView>(R.id.vocab_search_recycler_view).adapter = vocabAdapter

        Repository.listChange = {
            updateAdapter()
        }

        FirebaseManager.loadVocabFromFirebase()

        findViewById<EditText>(R.id.vocab_search_input_edittext)
            .addTextChangedListener {
                Repository.filterRepositoryByName(
                    findViewById<EditText>(R.id.vocab_search_input_edittext).text.toString()
                )
                //listchange on repository does this on its own
                // updateAdapter()
            }
    }

    private fun launchTermDisplay() {
        val intent = Intent(this, DisplayTerm::class.java)
        startActivity(intent)
    }

    private fun updateAdapter() {
        findViewById<RecyclerView>(R.id.vocab_search_recycler_view).adapter = vocabAdapter
        vocabAdapter.submitList(Repository.currentVocab
            .sortedBy { it.name })
        vocabAdapter.notifyDataSetChanged()
    }


}