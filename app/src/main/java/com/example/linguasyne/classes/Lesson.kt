package com.example.linguasyne.classes

import com.example.linguasyne.managers.LessonTypes

data class Lesson(val l_l: List<Term>, val l_t: LessonTypes) {
    val lesson_list = l_l
    val lesson_type = l_t

    //A lesson is a list of x terms (5 by default) which are presented to the user.
    //In a future iteration this will split into 3 screens of term, meaning, spelling (?).

}
