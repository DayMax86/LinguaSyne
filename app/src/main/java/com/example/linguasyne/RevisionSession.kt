package com.example.linguasyne

class RevisionSession(var session_list: List<Term>) {
    var sessionList = session_list
    lateinit var currentTerm: Term

    lateinit var currentStep: AnswerTypes
    var engStepComplete: Boolean = false
    var transStepComplete: Boolean = false

    enum class AnswerTypes {
        ENG,
        TRANS
    }

}