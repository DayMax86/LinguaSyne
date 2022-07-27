package com.example.linguasyne

import android.util.Log
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
        val toRemoveList: MutableList<Term> = mutableListOf()
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

        /*-----TESTING ONLY-----*/
        tempList.clear()
        for (i: Int in 0..2) {
            tempList.add(VocabRepository.allVocab[i])
        }
        /*---------------------*/

        //Sort the results (randomly by default but could be oldest first etc. etc.)
        //Set manager's current list
        current_session = RevisionSession(sortSessionBy(tempList, SortOrder.RANDOM))

    }

    fun advanceSession(): Term? {
        //This method is used to loop through the terms in the current revision session
        if (current_session.currentTerm.engAnswered && current_session.currentTerm.transAnswered) {
            //Both steps are now complete so it can be removed from the list

            val tl: MutableList<Term> = mutableListOf<Term>()
            tl.add(current_session.currentTerm)
            for (t: Term in tl) {
                current_session.sessionList.remove(t)
                Log.d("RevisionSessionManager", current_session.sessionList.size.toString())
            }
        }

        //Determine what the next term should be based on how many terms are left in the session
        val sl = current_session.sessionList
        val ct = current_session.currentTerm

        var endOfList = false
        if (sl.indexOf(ct) == (sl.size - 1)) {
            endOfList = true
        }

        swapSteps()

        if (sl.size >= 1) {
            if (endOfList) {
                current_session.currentTerm = sl.elementAt(0)
            } else {
                current_session.currentTerm = sl.elementAt(sl.indexOf(ct) + 1)
                //For now swap steps every time
            }
        } else if (sl.isEmpty()) {
            //There must be no terms left in the session so it can be ended!
            //Launch summary activity
            return null
        }

        //Return the term to display
        return current_session.currentTerm
    }

    private fun swapSteps() {
        if (current_session.currentStep == RevisionSession.AnswerTypes.ENG)
            current_session.currentStep = RevisionSession.AnswerTypes.TRANS
        else if (current_session.currentStep == RevisionSession.AnswerTypes.TRANS) {
            current_session.currentStep = RevisionSession.AnswerTypes.ENG
        }
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