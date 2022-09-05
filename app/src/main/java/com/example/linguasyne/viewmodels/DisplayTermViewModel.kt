package com.example.linguasyne.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguasyne.classes.Term
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.enums.Gender
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.managers.LessonManager
import com.example.linguasyne.managers.VocabRepository
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.LsGrey
import com.example.linguasyne.ui.theme.LsTeal200
import com.example.linguasyne.ui.theme.LsVocabTextBlue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

enum class Sources {
    LESSON,
    SEARCH
}

class DisplayTermViewModel : ViewModel() {

    lateinit var vSource: Sources

    var termToDisplay: Term by mutableStateOf(VocabRepository.currentVocab[0])
    var termsUploaded: Boolean = false

    var userTranslationInput: String by mutableStateOf("")
    var userMnemonicInput: String by mutableStateOf("")

    private var masc by mutableStateOf(false)
    var mascOutlineColour by mutableStateOf(LsGrey)
    private var fem by mutableStateOf(false)
    var femOutlineColour by mutableStateOf(LsGrey)

    val selectedNewsColour: Color = LsVocabTextBlue
    val unselectedNewsColour: Color = LsTeal200

    fun onActivityLaunch(): Sources {
        getTermData()
        if (vSource == Sources.LESSON && !termsUploaded) {
            for (term: Term in LessonManager.currentLesson.lessonList) {
                addTermToUserCollection(term)
                termsUploaded = true
            }
        }
        return vSource
    }

    private fun getTermData() {
        fetchTermSource()
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
                val firestoreRef = FirebaseFirestore.getInstance()
                viewModelScope.launch {
                    try {
                        var results: MutableList<Term>
                        firestoreRef
                            .collection("users")
                            .document("${FirebaseManager.currentUser?.email}")
                            .collection("terms")
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {

                                }
                            }

                        for (t in results) {
                            if (t.id == VocabRepository.currentVocab[0].id) {
                                termToDisplay = t
                                Log.d(
                                    "DisplayTermViewModel",
                                    "Term to display taken from user's firestore collection (User has already unlocked it)"
                                )
                            }
                        }


                    } catch (e: Exception) {
                        Log.e(
                            "DisplayTermViewModel",
                            "$e"
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
                        "$e"
                    )
                }
            }
    }

    enum class TransOrMnem {
        TRANSLATIONS,
        MNEMONICS
    }

    fun addCustomData(term: Term, destination: TransOrMnem) {
        viewModelScope
            .launch {
                try {
                    val firestoreRef = FirebaseFirestore.getInstance()
                    var inputList: List<String>
                    firestoreRef
                        .collection("users")
                        .document("${FirebaseManager.currentUser?.email}")
                        .collection("terms")
                        .document("${term.name}")
                        .get()
                        .await()
                        .apply {
                            inputList = this.get(destination.toString().lowercase()) as List<String>
                        }
                    firestoreRef
                        .collection("users")
                        .document("${FirebaseManager.currentUser?.email}")
                        .collection("terms")
                        .document("${term.name}")
                        .update(
                            destination.toString().lowercase(), inputList.plus(
                                when (destination) {
                                    TransOrMnem.TRANSLATIONS -> userTranslationInput
                                    TransOrMnem.MNEMONICS -> userMnemonicInput
                                }
                            )
                        )
                        .await()
                } catch (e: Exception) {
                    Log.e("DisplayTermViewModel", "$e")
                }
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