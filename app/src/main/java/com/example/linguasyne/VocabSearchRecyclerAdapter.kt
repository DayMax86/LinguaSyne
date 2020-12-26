package com.example.linguasyne

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VocabSearchRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//    inner class NumberViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView) {
//
//        fun bind(entry: PhoneNumber) {
//            containerView.type.text = entry.numberType
//            containerView.number.text = entry.number
//            containerView.setDelayedClickListener { clickListener.invoke(entry) }
//        }


    private var items: List<Vocab> = emptyList()

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


    class VocabSearchViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val iv = itemview
        fun bind(vocab: Vocab) {
            iv.findViewById<TextView>(R.id.term_search_name)
                .setText(vocab.name)
            iv.findViewById<TextView>(R.id.search_term_type_textview)
                .setText(vocab.types.toString())
            iv.findViewById<TextView>(R.id.term_search_current_level_textview)
                .setText(vocab.current_level_term.toString())
            iv.findViewById<TextView>(R.id.term_search_next_level_textview)
                .setText((vocab.current_level_term + 1).toString())
            iv.findViewById<TextView>(R.id.search_term_unlock_level_textview)
                .setText(vocab.unlock_level.toString())
            // implement progress bar
        }

    }
}