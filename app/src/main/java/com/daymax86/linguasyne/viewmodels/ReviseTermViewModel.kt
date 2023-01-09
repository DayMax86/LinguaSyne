package com.daymax86.linguasyne.viewmodels

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.daymax86.linguasyne.R
import com.daymax86.linguasyne.classes.RevisionSession
import com.daymax86.linguasyne.enums.AnimationLengths
import com.daymax86.linguasyne.enums.ComposableDestinations
import com.daymax86.linguasyne.enums.Gender
import com.daymax86.linguasyne.managers.FirebaseManager
import com.daymax86.linguasyne.managers.RevisionSessionManager
import com.daymax86.linguasyne.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class ReviseTermViewModel(
    private val navController: NavHostController,
    val mediaPlayerCorrect: MediaPlayer,
    val mediaPlayerWrong: MediaPlayer,
) : ViewModel() {

    var currentTermTitle: String? by mutableStateOf("")
    private var ctName: String = ""
    private var ctTrans: String = ""

    var userInput: String by mutableStateOf("")

    enum class TextFieldColours {
        DEFAULT,
        CORRECT,
        WRONG,
        SELECTED,
        UNSELECTED,
        ;

        val colourDefinitions: androidx.compose.ui.graphics.Color
            @Composable
            @ReadOnlyComposable
            get() = when (this) {
                DEFAULT -> MaterialTheme.colors.secondary
                CORRECT -> LsCorrectGreen
                WRONG -> LsErrorRed
                SELECTED -> MaterialTheme.colors.primary
                UNSELECTED -> MaterialTheme.colors.onBackground
            }
    }


    var textFieldOutlineColour by mutableStateOf(TextFieldColours.DEFAULT)

    var mascOutlineColour by mutableStateOf(TextFieldColours.UNSELECTED)
    var femOutlineColour by mutableStateOf(TextFieldColours.UNSELECTED)

    var selectedGender by mutableStateOf(Gender.NO)
    private var mascSelected = false
    private var femSelected = false
    private var enableGenderSelection = false

    var selectGenderTextColour by mutableStateOf(TextFieldColours.DEFAULT)
    var mascImage by mutableStateOf(R.drawable.opaquemars)
    var femImage by mutableStateOf(R.drawable.opaquevenus)
    var flagEmoji by mutableStateOf(String(Character.toChars(0x1F1EC)))

    val mascEmoji = String(Character.toChars(0x2642))
    val femEmoji = String(Character.toChars(0x2640))

    var animateCorrect: Boolean by mutableStateOf(false)
    var animateDuration: Long by mutableStateOf(AnimationLengths.ANIMATION_DURATION_SHORT)

    var blurAmount: Int by mutableStateOf(0)

    var displayTerm: Boolean by mutableStateOf(true)
    var displaySummary: Boolean by mutableStateOf(false)
    var summaryTotalCorrect: Int by mutableStateOf(0)
    var summaryTotalIncorrect: Int by mutableStateOf(0)

    var displayEndSessionWarning: Boolean by mutableStateOf(false)

    init {
        viewModelScope
            .launch {
                RevisionSessionManager.createSession(FirebaseManager.getUserVocabUnlocks())
            }.invokeOnCompletion {
                updateTermTitle(RevisionSessionManager.currentSession.currentStep)
            }
        displayTerm = true
    }

    fun onBackPressed() {
        displayEndSessionWarning = !displayEndSessionWarning
        blurAmount = if (displayEndSessionWarning) {
            5
        } else {
            0
        }
    }

    fun onEndPressed() {
        navController.navigate(ComposableDestinations.HOME)
    }

    private fun updateTermTitle(cs: RevisionSession.AnswerTypes) {

        ctName = RevisionSessionManager.currentSession.currentTerm.name
        ctTrans = RevisionSessionManager.currentSession.currentTerm.translations[0]

        when (cs) {
            RevisionSession.AnswerTypes.ENG -> {
                currentTermTitle = ctName
                enableGenderSelection = true
                flagEmoji =
                    String(Character.toChars(0x1F1EC)) + String(Character.toChars(0x1F1E7)) //UK flag emoji
            }
            RevisionSession.AnswerTypes.TRANS -> {
                currentTermTitle =
                    "$ctTrans  " + when (RevisionSessionManager.currentSession.currentTerm.gender) {
                        Gender.M -> {
                            mascEmoji
                        }
                        Gender.F -> {
                            femEmoji
                        }
                        Gender.MF -> {
                            mascEmoji + femEmoji
                        }
                        else -> {
                            ""
                        }
                    }
                //Disable gender selection for English words
                enableGenderSelection = false
                flagEmoji =
                    String(Character.toChars(0x1F1EB)) + String(Character.toChars(0x1F1F7)) //FR flag emoji
            }
        }

        if (enableGenderSelection) {
            mascImage = R.drawable.opaquemars
            femImage = R.drawable.opaquevenus
            selectGenderTextColour = TextFieldColours.DEFAULT
        } else {
            mascImage = R.drawable.alphamars
            femImage = R.drawable.alphavenus
            selectGenderTextColour = TextFieldColours.DEFAULT
        }

    }

    fun handleSubmit() {
        //Disable answer submit while correct animation is playing from last term
        if (!animateCorrect) {
            validateAnswer()
        }
    }

    fun handleMascClick() {
        if (enableGenderSelection) {
            //Toggle selection
            if (mascSelected) {
                mascSelected = false
                mascOutlineColour = TextFieldColours.UNSELECTED
            } else {
                mascSelected = true
                mascOutlineColour = TextFieldColours.SELECTED
            }
        }
    }

    fun handleFemClick() {
        if (enableGenderSelection) {
            //Toggle selection
            if (femSelected) {
                femSelected = false
                femOutlineColour = TextFieldColours.UNSELECTED
            } else {
                femSelected = true
                femOutlineColour = TextFieldColours.SELECTED
            }
        }
    }

    private fun validateAnswer() {
        viewModelScope.launch {
            try {
                //Check if the user input matches the current term's name
                if (checkAnswer()) {
                    //User got the answer correct so show appropriate animation
                    textFieldOutlineColour = TextFieldColours.CORRECT

                    if (enableGenderSelection) {
                        if (checkGender()) {
                            animateCorrect = true
                            mediaPlayerCorrect.start()
                            delay(AnimationLengths.ANIMATION_DURATION_SHORT)
                            advance()
                            animateCorrect = false
                            mediaPlayerCorrect.stop()
                            resetUi()
                        }
                    } else {
                        animateCorrect = true
                        mediaPlayerCorrect.start()
                        delay(AnimationLengths.ANIMATION_DURATION_SHORT)
                        advance()
                        animateCorrect = false
                        mediaPlayerCorrect.stop()
                        resetUi()
                    }

                } else {
                    //User got the answer wrong so show appropriate animation
                    textFieldOutlineColour = TextFieldColours.WRONG
                    mediaPlayerWrong.start()
                }

            } catch (e: Exception) {
                Log.e("ReviseTermViewModel", "$e")
            }
        }
    }

    fun onSummaryButtonPress() {
        navController.navigate(ComposableDestinations.HOME)
        displaySummary = false
    }

    private fun resetUi() {
        //Reset text box and border colour
        userInput = ""
        textFieldOutlineColour = TextFieldColours.DEFAULT
        //Reset gender borders and values
        mascSelected = false
        mascOutlineColour = TextFieldColours.UNSELECTED
        femSelected = false
        femOutlineColour = TextFieldColours.UNSELECTED

        // Make sure the activity is displaying either the term name or translation
        updateTermTitle(RevisionSessionManager.currentSession.currentStep)
    }

    fun handleInput(text: String) {
        userInput = text
        textFieldOutlineColour = TextFieldColours.DEFAULT
    }

    private suspend fun advance() {
        currentTermTitle = RevisionSessionManager.advanceSession()?.name.toString()
        if (currentTermTitle == "" || currentTermTitle == null || currentTermTitle == "null") {
            //There is no next term (reached end of list) so activity should end and summary be launched
            summaryTotalCorrect = RevisionSessionManager.currentSession.totalCorrect
            summaryTotalIncorrect = RevisionSessionManager.currentSession.totalIncorrect
            displaySummary = true
            displayTerm = false
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
                Gender.M -> mascOutlineColour = TextFieldColours.CORRECT
                Gender.F -> femOutlineColour = TextFieldColours.CORRECT
                Gender.MF -> {
                    mascOutlineColour = TextFieldColours.CORRECT
                    femOutlineColour = TextFieldColours.CORRECT
                }
                else -> {/* No gender so colours remain unchanged */
                }
            }
            return true
        }

        //If they got the gender wrong...
        else if (selectedGender != t.gender) {
            when (selectedGender) {
                Gender.M -> mascOutlineColour = TextFieldColours.WRONG
                Gender.F -> femOutlineColour = TextFieldColours.WRONG
                Gender.MF -> {
                    mascOutlineColour = TextFieldColours.WRONG
                    femOutlineColour = TextFieldColours.WRONG
                }
                else -> {/* No gender selected but the user needs to select one */
                    selectGenderTextColour = TextFieldColours.WRONG
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
                if (answer == RevisionSessionManager.currentSession.currentTerm.name.lowercase()
                        .filter {
                            !it.isWhitespace()
                        }
                ) {
                    RevisionSessionManager.currentSession.currentTerm.transAnswered = true
                    return true
                }
            }
            (RevisionSession.AnswerTypes.ENG) -> {
                //Need to check for each of the translations in the list
                for (trans in RevisionSessionManager.currentSession.currentTerm.translations) {
                    if (answer == trans.lowercase().filter {
                            !it.isWhitespace()
                        }) {
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
