package com.example.linguasyne.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.linguasyne.classes.Term
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.enums.AnimationLengths
import com.example.linguasyne.enums.ComposableDestinations
import com.example.linguasyne.enums.Gender
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.managers.LessonManager
import com.example.linguasyne.managers.LessonTypes
import com.example.linguasyne.managers.VocabRepository
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.LsGrey
import com.example.linguasyne.ui.theme.LsTeal200
import com.example.linguasyne.ui.theme.LsVocabTextBlue
import com.google.common.primitives.UnsignedBytes.toInt
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.notify
import okhttp3.internal.wait
import java.util.ArrayList

enum class Sources {
    LESSON,
    SEARCH
}

class DisplayTermViewModel(
    private val navController: NavHostController
) : ViewModel() {

    lateinit var vSource: Sources

    var termToDisplay: Term by mutableStateOf(VocabRepository.allVocab[0])
    var termsUploaded: Boolean = false
    var showPopUpInput: Boolean by mutableStateOf(false)
    var selectedInputType: TransOrMnem by mutableStateOf(TransOrMnem.TRANSLATIONS)

    var userInput: String by mutableStateOf("")

    private var masc by mutableStateOf(false)
    var mascOutlineColour by mutableStateOf(LsGrey)
    private var fem by mutableStateOf(false)
    var femOutlineColour by mutableStateOf(LsGrey)
    var textFieldOutlineColour by mutableStateOf(LsVocabTextBlue)

    val selectedDotColour: Color = LsVocabTextBlue
    val unselectedDotColour: Color = LsTeal200

    var animateSuccess: Boolean by mutableStateOf(false)
    val animateDuration = AnimationLengths.ANIMATION_DURATION_LONG
    var blurAmount: Int by mutableStateOf(0)

    var goToSearch: Boolean by mutableStateOf(false)

    fun onActivityLaunch(): Sources {
        viewModelScope
            .launch {
                //LessonManager.createLesson(LessonTypes.VOCAB)
                if (fetchTermSource() == Sources.LESSON) {
                    if (!termsUploaded) {
                        for (term: Term in LessonManager.currentLesson.lessonList) {
                            //Lessons should only be created for terms not previously unlocked so no check needed
                            addTermToUserCollection(term)
                            termsUploaded = true
                        }
                    }
                }
                getTermData()
            }
        return vSource
    }

    fun handleBackPress() {
        viewModelScope
            .launch {
                if (!showPopUpInput) {
                    FirebaseManager.loadVocabFromFirebase()
                    navController.navigate(ComposableDestinations.TERM_SEARCH)
                    //Call the onActivityEnd function
                }
            }
    }

    private fun getTermData() {
        fetchTermSource()
        //Need to make sure the lesson has already been created before fetching the terms
        fetchTerm()
        fetchGenderImages()
    }

    private fun fetchTermSource(): Sources {
        vSource =
            if (LessonManager.activeLesson) {
                Sources.LESSON
            } else {
                Sources.SEARCH
            }
        return vSource
    }

    private fun fetchTerm() {
        when (fetchTermSource()) {
            (Sources.LESSON) -> {
                termToDisplay = LessonManager.currentLesson.lessonList.elementAt(0)
            }
            (Sources.SEARCH) -> {
                //Check if the user has already unlocked the term so that their custom data can be displayed.
                viewModelScope.launch {
                    try {
                        termToDisplay = FirebaseFirestore.getInstance()
                            .collection("users")
                            .document("${FirebaseManager.currentUser?.email}")
                            .collection("terms")
                            .get()
                            .await()
                            .toObjects(Term::class.java)
                            .first { it.id == VocabRepository.currentVocab[0].id }
                    } catch (e: Exception) {
                        Log.e(
                            "DisplayTermViewModel",
                            "fetchTerm error: $e"
                        )
                    }
                }
            }

            else -> {/**/
            }

        }
    }

    private fun addTermToUserCollection(term: Term) {
        viewModelScope
            .launch {
                try {
                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document("${FirebaseManager.currentUser?.email}")
                        .collection("terms")
                        .add(term)
                        .await()
                    Log.d(
                        "DisplayTermViewModel",
                        "Vocab item #" + term.id + " added to ${FirebaseManager.currentUser?.email} firebase"
                    )
                } catch (e: Exception) {
                    Log.e(
                        "DisplayTermViewModel",
                        "addToUserCollection error: $e"
                    )
                }
            }
    }

    enum class TransOrMnem {
        TRANSLATIONS,
        MNEMONICS
    }

    fun handleTextChange(text: String) {
        userInput = text
    }

    fun handleButtonPress() {
        addCustomData(termToDisplay, selectedInputType)
    }

    fun handleTransTextPress() {
        showPopUpInput = true
        blurAmount = 5
        selectedInputType = TransOrMnem.TRANSLATIONS
    }

    fun handleMnemTextPress() {
        showPopUpInput = true
        blurAmount = 5
        selectedInputType = TransOrMnem.MNEMONICS
    }

    private fun addCustomData(term: Term, destination: TransOrMnem) {
        viewModelScope
            .launch {
                try {
                    val firestoreRef = FirebaseFirestore.getInstance()

                    val docId =
                        firestoreRef
                            .collection("users")
                            .document("${FirebaseManager.currentUser?.email}")
                            .collection("terms")
                            .get()
                            .await()
                            .first { it.getField<String>("id") == term.id }.id


                    firestoreRef
                        .collection("users")
                        .document("${FirebaseManager.currentUser?.email}")
                        .collection("terms")
                        .document(docId)
                        .update(
                            destination.toString().lowercase(),
                            FieldValue.arrayUnion(userInput)
                        )
                        .await()


                    Log.d("DisplayTermViewModel", "Successfully updated Firestore value")

                    runSuccessAnimation()

                } catch (e: Exception) {
                    Log.e("DisplayTermViewModel", "AddCustomData error: $e")
                }
            }
    }

    fun togglePopUp() {
        showPopUpInput = !showPopUpInput
        blurAmount = if (blurAmount == 0) {
            5
        } else {
            0
        }
    }

    private fun runSuccessAnimation() {
        viewModelScope.launch {
            showPopUpInput = false
            animateSuccess = !animateSuccess
            delay(AnimationLengths.ANIMATION_DURATION_DEFAULT)
            animateSuccess = !animateSuccess
            blurAmount = 0
        }

    }

    private fun fetchGenderImages() {
        if (termToDisplay is Vocab) {
            //Go through all the genders
            when ((termToDisplay as Vocab).gender) {
                Gender.NO -> {
                    masc = false
                    mascOutlineColour = LsGrey
                    fem = false
                    femOutlineColour = LsGrey
                }
                Gender.F -> {
                    masc = false
                    mascOutlineColour = LsGrey
                    fem = true
                    femOutlineColour = LsCorrectGreen
                }
                Gender.M -> {
                    masc = true
                    mascOutlineColour = LsCorrectGreen
                    fem = false
                    femOutlineColour = LsGrey
                }
                Gender.MF -> {
                    masc = true
                    mascOutlineColour = LsCorrectGreen
                    fem = true
                    femOutlineColour = LsCorrectGreen
                }
            }
        }
    }


    fun onActivityEnd() {
        LessonManager.activeLesson = false
    }


}