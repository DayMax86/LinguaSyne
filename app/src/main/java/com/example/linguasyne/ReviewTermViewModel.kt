package com.example.linguasyne

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.LsErrorRed
import com.example.linguasyne.ui.theme.LsPurple500

class ReviewTermViewModel : ViewModel() {

    var currentTermTitle: String? by mutableStateOf("")
    var userInput: String by mutableStateOf("")
    var launchSummary by mutableStateOf(false)
    var outlineColour by mutableStateOf(Color(0x3F0F0F0F))

    var summaryTotalCorrect: Int by mutableStateOf(RevisionSessionManager.current_session.totalCorrect)
    var summaryTotalIncorrect: Int by mutableStateOf(RevisionSessionManager.current_session.totalIncorrect)

    fun initiateSession() {
        //Always starts with ENG but leaving it open to future change
        if (RevisionSessionManager.current_session.currentStep == RevisionSession.AnswerTypes.ENG) {
            currentTermTitle = RevisionSessionManager.current_session.currentTerm.name
        } else {
            Log.e("ReviewTermViewModel", "Starting step is not ENG!")
        }
    }

    private fun updateTermTitle() {
        if (RevisionSessionManager.current_session.currentStep == RevisionSession.AnswerTypes.ENG) {
            currentTermTitle = RevisionSessionManager.current_session.currentTerm.name
        } else if (RevisionSessionManager.current_session.currentStep == RevisionSession.AnswerTypes.TRANS) {
            currentTermTitle = RevisionSessionManager.current_session.currentTerm.translations[0]
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

            // Make sure the activity is displaying either the term name or translation
            updateTermTitle()

            //Load the next term
            if (!advance()) {
                //No more terms left so summary can be launched
                launchSummary = true
            }

        } else {
            //User got the answer wrong so show appropriate animation
            outlineColour = LsErrorRed
        }

    }

    fun handleInput(text: String) {
        userInput = text
        outlineColour = LsPurple500
    }

    private fun advance(): Boolean {
        currentTermTitle = RevisionSessionManager.advanceSession()?.name.toString()
        if (currentTermTitle == "" || currentTermTitle == null) {
            //There is no next term (reached end of list) so activity should end and summary be launched
            return false
        }
        return true
    }

    private fun checkAnswer(): Boolean {
        val answer: String = userInput.lowercase().filter {
            !it.isWhitespace()
        }
        when (RevisionSessionManager.current_session.currentStep) {
            //Check answer according to whether it's an ENG or TRANS step being tested
            (RevisionSession.AnswerTypes.TRANS) -> {
                if (answer == currentTermTitle) {
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
