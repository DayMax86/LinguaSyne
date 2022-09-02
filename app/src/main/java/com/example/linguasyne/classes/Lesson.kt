package com.example.linguasyne.classes

import com.example.linguasyne.managers.LessonTypes

data class Lesson(val ll: List<Term>, val lt: LessonTypes) {
    val lessonList = ll
    val lessonType = lt

    //A lesson is a list of x terms (5 by default) which are presented to the user.
    //In a future iteration this will split into 3 screens of term, meaning, spelling (?).

}
