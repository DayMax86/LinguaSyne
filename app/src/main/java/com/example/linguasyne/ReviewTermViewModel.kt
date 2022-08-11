package com.example.linguasyne

import android.content.Context
import android.content.Intent
import android.widget.EditText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.common.collect.Iterators.advance
import kotlinx.coroutines.runBlocking

class ReviewTermViewModel : ViewModel() {

    var currentTermName: String by mutableStateOf("")
    var userInput: String by mutableStateOf("")
    var launchSummary by mutableStateOf(false)

    var summaryTotalCorrect: Int by mutableStateOf(RevisionSessionManager.current_session.totalCorrect)
    var summaryTotalIncorrect: Int by mutableStateOf(RevisionSessionManager.current_session.totalIncorrect)

    fun immediate() {
        currentTermName = RevisionSessionManager.current_session.currentTerm.name
    }

    fun handleSubmit() {
        validateAnswer()
    }

    private fun validateAnswer() {
        //Check if the user input matches the current term's name
        if (checkAnswer()) {
            //User got the answer correct so show appropriate animation
            //animateAnswer(true)

            //Load the next term
            launchSummary = true
            advance()

            // Display next term
            //displayTerm()
        } else {
            //User got the answer wrong so show appropriate animation
            //animateAnswer(false)
        }

    }

    fun handleInput(text: String) {
        userInput = text
    }

    private fun advance() {
        currentTermName = RevisionSessionManager.advanceSession()?.name.toString()
        if (currentTermName == "") {
            //There is no next term (reached end of list) so activity should end and summary be launched
        }
    }

    private fun checkAnswer(): Boolean {
        val answer: String = userInput.lowercase().filter {
            !it.isWhitespace()
        }
        when (RevisionSessionManager.current_session.currentStep) {
            //Check answer according to whether it's an ENG or TRANS step being tested
            (RevisionSession.AnswerTypes.TRANS) -> {
                if (answer == currentTermName) {
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
