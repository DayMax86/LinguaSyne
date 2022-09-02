package com.example.linguasyne.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.linguasyne.classes.Term
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.enums.Gender
import com.example.linguasyne.managers.LessonManager
import com.example.linguasyne.managers.VocabRepository
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.LsGrey
import com.example.linguasyne.ui.theme.LsTeal200
import com.example.linguasyne.ui.theme.LsVocabTextBlue

enum class Sources {
    LESSON,
    SEARCH
}

class DisplayTermViewModel {

    lateinit var vSource: Sources

    var termToDisplay: Term by mutableStateOf(VocabRepository.currentVocab[0])

    private var masc by mutableStateOf(false)
    var mascOutlineColour by mutableStateOf(LsGrey)
    private var fem by mutableStateOf(false)
    var femOutlineColour by mutableStateOf(LsGrey)

    val selectedNewsColour: Color = LsVocabTextBlue
    val unselectedNewsColour: Color = LsTeal200

    fun onActivityLaunch(): Sources {
        getTermData()
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
                termToDisplay = LessonManager.current_lesson.lessonList.elementAt(0)
            }
            (Sources.SEARCH) -> {
                termToDisplay = VocabRepository.currentVocab.elementAt(0)
            }
            else -> {/**/
            }
        }
    }

    /*
    private fun fetchImages() {
        var firstInList = false
        var lastInList = false

        //If this is the first element in the list then the previous term navigating image should be hidden/disabled

        when (vSource) {
            (Sources.LESSON) -> {
                if (tPos == LessonManager.current_lesson.lesson_list.size - 1) {
                    lastInList = true
                } else if (tPos == 0) {
                    firstInList = true
                }


                if (firstInList) {
                    //Disable left arrow ImageView onClickListener
                    enabledLeftArrow = false
                    leftArrowImage = R.drawable.alphaleftarrow
                } else if (lastInList) {
                    //Disable right arrow ImageView onClickListener
                    enabledRightArrow = false
                    rightArrowImage = R.drawable.alpharightarrow
                } else {
                    //must be somewhere away from the ends of the list so both can be enabled
                    enabledLeftArrow = true
                    enabledRightArrow = true
                    leftArrowImage = R.drawable.opaqueleftarrow
                    rightArrowImage = R.drawable.opaquerightarrow
                }

            }

            (Sources.SEARCH) -> {
                enabledLeftArrow = false
                enabledRightArrow = false
                // Done through search so don't cycle through terms
                leftArrowImage = R.drawable.alphaleftarrow
                rightArrowImage = R.drawable.alpharightarrow
            }
            else -> {/*-----*/
            }
        }


    }
    */

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