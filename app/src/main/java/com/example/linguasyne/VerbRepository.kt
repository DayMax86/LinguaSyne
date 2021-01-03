package com.example.linguasyne

object VerbRepository {
    var listChange: (() -> Unit)? = null

    var allVerbs: List<Verb> = emptyList()

    var currentVerbs: List<Verb> = emptyList()
        set(value) {
            field = value
            listChange?.invoke()
        }


    fun filterVerbRepositoryByName(search_term_name: String) {
        currentVerbs = allVerbs
            .filter { it.name.contains(search_term_name.toLowerCase()) }
            .sortedBy { it.name }
    }

    fun filterVerbRepositoryById(search_term_id: String) {
        currentVerbs = allVerbs
            .filter { it.id.contains(search_term_id.toLowerCase()) }
            .sortedBy { it.id }
    }

}