package com.example.linguasyne.managers

import com.example.linguasyne.classes.Lesson
import com.example.linguasyne.classes.Vocab


object LessonManager {

    lateinit var currentLesson: Lesson
    var activeLesson: Boolean = false

    private const val LESSON_SIZE = 5 //5 by default

    suspend fun createLesson(onComplete: () -> Unit) {
        if (FirebaseManager.currentUser != null) {
            var tempList: List<Vocab> = emptyList()

            VocabRepository.filterByUserNotYetUnlocked()
            VocabRepository.filterByUnlockLevel(FirebaseManager.currentUser!!.level) //Null check surrounding method content so non-null asserted call is safe

            //If repository has been set to the right , create lesson object and set to current lesson
            if (!activeLesson) {
                val max: Int = minOf(LESSON_SIZE, VocabRepository.currentVocab.size - 1)
                for (i: Int in 0 until max) {
                    tempList = tempList.plus(VocabRepository.currentVocab[i])
                }

                //5 items (or the maximum number of items possible if < 5 in vocab list) added to new lesson object
                currentLesson = Lesson(tempList)
                //make sure the below is set to false again when the lesson ends.
                activeLesson = true
                onComplete()
                return

            } else {
                return
            }


        }

    }

}




