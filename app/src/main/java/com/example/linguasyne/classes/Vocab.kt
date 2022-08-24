package com.example.linguasyne.classes

import com.example.linguasyne.enums.Gender
import com.example.linguasyne.enums.TermTypes
import com.example.linguasyne.managers.LessonTypes

class Vocab(term_id: String,
            term_name: String,
            term_unlock_level: Int,
            term_translations: List<String>,
            term_mnemonics: List<String>,
            term_gender: List<String>,
            term_types: List<String>) : Term(term_id,term_name,term_unlock_level,term_translations,term_mnemonics) {

    var gender: Gender = Gender.NO
    val types = mutableListOf<TermTypes>()
    val is_part_of = listOf<String>()
    val constituent_terms = listOf<String>()

    override val class_type: LessonTypes = LessonTypes.VOCAB

    init {
        setGender(term_gender)
        setTypes(term_types)
    }



    private fun setGender(term_genders: List<String>) {
        term_genders.forEach{
            when (it.toLowerCase()) {
                "no" -> gender = Gender.NO
                "mf" -> gender = Gender.MF
                "m" -> gender = Gender.M
                "f" -> gender = Gender.F
            }
        }
    }

    private fun setTypes(term_types: List<String>){
        term_types.forEach {
            when (it.toLowerCase()) {
                "n" -> this.types.add(TermTypes.NOUN)
                "adj" -> this.types.add(TermTypes.ADJ)
                "adv" -> this.types.add(TermTypes.ADV)
                "verb" -> this.types.add(TermTypes.VERB)
                "interj" -> this.types.add(TermTypes.INTERJ)
                "expr" -> this.types.add(TermTypes.EXPR)
            }
        }
    }

}