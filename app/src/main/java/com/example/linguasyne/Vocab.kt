package com.example.linguasyne

import com.example.linguasyne.enums.Gender
import com.example.linguasyne.enums.TermTypes

class Vocab(term_name: String, term_unlock_level: Int, term_gender: Gender, term_type: TermTypes) : Term(term_name, term_unlock_level) {
    var gender: Gender = term_gender
    var is_part_of_id = arrayListOf<String>()
    var constituent_terms_id = arrayListOf<String>()
    var types = arrayListOf<TermTypes>()



}