package com.example.linguasyne.classes

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.linguasyne.enums.Gender
import com.example.linguasyne.enums.ReviewTimes
import com.example.linguasyne.enums.TermTypes
import com.google.firebase.Timestamp
import java.time.Month
import java.util.*

class Vocab(
    vocabId: String = "",
    vocabName: String = "",
    vocabUnlockLevel: Int = 0,
    vocabTranslations: List<String> = emptyList(),
    vocabMnemonics: List<String> = emptyList(),
    vocabGender: String = "",
    vocabTypes: List<String> = emptyList(),
) {

    var id: String = vocabId
    var name: String = vocabName
    var unlockLevel: Int = vocabUnlockLevel
    var translations = vocabTranslations
    var mnemonics = vocabMnemonics

    var gender: Gender = Gender.NO
    val types = mutableListOf<TermTypes>()

    var engAnswered: Boolean = false
    var transAnswered: Boolean = false

    var answeredPerfectly: Boolean = true

    var currentLevelTerm: Int = 0
    var nextReviewTime: Long = 0
    var nextReviewHours: Int = 0

    init {
        assignGender(vocabGender)
        assignTypes(vocabTypes)
    }

    private fun assignGender(vocabGender: String) {
        when (vocabGender.lowercase()) {
            "no" -> gender = Gender.NO
            "mf" -> gender = Gender.MF
            "m" -> gender = Gender.M
            "f" -> gender = Gender.F
        }

    }

    private fun assignTypes(vocabTypes: List<String>) {
        vocabTypes.forEach {
            when (it.lowercase()) {
                "n" -> this.types.add(TermTypes.NOUN)
                "adj" -> this.types.add(TermTypes.ADJ)
                "adv" -> this.types.add(TermTypes.ADV)
                "verb" -> this.types.add(TermTypes.VERB)
                "interj" -> this.types.add(TermTypes.INTERJ)
                "expr" -> this.types.add(TermTypes.EXPR)
            }
        }
    }

    fun reviewDue(): Boolean {
        if (this.nextReviewHours == ReviewTimes.NEVER) {
            //Make sure that the user never sees a completed term.
            return false
        } else {
            return this.nextReviewTime < Calendar.getInstance().timeInMillis
        }
    }

}
