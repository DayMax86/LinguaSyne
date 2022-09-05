package com.example.linguasyne.classes

import com.example.linguasyne.managers.LessonTypes

data class Lesson(val lessonList: List<Term>, val lessonType: LessonTypes) {

    //A lesson is a list of x terms (5 by default) which are presented to the user.
    //In a future iteration this could split into 3 screens of term, meaning, spelling (?).

}
