package com.example.linguasyne

import java.time.LocalDateTime

object RevisionSessionManager {

    lateinit var current_session: RevisionSession

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
        for (t: Term in tempList) {
            if (!t.isUnlocked) {
                toRemoveList.add(t)
            }
        }
        for (t: Term in toRemoveList) {
            tempList.remove(t)
        }

        //Finally filter by review time and we're left with terms that are due for revision.
        toRemoveList.clear()
        for (t: Term in tempList) {
            if (t.next_review.isBefore(LocalDateTime.now())) {
                toRemoveList.add(t)
            }
        }

        for (t: Term in toRemoveList) {
            tempList.remove(t)
        }

        //Sort the results (randomly by default but could be oldest first etc. etc.)
        //Set manager's current list
        current_session.session_list = sortSessionBy(tempList, SortOrder.RANDOM)
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