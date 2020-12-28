package com.example.linguasyne

object Repository {
    var listChange: (() -> Unit)? = null

    var currentVocab: List<Vocab> = emptyList()
        set(value) {
            field = value
            listChange?.invoke()
        }


    fun filterRepositoryByName(search_term_name: String) {
        currentVocab = currentVocab
            .filter { it.name.contains(search_term_name) }
            .sortedBy { it.name }
    }

    fun filterRepositoryById(search_term_id: String){
        currentVocab = currentVocab
            .filter { it.id.contains(search_term_id) }
            .sortedBy { it.id }
    }
}