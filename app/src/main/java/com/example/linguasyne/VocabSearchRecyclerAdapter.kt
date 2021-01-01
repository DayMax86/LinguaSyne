package com.example.linguasyne

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VocabSearchRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Vocab> = emptyList()
    var clickListener: ((Vocab) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VocabSearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vocab_search_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VocabSearchViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    fun submitList(list: List<Vocab>) {
        items = list
    }

    inner class VocabSearchViewHolder(val itemview: View) : RecyclerView.ViewHolder(itemview) {

        fun bind(vocab: Vocab) {
            itemview.findViewById<TextView>(R.id.term_search_name).text = vocab.name
            itemview.findViewById<TextView>(R.id.search_term_type_textview).text = vocab.types.toString()
            itemview.findViewById<TextView>(R.id.term_search_current_level_textview).text = vocab.current_level_term.toString()
            itemview.findViewById<TextView>(R.id.term_search_next_level_textview).text = (vocab.current_level_term + 1).toString()
            itemview.findViewById<TextView>(R.id.search_term_unlock_level_textview).text = vocab.unlock_level.toString()
            // implement progress bar

            itemview.findViewById<ImageView>(R.id.term_search_background_rectangle)
                .setOnClickListener {
                    clickListener?.invoke(vocab)

                }
        }
    }
}