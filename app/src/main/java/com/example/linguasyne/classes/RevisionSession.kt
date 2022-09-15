package com.example.linguasyne.classes

class RevisionSession(var sl: MutableList<Vocab>) {
    var sessionList = sl
    lateinit var currentTerm: Vocab

    lateinit var currentStep: AnswerTypes

    var totalCorrect: Int = 0
    var totalIncorrect: Int = 0

    enum class AnswerTypes {
        ENG,
        TRANS
    }

}