package com.example.linguasyne

import com.example.linguasyne.enums.Gender

class Vocab(term_name: String, term_unlock_level: Int, term_gender: Gender) : Term(term_name, term_unlock_level) {
    val gender: Gender = term_gender
    val is_part_of = arrayListOf<Term>()
    val constituent_terms = arrayListOf<Term>()



}