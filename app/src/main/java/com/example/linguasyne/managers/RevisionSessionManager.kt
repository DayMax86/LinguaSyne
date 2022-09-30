package com.example.linguasyne.managers

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.linguasyne.classes.RevisionSession
import com.example.linguasyne.classes.User
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.enums.ReviewTimes
import com.example.linguasyne.enums.ReviewTimes.EIGHT_MONTHS
import com.example.linguasyne.enums.ReviewTimes.FOUR_MONTHS
import com.example.linguasyne.enums.ReviewTimes.NEVER
import com.example.linguasyne.enums.ReviewTimes.NOW
import com.example.linguasyne.enums.ReviewTimes.ONE_DAY
import com.example.linguasyne.enums.ReviewTimes.ONE_MONTH
import com.example.linguasyne.enums.ReviewTimes.ONE_WEEK
import com.example.linguasyne.enums.ReviewTimes.ONE_YEAR
import com.example.linguasyne.enums.ReviewTimes.TWO_DAYS
import com.example.linguasyne.enums.ReviewTimes.TWO_MONTHS
import com.example.linguasyne.enums.ReviewTimes.TWO_WEEKS
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object RevisionSessionManager {

    var currentSession: RevisionSession = RevisionSession()


    @RequiresApi(Build.VERSION_CODES.O)
    fun createSession(userUnlocks: List<Vocab>) {

/*        //Find all the terms the user has unlocked from their Firebase data


        //Now go through and filter out the ones that the user hasn't unlocked yet
            val toRemoveList: MutableList<Vocab> = mutableListOf()

            userUnlocks.forEach { uU ->
                tempList.removeIf {
                    it.id == uU.id
                }
            }

        //Finally filter by review time and we're left with terms that are due for revision.
        toRemoveList.clear()
        for (v: Vocab in tempList) {
            if (v.nextReview == ReviewTimes.NOW) {
                toRemoveList.add(v)
            }
        }*/

        /*-----TESTING ONLY-----*/
        val tempList: MutableList<Vocab> = mutableListOf()
        for (i: Int in 0..2) {
            tempList.add(userUnlocks[i])
        }
        /*---------------------*/

        //Sort the results (randomly by default but could be oldest first etc. etc.)
        //Set manager's current list
        //TODO() While the test above is active, tempList has replaced userUnlocks in the line below
        currentSession = RevisionSession(sortSessionBy(tempList, SortOrder.RANDOM))
        currentSession.totalCorrect = 0
        currentSession.totalIncorrect = 0

        currentSession.currentStep = RevisionSession.AnswerTypes.ENG
        currentSession.currentTerm = let { currentSession.sessionList[0] }

    }

    suspend fun advanceSession(): Vocab? { //Returns null if the session list has been exhausted
        //Has the term already had both steps completed?
        if (currentSession.currentTerm.engAnswered && currentSession.currentTerm.transAnswered) {
            //Both steps are now complete so it can be removed from the list
            updateSessionTotals()

            //If they got both steps correct first time the term's next review should be advanced.
            if (currentSession.currentTerm.answeredPerfectly) {
                advanceReviewTime(currentSession.currentTerm.nextReview)
                //Likewise it should be 'demoted' if they didn't answer perfectly.
            } else {
                decreaseReviewTime(currentSession.currentTerm.nextReview)
            }
            updateReviewTimeOnFirebase()

            val tl: MutableList<Vocab> = mutableListOf()
            tl.add(currentSession.currentTerm)
            for (t: Vocab in tl) {
                currentSession.sessionList.remove(t)
            }
        }

        //Determine what the next term should be based on how many terms are left in the session
        val sl = currentSession.sessionList
        val ct = currentSession.currentTerm

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
                currentSession.currentTerm = sl.elementAt(0)
            } else {
                //Move to the next term in the list
                currentSession.currentTerm = sl.elementAt(sl.indexOf(ct) + 1)
            }
        } else if (sl.isEmpty()) {
            //There must be no terms left in the session so it can be ended and the summary screen shown
            return null
        }

        //Return the term to display, or null if the session is complete
        return currentSession.currentTerm
    }

    private suspend fun updateReviewTimeOnFirebase() {
        try {
            val firestoreRef = FirebaseFirestore.getInstance()
            firestoreRef
                .collection("users")
                .document("${FirebaseManager.currentUser?.email}")
                .collection("terms")
                .get()
                .await()
                .first { it.getField<String>("id") == currentSession.currentTerm.id }.id
                .apply {
                    firestoreRef
                        .collection("users")
                        .document("${FirebaseManager.currentUser?.email}")
                        .collection("terms")
                        .document(this)
                        .update(
                            "nextReview",
                            currentSession.currentTerm.nextReview
                        )
                        .await()
                    Log.d(
                        "RevisionSessionManager",
                        "Successfully updated Firestore next review time: ${currentSession.currentTerm.nextReview}"
                    )
                }
        } catch (e: Exception) {
            Log.e("RevisionSessionManager", "Next review firebase update error: $e")
        }
    }

    private fun advanceReviewTime(rt: Int) {
        when (rt) {
            (NOW) -> {
                currentSession.currentTerm.nextReview = ONE_DAY
            }
            (ONE_DAY) -> {
                currentSession.currentTerm.nextReview = TWO_DAYS
            }
            (TWO_DAYS) -> {
                currentSession.currentTerm.nextReview = ONE_WEEK
            }
            (ONE_WEEK) -> {
                currentSession.currentTerm.nextReview = TWO_WEEKS
            }
            (TWO_WEEKS) -> {
                currentSession.currentTerm.nextReview = ONE_MONTH
            }
            (ONE_MONTH) -> {
                currentSession.currentTerm.nextReview = TWO_MONTHS
            }
            (TWO_MONTHS) -> {
                currentSession.currentTerm.nextReview = FOUR_MONTHS
            }
            (FOUR_MONTHS) -> {
                currentSession.currentTerm.nextReview = EIGHT_MONTHS
            }
            (EIGHT_MONTHS) -> {
                currentSession.currentTerm.nextReview = ONE_YEAR
            }
            (ONE_YEAR) -> {
                currentSession.currentTerm.nextReview = NEVER
            }
        }
    }

    private fun decreaseReviewTime(rt: Int) {
        when (rt) {
            (NOW) -> {
                currentSession.currentTerm.nextReview = NOW
            }
            (ONE_DAY) -> {
                currentSession.currentTerm.nextReview = NOW
            }
            (TWO_DAYS) -> {
                currentSession.currentTerm.nextReview = ONE_DAY
            }
            (ONE_WEEK) -> {
                currentSession.currentTerm.nextReview = TWO_DAYS
            }
            (TWO_WEEKS) -> {
                currentSession.currentTerm.nextReview = ONE_WEEK
            }
            (ONE_MONTH) -> {
                currentSession.currentTerm.nextReview = TWO_WEEKS
            }
            (TWO_MONTHS) -> {
                currentSession.currentTerm.nextReview = ONE_MONTH
            }
            (FOUR_MONTHS) -> {
                currentSession.currentTerm.nextReview = TWO_MONTHS
            }
            (EIGHT_MONTHS) -> {
                currentSession.currentTerm.nextReview = FOUR_MONTHS
            }
            (ONE_YEAR) -> {
                currentSession.currentTerm.nextReview = EIGHT_MONTHS
            }
        }
    }


    private fun swapSteps() {
        if (currentSession.currentStep == RevisionSession.AnswerTypes.ENG)
            currentSession.currentStep = RevisionSession.AnswerTypes.TRANS
        else if (currentSession.currentStep == RevisionSession.AnswerTypes.TRANS) {
            currentSession.currentStep = RevisionSession.AnswerTypes.ENG
        }
    }

    private fun updateSessionTotals() {
        //Take number answered perfectly from sessionSize
        if (currentSession.currentTerm.answeredPerfectly) {
            currentSession.totalCorrect++
        } else {
            currentSession.totalIncorrect++
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sortSessionBy(session: MutableList<Vocab>, order: SortOrder): MutableList<Vocab> {
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