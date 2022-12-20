package com.example.linguasyne.classes

import android.net.Uri
import android.util.Log
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
        Log.d("User", "Current user level: ${this.level}")
        userUnlocks.filter { it.unlockLevel == this.level }.forEach {
            Log.d("User", "Vocab item next review hours: ${it.nextReviewHours}")
            if (it.nextReviewHours >= ReviewTimes.ONE_DAY) { //This has been lowered for testing - should be ONE_WEEK or perhaps greater
                i++
            }
        }
        //Make threshold 80% of unlocks for that level
        VocabRepository.filterByUnlockLevel(this.level).apply {
            Log.d("User", "Current vocab size: ${VocabRepository.currentVocab.size}")
            threshold = floor(VocabRepository.currentVocab.size * 0.8).toInt()
            Log.d("User", "i: ${i}")
            Log.d("User", "Threshold: ${threshold}")
        }
        if (i >= threshold) {
            levelUp()
        }
    }

    private suspend fun levelUp() {
        Log.d("User", "Threshold: Levelling up!")
        this.level++
        FirebaseManager.increaseUserLevel()
    }

}
