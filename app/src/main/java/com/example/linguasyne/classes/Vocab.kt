package com.example.linguasyne.classes

import com.example.linguasyne.enums.Gender
import com.example.linguasyne.enums.TermTypes
import com.example.linguasyne.managers.LessonTypes

class Vocab(termId: String,
            termName: String,
            termUnlockLevel: Int,
            termTranslations: List<String>,
            termMnemonics: List<String>,
            termGender: String,
            termTypes: List<String>) : Term(termId,termName,termUnlockLevel,termTranslations,termMnemonics) {

    var gender: Gender = Gender.NO
    val types = mutableListOf<TermTypes>()
    val isPartOf = listOf<String>()
    val constituentTerms = listOf<String>()

    override val classType: LessonTypes = LessonTypes.VOCAB

    init {
        assignGender(termGender)
        assignTypes(termTypes)
    }



    private fun assignGender(termGender: String) {
            when (termGender.toLowerCase()) {
                "no" -> gender = Gender.NO
                "mf" -> gender = Gender.MF
                "m" -> gender = Gender.M
                "f" -> gender = Gender.F
            }

    }

    private fun assignTypes(termTypes: List<String>){
        termTypes.forEach {
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