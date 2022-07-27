package com.example.linguasyne

class RevisionSession(var sl: MutableList<Term>) {
    var sessionList = sl
    lateinit var currentTerm: Term

    lateinit var currentStep: AnswerTypes

    enum class AnswerTypes {
        ENG,
        TRANS
    }

}