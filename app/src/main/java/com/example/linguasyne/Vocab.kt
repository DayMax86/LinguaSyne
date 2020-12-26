package com.example.linguasyne

import com.example.linguasyne.enums.Gender
import com.example.linguasyne.enums.TermTypes

class Vocab(term_id: String,
            term_name: String,
            term_unlock_level: Int,
            term_translations: List<String>,
            term_mnemonics: List<String>,
            term_genders: List<String>,
            term_types: List<String>) : Term(term_id,term_name,term_unlock_level,term_translations,term_mnemonics) {

    val genders = mutableListOf<Gender>()
    val types = mutableListOf<TermTypes>()
    val is_part_of = listOf<String>()
    val constituent_terms = listOf<String>()


    init {
        setGenders(term_genders)
        setTypes(term_types)
    }



    private fun setGenders(term_genders: List<String>) {
        term_genders.forEach{
            when (it.toLowerCase()) {
                "no" -> this.genders.add(Gender.NO)
                "mf" -> this.genders.add(Gender.MF)
                "m" -> this.genders.add(Gender.M)
                "f" -> this.genders.add(Gender.F)
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