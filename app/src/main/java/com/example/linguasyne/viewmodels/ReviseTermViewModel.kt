package com.example.linguasyne.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.linguasyne.R
import com.example.linguasyne.classes.RevisionSession
import com.example.linguasyne.enums.AnimationLengths
import com.example.linguasyne.enums.ComposableDestinations
import com.example.linguasyne.enums.Gender
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.managers.RevisionSessionManager
import com.example.linguasyne.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class ReviseTermViewModel(
    private val navController: NavHostController
) : ViewModel() {

    var currentTermTitle: String? by mutableStateOf("")
    private var ctName: String = ""
    private var ctTrans: String = ""

    var userInput: String by mutableStateOf("")
    var textFieldOutlineColour by mutableStateOf(LsGrey)
    var mascOutlineColour by mutableStateOf(LsGrey)
    var femOutlineColour by mutableStateOf(LsGrey)

    var selectedGender by mutableStateOf(Gender.NO)
    private var mascSelected = false
    private var femSelected = false
    private var enableGenderSelection = false

    var selectGenderTextColour by mutableStateOf(LsTextBlue)
    var mascImage by mutableStateOf(R.drawable.opaquemars)
    var femImage by mutableStateOf(R.drawable.opaquevenus)

    var animateCorrect: Boolean by mutableStateOf(false)
    var animateDuration: Long by mutableStateOf(AnimationLengths.ANIMATION_DURATION_SHORT)

    var summaryTotalCorrect: Int by mutableStateOf(0)
    var summaryTotalIncorrect: Int by mutableStateOf(0)

    init {
        viewModelScope
            .launch {
                RevisionSessionManager.createSession(FirebaseManager.getUserVocabUnlocks())
                summaryTotalCorrect = RevisionSessionManager.currentSession.totalCorrect
                summaryTotalIncorrect = RevisionSessionManager.currentSession.totalIncorrect
            }.invokeOnCompletion {
                updateTermTitle(RevisionSessionManager.currentSession.currentStep)
            }
    }

    private fun updateTermTitle(cs: RevisionSession.AnswerTypes) {

        ctName = RevisionSessionManager.currentSession.currentTerm.name
        ctTrans = RevisionSessionManager.currentSession.currentTerm.translations[0]

        when (cs) {
            RevisionSession.AnswerTypes.ENG -> {
                currentTermTitle = ctName
                enableGenderSelection = true
            }
            RevisionSession.AnswerTypes.TRANS -> {
                currentTermTitle = ctTrans
                //Disable gender selection for English words
                enableGenderSelection = false
            }
        }

        if (enableGenderSelection) {
            mascImage = R.drawable.opaquemars
            femImage = R.drawable.opaquevenus
            selectGenderTextColour = LsTextBlue
        } else {
            mascImage = R.drawable.alphamars
            femImage = R.drawable.alphavenus
            selectGenderTextColour = LsGrey
        }

    }

    fun handleSubmit() {
        validateAnswer()
    }

    fun handleMascClick() {
        if (enableGenderSelection) {
            //Toggle selection
            if (mascSelected) {
                mascSelected = false
                mascOutlineColour = LsGrey
            } else if (!mascSelected) {
                mascSelected = true
                mascOutlineColour = LsDarkPurple
            }
        }
    }

    fun handleFemClick() {
        if (enableGenderSelection) {
            //Toggle selection
            if (femSelected) {
                femSelected = false
                femOutlineColour = LsGrey
            } else if (!femSelected) {
                femSelected = true
                femOutlineColour = LsDarkPurple
            }
        }
    }

    private fun validateAnswer() {
        viewModelScope.launch {
            try {
                //Check if the user input matches the current term's name
                if (checkAnswer()) {
                    //User got the answer correct so show appropriate animation
                    textFieldOutlineColour = LsCorrectGreen

                    if (enableGenderSelection) {
                        if (checkGender()) {
                            animateCorrect = true
                            delay(AnimationLengths.ANIMATION_DURATION_SHORT)
                            advance()
                            animateCorrect = false
                            resetUi()
                        }
                    } else {
                        animateCorrect = true
                        delay(AnimationLengths.ANIMATION_DURATION_SHORT)
                        advance()
                        animateCorrect = false
                        resetUi()
                    }


                } else {
                    //User got the answer wrong so show appropriate animation
                    textFieldOutlineColour = LsErrorRed
                }

            } catch (e: Exception) {

            }
        }
    }

    fun onSummaryButtonPress() {
        navController.navigate(ComposableDestinations.HOME)
    }

    private fun resetUi() {
        //Reset text box and border colour
        userInput = ""
        textFieldOutlineColour = LsGrey
        //Reset gender borders and values
        mascSelected = false
        mascOutlineColour = LsGrey
        femSelected = false
        femOutlineColour = LsGrey

        // Make sure the activity is displaying either the term name or translation
        updateTermTitle(RevisionSessionManager.currentSession.currentStep)
    }

    fun handleInput(text: String) {
        userInput = text
        textFieldOutlineColour = LsDarkPurple
    }

    private suspend fun advance() {
        currentTermTitle = RevisionSessionManager.advanceSession()?.name.toString()
        if (currentTermTitle == "" || currentTermTitle == null || currentTermTitle == "null") {
            //There is no next term (reached end of list) so activity should end and summary be launched
            navController.navigate(ComposableDestinations.SUMMARY)
        }
    }

    private fun checkGender(): Boolean {

        if (mascSelected && !femSelected) {
            // Just Masc selected
            selectedGender = Gender.M
        } else if (!mascSelected && femSelected) {
            // Just Fem selected
            selectedGender = Gender.F
        } else if (mascSelected && femSelected) {
            // Both selected
            selectedGender = Gender.MF
        } else {
            // Must have neither selected
            selectedGender = Gender.NO
        }

        // If they got the gender correct...
        val t = RevisionSessionManager.currentSession.currentTerm
        if (selectedGender == t.gender) {
            when (selectedGender) {
                Gender.M -> mascOutlineColour = LsCorrectGreen
                Gender.F -> femOutlineColour = LsCorrectGreen
                Gender.MF -> {
                    mascOutlineColour = LsCorrectGreen
                    femOutlineColour = LsCorrectGreen
                }
                else -> {/* No gender so colours remain unchanged */
                }
            }
            return true
        }

        //If the got the gender wrong...
        else if (selectedGender != t.gender) {
            when (selectedGender) {
                Gender.M -> mascOutlineColour = LsErrorRed
                Gender.F -> femOutlineColour = LsErrorRed
                Gender.MF -> {
                    mascOutlineColour = LsErrorRed
                    femOutlineColour = LsErrorRed
                }
                else -> {/* No gender but the user needs to select one */
                    selectGenderTextColour = LsErrorRed
                }
            }
            return false
        }

        return false
    }

    private fun checkAnswer(): Boolean {
        val answer: String = userInput.lowercase().filter {
            !it.isWhitespace()
        }
        when (RevisionSessionManager.currentSession.currentStep) {
            //Check answer according to whether it's an ENG or TRANS step being tested
            (RevisionSession.AnswerTypes.TRANS) -> {
                if (answer == RevisionSessionManager.currentSession.currentTerm.name) {
                    RevisionSessionManager.currentSession.currentTerm.transAnswered = true
                    return true
                }
            }
            (RevisionSession.AnswerTypes.ENG) -> {
                //Need to check for each of the translations in the list
                for (trans in RevisionSessionManager.currentSession.currentTerm.translations) {
                    if (answer == trans.lowercase()) {
                        RevisionSessionManager.currentSession.currentTerm.engAnswered = true
                        return true
                    }
                }
            }
            else -> {
                //
            }
        }
        RevisionSessionManager.currentSession.currentTerm.answeredPerfectly = false
        return false
    }

}
