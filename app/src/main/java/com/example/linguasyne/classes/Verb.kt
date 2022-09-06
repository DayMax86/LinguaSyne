package com.example.linguasyne.classes

class Verb(termId: String,
           termName: String,
           termUnlockLevel: Int,
           termTranslations: List<String>,
           termMnemonics: List<String>) : Term(termId,termName,termUnlockLevel,termTranslations,termMnemonics){

    //override val class_type: LessonManager.LessonTypes = LessonManager.LessonTypes.VERBS

}