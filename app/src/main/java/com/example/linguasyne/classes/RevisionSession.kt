package com.example.linguasyne.classes

data class RevisionSession(var sl: MutableList<Vocab>) {

    constructor() : this (mutableListOf())

    var sessionList = sl
    var currentTerm: Vocab = Vocab()

    var currentStep: AnswerTypes = AnswerTypes.ENG //Assign as default to avoid non-initialized error

    var totalCorrect: Int = 0
    var totalIncorrect: Int = 0

    enum class AnswerTypes {
        ENG,
        TRANS
    }

}
