package com.example.linguasyne.managers

import com.example.linguasyne.classes.Vocab

object VocabRepository {
    var listChange: (() -> Unit)? = null

    var allVocab: List<Vocab> = emptyList()

    var currentVocab: List<Vocab> = emptyList()
        set(value) {
            field = value
            listChange?.invoke()
        }


    fun filterByName(search_termName: String) {
        currentVocab = allVocab
            .filter { it.name.contains(search_termName.toLowerCase()) }
            .sortedBy { it.name }
    }

    fun filterById(search_termId: String) {
        currentVocab = allVocab
            .filter { it.id.contains(search_termId.toLowerCase()) }
            .sortedBy { it.id }
    }

    fun filterByUnlockLevel(search_term_level: Int) {
        currentVocab = allVocab
            .filter { it.unlockLevel == search_term_level }
            .sortedBy { it.id }
    }

}