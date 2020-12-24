package com.example.linguasyne

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class VocabSearch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocab_search)

        populateVocabSearch()

        val adapter = GroupAdapter<GroupieViewHolder>()
        findViewById<RecyclerView>(R.id.vocab_search_recycler_view).adapter = adapter
        adapter.notifyDataSetChanged()
    }


    private fun populateVocabSearch() {
        val ref = FirebaseFirestore.getInstance()

        val adapter = GroupAdapter<GroupieViewHolder>()
        findViewById<RecyclerView>(R.id.vocab_search_recycler_view).adapter = adapter

        val terms_to_display = mutableListOf<Term>()

        ref.collection("vocab")
                .get()
                .addOnSuccessListener { documents ->
                    Log.d("VocabSearch", "OnSuccessListener triggered!")
                    for (document in documents) {
                        Log.d("VocabSearch", "${document.id} => ${document.data}")
                        val term_translations = document.get("translations")
                        val term_mnemonics = document.get("mnemonics")
                        val term = Term(
                                document.getField<String>("id").toString(),
                                document.getField<String>("name").toString(),
                                document.getField<Int>("unlock_level")?.toInt(),
                                term_translations as List<String>,
                                term_mnemonics as List<String>)
                        terms_to_display.add(term)
                    }
                    terms_to_display
                            .sortedBy { it.id }
                            .forEach {
                                adapter.add(SearchItem(it))
                            }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.d("VocabSearch", "OnFailureListener triggered!")
                    Log.w("VocabSearch", "Error getting documents: ", exception)
                }
    }
}


//class SearchItem(val term: Term) : Item<GroupieViewHolder>() {
//    override fun getLayout(): Int {
//        return R.layout.vocab_search_row
//    }
//
//    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//        Log.d("VocabSearch", "time to bind!")
//        viewHolder.itemView.findViewById<TextView>(R.id.sample_search_textbox).text = term.id
//        viewHolder.itemView.findViewById<TextView>(R.id.sample_search_button).text = term.name
//    }
//}