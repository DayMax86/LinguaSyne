package com.example.linguasyne.managers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.linguasyne.classes.RevisionSession
import com.example.linguasyne.classes.Term
import com.example.linguasyne.classes.User
import com.example.linguasyne.enums.ReviewTimes
import java.time.LocalDateTime

object RevisionSessionManager {

    lateinit var current_session: RevisionSession

    @RequiresApi(Build.VERSION_CODES.O) //This is needed for the time references
    fun createSession() {

        //Find all the terms the user has unlocked from their Firebase data
        val user: User = FirebaseManager.currentUser!!
        val tempList: MutableList<Term> = mutableListOf<Term>()

        //Find all the terms that are unlocked on or below the user's level
        val ul: Int = user.level
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
            if (t.nextReview == ReviewTimes.NOW) {
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
        current_session.totalCorrect = 0
        current_session.totalIncorrect = 0

        current_session.currentStep = RevisionSession.AnswerTypes.ENG
        current_session.currentTerm = TermDisplayManager.termList[0]
    }

    fun advanceSession(): Term? { //Returns null if the session list has been exhausted
        //Has the term already had both steps completed?
        if (current_session.currentTerm.engAnswered && current_session.currentTerm.transAnswered) {
            //Both steps are now complete so it can be removed from the list
            updateSessionTotals()

            val tl: MutableList<Term> = mutableListOf<Term>()
            tl.add(current_session.currentTerm)
            for (t: Term in tl) {
                current_session.sessionList.remove(t)
            }
        }

        //Determine what the next term should be based on how many terms are left in the session
        val sl = current_session.sessionList
        val ct = current_session.currentTerm

        //Are we at the end of the list and therefore need to loop back to the start?
        var endOfList = false
        if (sl.indexOf(ct) == (sl.size - 1)) {
            endOfList = true
        }

        //Make sure to swap steps each time, so both steps can be completed
        swapSteps()

        if (sl.size >= 1) {
            if (endOfList) {
                //Loop back to start because the end of the list has been reached the first time around
                current_session.currentTerm = sl.elementAt(0)
            } else {
                //Move to the next term in the list
                current_session.currentTerm = sl.elementAt(sl.indexOf(ct) + 1)
            }
        } else if (sl.isEmpty()) {
            //There must be no terms left in the session so it can be ended and the summary screen shown
            return null
        }

        //Return the term to display, or null if the session is complete
        return current_session.currentTerm
    }

    private fun swapSteps() {
        if (current_session.currentStep == RevisionSession.AnswerTypes.ENG)
            current_session.currentStep = RevisionSession.AnswerTypes.TRANS
        else if (current_session.currentStep == RevisionSession.AnswerTypes.TRANS) {
            current_session.currentStep = RevisionSession.AnswerTypes.ENG
        }
    }

    private fun updateSessionTotals() {
        //Take number answered perfectly from sessionSize
        if (current_session.currentTerm.answeredPerfectly) {
            current_session.totalCorrect++
        } else {
            current_session.totalIncorrect++
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sortSessionBy(session: MutableList<Term>, order: SortOrder): MutableList<Term> {
        when (order) {
            SortOrder.RANDOM -> {
                session.shuffle()
            }
            SortOrder.TIME -> {
                session.sortBy { it.nextReview }
            }
        }
        return session
    }

    private enum class SortOrder {
        RANDOM,
        TIME
    }

}