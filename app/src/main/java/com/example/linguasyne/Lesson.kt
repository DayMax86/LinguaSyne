package com.example.linguasyne

data class Lesson(val l_l: List<Term>, val l_t: LessonTypes) {
    val lesson_list = l_l
    val lesson_type = l_t
}
