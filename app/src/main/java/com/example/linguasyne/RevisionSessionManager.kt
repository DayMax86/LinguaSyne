package com.example.linguasyne

import android.util.Log
import java.time.LocalDateTime

object RevisionSessionManager {

    var current_session: RevisionSession = RevisionSession(emptyList<Term>())

    fun createSession() {
        //Find all the terms the user has unlocked from their Firebase data
        val user: User = FirebaseManager.current_user
        val tempList: MutableList<Term> = mutableListOf<Term>()

        //Find all the terms that are unlocked on or below the user's level
        val ul: Int = user.user_level
        for (i: Int in ul downTo 0) {
            VocabRepository.filterByUnlockLevel(i)
            tempList.addAll(VocabRepository.currentVocab)
        }

        //tempList should now contain all terms unlocked on or below the user's level
        //Now go through and filter out the ones that the user hasn't unlocked yet
        val toRemoveList: MutableList<Term> = mutableListOf<Term>()
        /* TEMPORARILY REMOVED UNTIL ISUNLOCKED IS IMPLEMENTED WITH LESSONS
        for (t: Term in tempList) {
            if (!t.isUnlocked) {
                toRemoveList.add(t)
            }
        }
        for (t: Term in toRemoveList) {
            tempList.remove(t)
        }*/

        //Finally filter by review time and we're left with terms that are due for revision.
        toRemoveList.clear()
        for (t: Term in tempList) {
            if (t.next_review.isAfter(LocalDateTime.now())) {
                toRemoveList.add(t)
            }
        }

        for (t: Term in toRemoveList) {
            tempList.remove(t)
        }

        //Sort the results (randomly by default but could be oldest first etc. etc.)
        //Set manager's current list
        current_session.session_list = sortSessionBy(tempList, SortOrder.RANDOM)

        /* ----- FOR TESTING ----- */
        for (t: Term in tempList) {
            Log.d("SessionCreation", t.id + ", " + t.name)
        }
        /* ----------------------- */
    }

    private fun sortSessionBy(session: MutableList<Term>, order: SortOrder): MutableList<Term> {
        when (order) {
            SortOrder.RANDOM -> {
                session.shuffle()
            }
            SortOrder.TIME -> {
                session.sortBy { it.next_review }
            }
            else -> {/* Most likely incorrect 'order' parameter */
            }
        }
        return session
    }

    private enum class SortOrder {
        RANDOM,
        TIME
    }

}