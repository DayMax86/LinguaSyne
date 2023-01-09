package com.daymax86.linguasyne.classes

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.daymax86.linguasyne.enums.Gender
import com.daymax86.linguasyne.enums.ReviewTimes
import com.daymax86.linguasyne.enums.TermTypes
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

    var nextReviewTime: Long = 0
    var nextReviewHours: Int = 0

    init {
        assignGender(vocabGender)
        assignTypes(vocabTypes)
    }

    private fun assignGender(vocabGender: String) {
        //Convert firebase string to custom enum structure
        when (vocabGender.lowercase()) {
            "no" -> gender = Gender.NO //Some terms have no gender
            "mf" -> gender = Gender.MF //Some terms could potentially be both masculine and feminine depending on form
            "m" -> gender = Gender.M
            "f" -> gender = Gender.F
        }

    }

    private fun assignTypes(vocabTypes: List<String>) {
        //convert firebase string to custom enum structure
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
