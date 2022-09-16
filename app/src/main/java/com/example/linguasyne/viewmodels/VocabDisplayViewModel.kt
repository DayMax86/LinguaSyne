package com.example.linguasyne.viewmodels

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.enums.AnimationLengths
import com.example.linguasyne.enums.ComposableDestinations
import com.example.linguasyne.enums.Gender
import com.example.linguasyne.enums.TermTypes
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.managers.LessonManager
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
import java.util.Comparator

class VocabDisplayViewModel(
    private val navController: NavHostController
) : ViewModel() {

    lateinit var vSource: Sources

    var termToDisplay: Vocab by mutableStateOf(VocabRepository.currentVocab[0])

    var showDisplay: Boolean by mutableStateOf(false)

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

    var showLoadingAnimation: Boolean by mutableStateOf(false)

    fun onActivityLaunch() {
        showLoadingAnimation = true
        viewModelScope
            .launch {

                vSource = fetchTermSource()

                if (vSource == Sources.LESSON) {
                    val userFirestoreVocab: List<Vocab> = FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document("${FirebaseManager.currentUser?.email}")
                        .collection("terms")
                        .get()
                        .await()
                        .toObjects(Vocab::class.java)
                    //If this is a lesson, unlock the terms for the user and add them to their personal firebase list
                    for (term: Vocab in LessonManager.currentLesson.lessonList) {
                        //For each vocab item in the lesson list
                        if (userFirestoreVocab.binarySearch(
                                element = term,
                                comparator = compareBy<Vocab> {
                                    it.id
                                }
                            ) < 0 //The binary search returns a negative number if the element is not found
                        ) {
                            //For each vocab item in the user's firebase list
                            term.isUnlocked = true
                            addTermToUserCollection(term)
                        }
                    }
                }
                getTermData()
                showLoadingAnimation = false
            }.apply {
                showDisplay = true
            }

    }

    fun handleBackPress() {
        viewModelScope
            .launch {
                if (!showPopUpInput) {
                    FirebaseManager.loadVocabFromFirebase()
                    navController.navigate(ComposableDestinations.TERM_SEARCH)
                    onActivityEnd()
                }
            }
    }

    private fun getTermData() {
        viewModelScope
            .launch {
                fetchTermSource()
                //Need to make sure the lesson has already been created before fetching the terms
                fetchTerm()
                fetchGenderImages()
            }
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
                            .toObjects(Vocab::class.java)
                            .first { it.id == VocabRepository.currentVocab[0].id }
                    } catch (e: Exception) {
                        //User hasn't yet unlocked the term so fetch from the online repo.
                        Log.e(
                            "DisplayTermViewModel",
                            "fetchTerm error: $e"
                        )
                        termToDisplay = VocabRepository.currentVocab[0]
                    }
                }
            }

            else -> {/**/
            }

        }
    }

    private fun addTermToUserCollection(term: Vocab) {
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

    private fun addCustomData(term: Vocab, destination: TransOrMnem) {
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
        when (termToDisplay.gender) {
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


    enum class TransOrMnem {
        TRANSLATIONS,
        MNEMONICS
    }

    enum class Sources {
        LESSON,
        SEARCH
    }


    fun onActivityEnd() {
        LessonManager.activeLesson = false
    }


}