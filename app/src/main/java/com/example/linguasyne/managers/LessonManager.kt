package com.example.linguasyne.managers

import com.example.linguasyne.classes.Lesson
import com.example.linguasyne.classes.Vocab


object LessonManager {

    lateinit var currentLesson: Lesson
    var activeLesson: Boolean = false

    private const val LESSON_SIZE = 5

    suspend fun createLesson(onComplete: () -> Unit) {
        if (FirebaseManager.currentUser != null) {

            val tempList: MutableList<Vocab> = mutableListOf()

            VocabRepository.filterByUserNotYetUnlocked()
            VocabRepository.filterByUnlockLevel(FirebaseManager.currentUser!!.level) //Null check surrounding method content so non-null asserted call is safe
            var i = 0
            for (v: Vocab in VocabRepository.currentVocab) {
                //If items have been added to the temp list, create lesson object and set to current lesson
                if (!activeLesson) {
                    //5 by default
                    if (i < LESSON_SIZE && i <= VocabRepository.currentVocab.size) {
                        tempList.add(v)
                        i++
                        //VocabRepository.currentVocab.minus(v)
                    } else {
                        //5 items (or the maximum number of items possible if < 5 in vocab list) added to new lesson object
                        currentLesson = Lesson(tempList)
                        //Mark the learnt terms as unlocked for the user
                        v.isUnlocked = true
                        //make sure the below is set to false again when the lesson ends.
                        activeLesson = true
                        onComplete()
                        //need to exit out of the for loop!
                        return
                    }
                } else {
                    return
                }

            }
        }

    }

}




