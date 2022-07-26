package com.example.linguasyne

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.snackbar.Snackbar
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
        for (i: Int in 0..3) {
            tempList.add(VocabRepository.allVocab[i])
        }
         /*---------------------*/

        //Sort the results (randomly by default but could be oldest first etc. etc.)
        //Set manager's current list
        current_session = RevisionSession(sortSessionBy(tempList, SortOrder.RANDOM))

    }

    fun advanceSession(): Term? {
        //This method is used to loop through the terms in the current revision session
        if (current_session.engStepComplete && current_session.transStepComplete) {
            //Both steps are now complete so it can be removed from the list
            current_session.session_list.minus(current_session.currentTerm)
        }

        //Determine what the next term should be based on how many terms are left in the session
        val sl = current_session.session_list
        val ct = current_session.currentTerm
        if (sl.size >= 2 && sl.indexOf(ct) < sl.size) {
            current_session.currentTerm = sl.elementAt(sl.indexOf(ct) + 1)
            //For now swap steps every time
            swapSteps()
        } else if (sl.size == 1) {
            //Must be the last term in the review session
            //Just swap steps, no need to set current term
            swapSteps()
        } else {
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