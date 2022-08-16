package com.example.linguasyne.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.linguasyne.FirebaseManager
import com.example.linguasyne.R
import com.example.linguasyne.VocabRepository
import com.example.linguasyne.VocabSearchRecyclerAdapter

class VocabSearchActivity : AppCompatActivity() {

    lateinit var vocabAdapter: VocabSearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocab_search)

        vocabAdapter = VocabSearchRecyclerAdapter().apply {
            clickListener = {
                VocabRepository.filterById(it.id)
                launchTermDisplay()
            }
        }
        findViewById<RecyclerView>(R.id.vocab_search_recycler_view).adapter = vocabAdapter

        VocabRepository.listChange = {
            updateAdapter()
        }

        FirebaseManager.loadVocabFromFirebase()

        findViewById<EditText>(R.id.vocab_search_input_edittext)
            .addTextChangedListener {
                VocabRepository.filterByName(
                    findViewById<EditText>(R.id.vocab_search_input_edittext).text.toString()
                )
            }
    }

    private fun launchTermDisplay() {
        val intent = Intent(this, DisplayTerm::class.java)
        startActivity(intent)
    }

    private fun updateAdapter() {
        findViewById<RecyclerView>(R.id.vocab_search_recycler_view).adapter = vocabAdapter
        vocabAdapter.submitList(
            VocabRepository.allVocab
            .sortedBy { it.name })
        vocabAdapter.notifyDataSetChanged()
    }


}