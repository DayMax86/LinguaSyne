package com.daymax86.linguasyne.classes

data class RevisionSession(var sl: MutableList<Vocab>) {

    constructor() : this (mutableListOf())

    var sessionList = sl
    var currentTerm: Vocab = Vocab()

    var currentStep: AnswerTypes = AnswerTypes.ENG //Assign as default to avoid non-initialized error

    //Keep count of user's correctly/incorrectly answered terms for the summary screen
    var totalCorrect: Int = 0
    var totalIncorrect: Int = 0

    //To keep track of which step the user is on
    enum class AnswerTypes {
        ENG,
        TRANS
    }

}
