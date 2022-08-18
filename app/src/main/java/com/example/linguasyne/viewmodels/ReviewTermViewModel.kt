package com.example.linguasyne.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.linguasyne.classes.RevisionSession
import com.example.linguasyne.managers.RevisionSessionManager
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.LsErrorRed
import com.example.linguasyne.ui.theme.LsPurple500

class ReviewTermViewModel : ViewModel() {

    var currentTermTitle: String? by mutableStateOf("")
    private var ctName: String = ""
    private var ctTrans: String = ""

    var userInput: String by mutableStateOf("")
    var launchSummary by mutableStateOf(false)
    var outlineColour by mutableStateOf(Color(0x3F0F0F0F))

    var summaryTotalCorrect: Int by mutableStateOf(RevisionSessionManager.current_session.totalCorrect)
    var summaryTotalIncorrect: Int by mutableStateOf(RevisionSessionManager.current_session.totalIncorrect)

    fun initiateSession() {
        updateTermTitle(RevisionSessionManager.current_session.currentStep)
    }

    private fun updateTermTitle(cs: RevisionSession.AnswerTypes) {

        ctName = RevisionSessionManager.current_session.currentTerm.name
        ctTrans = RevisionSessionManager.current_session.currentTerm.translations[0]

        currentTermTitle = when (cs) {
            RevisionSession.AnswerTypes.ENG -> {
                ctName
            }
            RevisionSession.AnswerTypes.TRANS -> {
                ctTrans
            }
        }
    }

    fun handleSubmit() {
        validateAnswer()
    }

    private fun validateAnswer() {
        //Check if the user input matches the current term's name
        if (checkAnswer()) {
            //User got the answer correct so show appropriate animation
            outlineColour = LsCorrectGreen

            //delay(1000)
            //Load the next term
            advance()
            //Clear the text box ready for the next term
            userInput = ""

        } else {
            //User got the answer wrong so show appropriate animation
            outlineColour = LsErrorRed
        }
        // Make sure the activity is displaying either the term name or translation
        updateTermTitle(RevisionSessionManager.current_session.currentStep)
    }

    fun handleInput(text: String) {
        userInput = text
        outlineColour = LsPurple500
    }

    private fun advance() {
        currentTermTitle = RevisionSessionManager.advanceSession()?.name.toString()
        if (currentTermTitle == "" || currentTermTitle == null || currentTermTitle == "null") {
            //There is no next term (reached end of list) so activity should end and summary be launched
            launchSummary = true
        }
    }

    private fun checkAnswer(): Boolean {
        val answer: String = userInput.lowercase().filter {
            !it.isWhitespace()
        }
        when (RevisionSessionManager.current_session.currentStep) {
            //Check answer according to whether it's an ENG or TRANS step being tested
            (RevisionSession.AnswerTypes.TRANS) -> {
                if (answer == RevisionSessionManager.current_session.currentTerm.name) {
                    RevisionSessionManager.current_session.currentTerm.transAnswered = true
                    return true
                }
            }
            (RevisionSession.AnswerTypes.ENG) -> {
                //Need to check for each of the translations in the list
                for (trans in RevisionSessionManager.current_session.currentTerm.translations) {
                    if (answer == trans.lowercase()) {
                        RevisionSessionManager.current_session.currentTerm.engAnswered = true
                        return true
                    }
                }
            }
            else -> {
                //
            }
        }
        RevisionSessionManager.current_session.currentTerm.answeredPerfectly = false
        return false
    }


}
