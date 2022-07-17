package com.example.linguasyne

class RevisionSession(var session_list: List<Term>) {
    var sessionList = session_list
    var currentTerm: Term? = null

    var engStepComplete: Boolean = false
    var transStepComplete: Boolean = false



}