package com.example.linguasyne.managers

import com.example.linguasyne.classes.Lesson
import com.example.linguasyne.classes.Vocab


object LessonManager {

    lateinit var current_lesson: Lesson
    var activeLesson: Boolean = false

    fun createLesson(lesson_type: LessonTypes) {
        val temp_list: MutableList<Vocab> = mutableListOf()
        when (lesson_type) {
            LessonTypes.VOCAB -> {
                VocabRepository.filterByUnlockLevel(FirebaseManager.current_user.user_level)
                var i = 0
                for (v: Vocab in VocabRepository.currentVocab) {
                    //If 5 items have been added to the temp list, create lesson data object and set to current lesson
                    if (!activeLesson) {
                        //5 by default but this shouldn't be hardcoded!!
                        if (i < 5 && i <= VocabRepository.currentVocab.size) {
                            temp_list.add(v)
                            i++
                            //VocabRepository.currentVocab.minus(v)
                        } else {
                            //5 items (or the maximum number of items possible if < 5 in vocab list) added to new lesson object
                            current_lesson = Lesson(temp_list, LessonTypes.VOCAB)
                            i = 0
                            //Mark the learnt terms as unlocked for the user
                            v.isUnlocked = true
                            //make sure the below is set to false again when the lesson ends.
                            activeLesson = true
                            //need to exit out of the for loop!
                            return
                        }
                    } else {
                        return
                    }

                }
            }
            //Implement other lesson types here.
            else -> {}
        }
        //The manager's current lesson has now been set, so HomeActivity can launch the appropriate activity which will display the right list
    }

}


public enum class LessonTypes {
    VOCAB,
    VERBS,
    PHRASES,
    TERM
}

