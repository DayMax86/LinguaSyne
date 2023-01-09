package com.example.linguasyne.classes

import android.net.Uri
import android.util.Log
import com.example.linguasyne.enums.ReviewTimes
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.managers.VocabRepository
import kotlin.math.floor

class User(email: String = "") {

    var imageUri: Uri? = null
    var id: String = "" //ID is set to email address as through firebase email is guaranteed to be unique
    val email: String
    var level: Int = 1
    var streak: Int = 0 //TODO(Not yet implemented)
    var studyCountry: String = "France"
    var darkModeEnabled: Boolean = false

    init {
        this.email = email.lowercase().filter {
            !it.isWhitespace() //Make sure the email is in consistent form
        }
        generateId() //Separate method in case ID format ever needs to change
    }

    private fun generateId() {
        id = email
    }

    suspend fun levelUpCheck(userUnlocks: List<Vocab>) {
        //If user has levelled up they need to be given more lessons
        var i: Int = 0
        val threshold: Int
        userUnlocks.filter { it.unlockLevel == this.level }.forEach {
            if (it.nextReviewHours >= ReviewTimes.ONE_WEEK) {
                i++
            }
        }
        //Make level-up threshold 80% of unlocks for all levels less than current user level
        VocabRepository.filterByUnlockLevel(this.level).apply {
            threshold = floor(VocabRepository.currentVocab.size * 0.8).toInt()
        }
        if (i >= threshold) {
            levelUp()
        }
    }

    private suspend fun levelUp() {
        Log.d("User", "Levelling up!")
        this.level++
        FirebaseManager.increaseUserLevel() //Increase user level on firebase
    }

}
