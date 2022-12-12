package com.example.linguasyne.managers

import com.example.linguasyne.classes.Lesson
import com.example.linguasyne.classes.Vocab

object LessonManager {

    lateinit var currentLesson: Lesson
    var activeLesson: Boolean = false

    private const val LESSON_SIZE = 5 //5 by default

    suspend fun createLesson(onComplete: () -> Unit) {
        if (FirebaseManager.currentUser != null && !activeLesson) {

            var tempList: List<Vocab> = emptyList()
            var notYetUnlocked: List<Vocab> = emptyList()

            VocabRepository.filterByUserNotYetUnlocked().apply {
                //get list of not-yet-unlocked items
                notYetUnlocked = VocabRepository.currentVocab
                //check to see if there are any other terms left to be unlocked at this level
                if (notYetUnlocked.isNotEmpty()) {
                    //filter not-yet-unlocked items by user's level
                    notYetUnlocked.filter {
                        it.unlockLevel == FirebaseManager.currentUser!!.level
                        //Null check surrounding method content so non-null asserted call is safe
                    }
                    //now take the first 5 items (or the full list if there are fewer than 5) of the list and create lesson
                    val max: Int = minOf(LESSON_SIZE, notYetUnlocked.size - 1)
                    for (i: Int in 0 until max) {
                        tempList = tempList.plus(notYetUnlocked[i])
                    }
                    //5 items (or the maximum number of items possible if < 5 in vocab list) added to new lesson object
                    currentLesson = Lesson(tempList)
                    //make sure the below is set to false again when the lesson ends.
                    activeLesson = true
                    onComplete()
                }

            }
        }

    }

}






