package com.example.linguasyne

import java.util.*

open class Term (term_name: String, term_unlock_level: Int){
    val id: String = UUID.randomUUID().toString()
    val name: String = term_name
    var current_level_term: Int = 0
    var next_review: Int = 0
    var unlock_level: Int = term_unlock_level
    var translations = arrayListOf<String>()
    var mnemonics = arrayListOf<String>()



}