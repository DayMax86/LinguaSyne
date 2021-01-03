package com.example.linguasyne


object LessonManager {

    lateinit var current_lesson: Lesson

    fun createLesson(lesson_type: LessonTypes) {
        var temp_list: List<Vocab> = emptyList()
        when (lesson_type) {
            LessonTypes.VOCAB -> {
                VocabRepository.filterVocabRepositoryByUnlockLevel(FirebaseManager.current_user.user_level)
                var i: Int = 0
                for (v: Vocab in VocabRepository.currentVocab) {
                    //If 5 items have been added to the temp list, create lesson data object and set to current lesson
                    if (i <= 5 && i <= VocabRepository.currentVocab.size) {
                        temp_list.plus(v)
                        i++
                        VocabRepository.currentVocab.minus(v)
                    }
                    else {
                        //5 items (or the maximum number of items possible if < 5 in vocab list) added to new lesson object
                        current_lesson = Lesson(temp_list, LessonTypes.VOCAB)
                        i = 0
                        return //Does this take us out of the for loop?
                    }
                }
            }
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


