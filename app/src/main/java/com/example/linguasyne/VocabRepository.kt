package com.example.linguasyne

object VocabRepository {
    var listChange: (() -> Unit)? = null

    var allVocab: List<Vocab> = emptyList()

    var currentVocab: List<Vocab> = emptyList()
        set(value) {
            field = value
            listChange?.invoke()
        }

    var activeLesson: Boolean = false


    fun filterVocabRepositoryByName(search_term_name: String) {
        currentVocab = allVocab
            .filter { it.name.contains(search_term_name.toLowerCase()) }
            .sortedBy { it.name }
    }

    fun filterVocabRepositoryById(search_term_id: String) {
        currentVocab = allVocab
            .filter { it.id.contains(search_term_id.toLowerCase()) }
            .sortedBy { it.id }
    }

    fun filterVocabRepositoryByUnlockLevel(search_term_level: Int) {
        currentVocab = allVocab
            .filter { it.unlock_level == search_term_level }
            .sortedBy { it.id }
    }

}