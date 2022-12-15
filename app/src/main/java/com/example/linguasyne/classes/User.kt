package com.example.linguasyne.classes

import android.net.Uri
import com.example.linguasyne.enums.ReviewTimes
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.managers.VocabRepository
import kotlin.math.floor

class User(email: String = "") {

    var imageUri: Uri? = null
    var id: String = ""
    val email: String
    var level: Int = 0
    var streak: Int = 0
    var studyCountry: String = "France"

    init {
        this.email = email
        generateId()
    }

    private fun generateId() {
        id = email
    }

    suspend fun levelUpCheck(userUnlocks: List<Vocab>) {
        var i: Int = 0
        val threshold: Int
        userUnlocks.filter { it.unlockLevel == this.level }.forEach {
            if (it.nextReviewHours >= ReviewTimes.ONE_DAY) { //This has been lowered for testing - should be ONE_WEEK or perhaps greater
                i++
            }
        }
        //Make threshold 80% of unlocks for that level
        VocabRepository.filterByUnlockLevel(this.level).apply {
            threshold = floor(VocabRepository.currentVocab.size * 0.8).toInt()
        }
        if (i >= threshold) {
            levelUp()
        }
    }

    private suspend fun levelUp() {
        this.level++
        FirebaseManager.increaseUserLevel()
    }

}
