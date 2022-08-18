package com.example.linguasyne.managers

import android.content.Context
import android.content.Intent
import com.example.linguasyne.classes.RevisionSession
import com.example.linguasyne.classes.Term
import com.example.linguasyne.activities.RevisionSummaryActivity

object TermDisplayManager {

    var termList: List<Term> = RevisionSessionManager.current_session.sl

    fun loadNextTerm(context: Context): Boolean {
        val t: Term? = RevisionSessionManager.advanceSession()

        if (t == null) {
            //Must be the end of the session, so launch summary activity
            val intent: Intent = Intent(context, RevisionSummaryActivity::class.java)
            context.startActivity(intent)
            return false
        }
        return true
    }

    fun checkAnswer(userAnswer: String): Boolean { //Returns true if answer correct, false if incorrect
        // t is the user's answer passed through from the activity
        val ct: Term = RevisionSessionManager.current_session.currentTerm
        val answer: String = userAnswer.lowercase().filter {
            !it.isWhitespace()
        }

        when (RevisionSessionManager.current_session.currentStep) {
            //Check answer according to whether it's an ENG or TRANS step being tested
            (RevisionSession.AnswerTypes.TRANS) -> {
                if (answer == ct.name) {
                    RevisionSessionManager.current_session.currentTerm.transAnswered = true
                    return true
                }
            }
            (RevisionSession.AnswerTypes.ENG) -> {
                //Need to check for each of the translations in the list
                for (trans in ct.translations) {
                    if (answer == trans.lowercase()) {
                        RevisionSessionManager.current_session.currentTerm.engAnswered = true
                        return true
                    }
                }
            }
            else -> {
                RevisionSessionManager.current_session.currentTerm.answeredPerfectly = false
                return false
            }
        }
        RevisionSessionManager.current_session.currentTerm.answeredPerfectly = false
        return false
    }
}