package com.example.linguasyne.classes

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.linguasyne.enums.ReviewTimes
import com.example.linguasyne.managers.LessonTypes
import java.time.LocalDateTime
import java.time.LocalDateTime.*
import java.time.Month

open class Term(
    termId: String,
    termName: String,
    termUnlockLevel: Int,
    termTranslations: List<String>,
    termMnemonics: List<String>
) {
    constructor() : this("", "", 0, emptyList(), emptyList())

    var id: String = termId
    var name: String = termName
    var unlockLevel: Int? = termUnlockLevel
    var translations = termTranslations
    var mnemonics = termMnemonics

    open val classType: LessonTypes = LessonTypes.TERM

    var currentLevelTerm: Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
    var nextReview: Int = ReviewTimes.NOW

    var isUnlocked: Boolean = false

    var engAnswered: Boolean = false
    var transAnswered: Boolean = false

    var answeredPerfectly: Boolean = true
}

