package com.example.linguasyne.viewmodels

import android.util.Log
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
import com.example.linguasyne.ui.theme.LsLightTeal
import com.example.linguasyne.ui.theme.LsVocabTextBlue
import com.google.common.primitives.UnsignedBytes.toInt
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.notify
import okhttp3.internal.wait
import java.util.ArrayList
import java.util.Comparator

class VocabDisplayViewModel(
    private val navController: NavHostController,
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
    val unselectedDotColour: Color = LsLightTeal

    var animateSuccess: Boolean by mutableStateOf(false)
    val animateDuration = AnimationLengths.ANIMATION_DURATION_LONG
    var blurAmount: Int by mutableStateOf(0)

    var showLoadingAnimation: Boolean by mutableStateOf(false)

    init {
        showLoadingAnimation = true
        viewModelScope
            .launch {

                vSource = fetchTermSource()

                if (vSource == Sources.LESSON) {
                    FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document("${FirebaseManager.currentUser?.email}")
                        .collection("terms")
                        .get()
                        .await()
                        .toObjects(Vocab::class.java)
                    //If this is a lesson, unlock the terms for the user and add them to their personal firebase list
                    //The terms only appear in a lesson once (this is ensured when the lesson is created)...
                    //...so they can be added to the user's collection without checking for pre-existing terms
                    if (LessonManager.currentLesson.lessonList.isEmpty()) {
                        //Display message to the user that they have no lessons available!
                    } else {
                        for (v: Vocab in LessonManager.currentLesson.lessonList) {
                            addTermToUserCollection(v)
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
                    if (vSource == Sources.LESSON) {
                        LessonManager.activeLesson = false
                        navController.navigate(ComposableDestinations.HOME)
                    } else {
                        FirebaseManager.loadVocabFromFirebase()
                        navController.navigate(ComposableDestinations.TERM_SEARCH)
                    }
                }
            }
    }

    private fun getTermData() {
        viewModelScope
            .launch {
                fetchTermSource()
                //Need to make sure the lesson has already been created before fetching the terms
                fetchTerm()
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


    enum class TransOrMnem {
        TRANSLATIONS,
        MNEMONICS
    }

    enum class Sources {
        LESSON,
        SEARCH
    }


}
