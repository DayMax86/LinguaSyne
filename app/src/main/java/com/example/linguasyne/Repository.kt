package com.example.linguasyne

object Repository {
    var listChange: (() -> Unit)? = null

    var allVocab: List<Vocab> = emptyList()

    var currentVocab: List<Vocab> = emptyList()
        set(value) {
            field = value
            listChange?.invoke()
        }


    fun filterRepositoryByName(search_term_name: String) {
        currentVocab = allVocab
            .filter { it.name.contains(search_term_name.toLowerCase()) }
            .sortedBy { it.name }
    }

    fun filterRepositoryById(search_term_id: String) {
        currentVocab = allVocab
            .filter { it.id.contains(search_term_id.toLowerCase()) }
            .sortedBy { it.id }
    }
}