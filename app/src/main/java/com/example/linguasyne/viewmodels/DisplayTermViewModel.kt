package com.example.linguasyne.viewmodels

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.linguasyne.R
import com.example.linguasyne.classes.Term
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.enums.Gender
import com.example.linguasyne.managers.LessonManager
import com.example.linguasyne.managers.VocabRepository

class DisplayTermViewModel {

    private var tPos: Int = 0
    private lateinit var vSource: Sources

    private enum class Sources {
        LESSON,
        SEARCH
    }

    var termToDisplay: Term by mutableStateOf(VocabRepository.currentVocab[0])

    var enabledLeftArrow by mutableStateOf(false)
    var enabledRightArrow by mutableStateOf(false)
    var leftArrowImage by mutableStateOf(R.drawable.opaqueleftarrow)
    var rightArrowImage by mutableStateOf(R.drawable.opaquerightarrow)

    private var masc by mutableStateOf(false)
    private var fem by mutableStateOf(false)

    fun onActivityLaunch() {
        getTermData()
    }

    private fun getTermData() {
        fetchTermSource()
        fetchTerm()
        fetchImages()
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
                termToDisplay = LessonManager.current_lesson.lesson_list.elementAt(tPos)
            }
            (Sources.SEARCH) -> {
                termToDisplay = VocabRepository.currentVocab.elementAt(0)
            }
            else -> {/**/
            }
        }
    }

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
            leftArrowImage = R.drawable.alphaleftarrow
            rightArrowImage = R.drawable.alpharightarrow
        }
    }

    private fun fetchGenderImages() {
        if (termToDisplay is Vocab) {
            //Go through all the genders
            when ((termToDisplay as Vocab).gender) {
                Gender.NO -> {
                    masc = false
                    fem = false
                }
                Gender.F -> {
                    masc = false
                    fem = true
                }
                Gender.M -> {
                    masc = true
                    fem = false
                }
                Gender.MF -> {
                    masc = true
                    fem = true
                }
            }
        }
    }

    fun loadPrev() {
        if (tPos > 0 && enabledLeftArrow) {
            tPos--
            fetchTerm()
        }
    }

    fun loadNext() {
        if (tPos < LessonManager.current_lesson.lesson_list.size - 1 && enabledRightArrow) {
            tPos++
            fetchTerm()
        }
    }

    fun onActivityEnd() {
        LessonManager.activeLesson = false
    }


}