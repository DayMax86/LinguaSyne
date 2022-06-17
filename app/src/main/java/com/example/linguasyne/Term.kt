package com.example.linguasyne

open class Term (term_id: String,
                 term_name: String,
                 term_unlock_level: Int?,
                 term_translations: List<String>,
                 term_mnemonics: List<String>){

    var id: String = term_id
    var name: String = term_name
    var unlock_level: Int? = term_unlock_level
    var translations = term_translations
    var mnemonics = term_mnemonics

    open val class_type: LessonTypes = LessonTypes.TERM

    var current_level_term: Int = 0
    var next_review: Int = 0

}

