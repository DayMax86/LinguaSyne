package com.example.linguasyne

import org.junit.Assert
import org.junit.Test

class RevisionSessionCreationTests {

    class ExampleUnitTest {
        @Test
        fun getAllVocabForUserLevel() {
            val user: User = FirebaseManager.current_user
            var tempList: MutableList<Term> = mutableListOf<Term>()
            //Find all the terms that are unlocked on or below the user's level
            var ul: Int = user.user_level
            for (i: Int in ul downTo 0) {
                VocabRepository.filterByUnlockLevel(i)
                tempList.addAll(VocabRepository.currentVocab)
            }
        }
    }
}




