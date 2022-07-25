package com.example.linguasyne

import android.content.Intent
import android.widget.EditText

object TermDisplayManager {

    var termList: List<Term> = RevisionSessionManager.current_session.session_list
/*
    RevisionSessionManager.current_session.currentStep = RevisionSession.AnswerTypes.ENG
    RevisionSessionManager.current_session.currentTerm = termList[0]
*/

    fun loadNextTerm() {
        val t: Term? = RevisionSessionManager.advanceSession()
        if (t == null) {
            //Must be the end of the session, so launch summary activity

        }
    }

    fun checkAnswer(answer: String): Boolean { //Returns true if answer correct, false if incorrect
        // t is the user's answer passed through from the activity
        val ct: Term = RevisionSessionManager.current_session.currentTerm
        when (RevisionSessionManager.current_session.currentStep) {
            //Check answer according to whether it's an ENG or TRANS step being tested
            (RevisionSession.AnswerTypes.TRANS) -> {
                //TODO() make it so that the answer is made lowercase and whitespace-free
                if (answer == ct.name) {
                    RevisionSessionManager.current_session.transStepComplete = true
                    return true
                }
            }
            (RevisionSession.AnswerTypes.ENG) -> {
                //Need to check for each of the translations in the list
                for (trans in ct.translations) {
                    if (answer == trans) {
                        RevisionSessionManager.current_session.engStepComplete = true
                        return true
                    }
                }
            }
            else -> {
                return false
            }
        }
        return false
    }
}