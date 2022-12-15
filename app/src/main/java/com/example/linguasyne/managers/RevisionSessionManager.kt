package com.example.linguasyne.managers

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.linguasyne.classes.RevisionSession
import com.example.linguasyne.classes.Vocab
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
import com.example.linguasyne.enums.TermTypes
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.tasks.await
import java.sql.Date
import java.time.LocalDate

object RevisionSessionManager {

    var currentSession: RevisionSession = RevisionSession()


    @RequiresApi(Build.VERSION_CODES.O)
    fun createSession(userUnlocks: List<Vocab>) {

        //Now go through and filter user unlocks by review time
        val tempList: MutableList<Vocab> = mutableListOf()
        for (i: Int in userUnlocks.indices) {
            //Filter by review time
            if (userUnlocks[i].nextReviewHours == NOW || userUnlocks[i].reviewDue()) {
                tempList.add(userUnlocks[i])
            }
        }


        //Sort the results (randomly by default but could be oldest first etc. etc.)
        //Set manager's current list
        currentSession = RevisionSession(sortSessionBy(tempList, SortOrder.RANDOM))
        currentSession.totalCorrect = 0
        currentSession.totalIncorrect = 0

        currentSession.currentStep = RevisionSession.AnswerTypes.ENG
        currentSession.currentTerm = let { currentSession.sessionList[0] }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun advanceSession(): Vocab? { //Returns null if the session list has been exhausted
        //Has the term already had both steps completed?
        if (currentSession.currentTerm.engAnswered && currentSession.currentTerm.transAnswered) {
            //Both steps are now complete so it can be removed from the list
            updateSessionTotals()

            //If they got both steps correct first time the term's next review should be advanced.
            if (currentSession.currentTerm.answeredPerfectly) {

                updateReviewTimeOnFirebase(
                    currentSession.currentTerm,
                    convertNextReviewHoursToTimestamp(
                        advanceReviewTime(currentSession.currentTerm.nextReviewHours)
                    )
                )
                //Likewise it should be 'demoted' if they didn't answer perfectly.
            } else {
                decreaseReviewTime(currentSession.currentTerm.nextReviewHours)
            }

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

        if (sl.size >= 1) {
            if (endOfList) {
                //Loop back to start because the end of the list has been reached the first time around
                currentSession.currentTerm = sl.elementAt(0)
            } else {
                //Move to the next term in the list
                currentSession.currentTerm = sl.elementAt(sl.indexOf(ct) + 1)
            }

            when (currentSession.currentStep) {
                RevisionSession.AnswerTypes.ENG -> {
                    //Make sure to swap steps (if necessary), so both steps can be completed
                    if (currentSession.currentTerm.engAnswered) {
                        swapSteps()
                    }
                }

                (RevisionSession.AnswerTypes.TRANS) -> {
                    //Make sure to swap steps (if necessary), so both steps can be completed
                    if (currentSession.currentTerm.transAnswered) {
                        swapSteps()
                    }
                }

                else -> {/**/
                }
            }

        } else {
            //There must be no terms left in the session so it can be ended and the summary screen shown
            return null
        }

        //Return the term to display, or null if the session is complete
        return currentSession.currentTerm
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun updateReviewTimeOnFirebase(term: Vocab, nextReviewTimestamp: Date?) {
        try {
            val firestoreRef = FirebaseFirestore.getInstance()
            firestoreRef
                .collection("users")
                .document("${FirebaseManager.currentUser?.email}")
                .collection("terms")
                .get()
                .await()
                .apply {
                    this.documents.forEach {
                        if (it.getField<String>("id") == term.id) {
                            firestoreRef
                                .collection("users")
                                .document("${FirebaseManager.currentUser?.email}")
                                .collection("terms")
                                .document(it.id)
                                .update(
                                    "nextReviewTime",
                                    nextReviewTimestamp,
                                    "nextReviewHours",
                                    term.nextReviewHours
                                ).apply {
                                    Log.d(
                                        "RevisionSessionManager",
                                        "Successfully updated Firestore next review time: ${currentSession.currentTerm.nextReviewHours}"
                                    )
                                }
                        }
                    }
                }
        } catch (e: Exception) {
            Log.e("RevisionSessionManager", "Next review firebase update error: $e")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertNextReviewHoursToTimestamp(hours: Int): Date? {
        return Date.valueOf(LocalDate.now().plusDays((hours / 24).toLong()).toString())
    }

    private fun advanceReviewTime(rt: Int): Int {
        when (rt) {
            (NOW) -> {
                currentSession.currentTerm.nextReviewHours = ONE_DAY
            }
            (ONE_DAY) -> {
                currentSession.currentTerm.nextReviewHours = TWO_DAYS
            }
            (TWO_DAYS) -> {
                currentSession.currentTerm.nextReviewHours = ONE_WEEK
            }
            (ONE_WEEK) -> {
                currentSession.currentTerm.nextReviewHours = TWO_WEEKS
            }
            (TWO_WEEKS) -> {
                currentSession.currentTerm.nextReviewHours = ONE_MONTH
            }
            (ONE_MONTH) -> {
                currentSession.currentTerm.nextReviewHours = TWO_MONTHS
            }
            (TWO_MONTHS) -> {
                currentSession.currentTerm.nextReviewHours = FOUR_MONTHS
            }
            (FOUR_MONTHS) -> {
                currentSession.currentTerm.nextReviewHours = EIGHT_MONTHS
            }
            (EIGHT_MONTHS) -> {
                currentSession.currentTerm.nextReviewHours = ONE_YEAR
            }
            (ONE_YEAR) -> {
                currentSession.currentTerm.nextReviewHours = NEVER
            }
        }
        return currentSession.currentTerm.nextReviewHours
    }

    private fun decreaseReviewTime(rt: Int): Int {
        when (rt) {
            (NOW) -> {
                currentSession.currentTerm.nextReviewHours = NOW
            }
            (ONE_DAY) -> {
                currentSession.currentTerm.nextReviewHours = NOW
            }
            (TWO_DAYS) -> {
                currentSession.currentTerm.nextReviewHours = ONE_DAY
            }
            (ONE_WEEK) -> {
                currentSession.currentTerm.nextReviewHours = TWO_DAYS
            }
            (TWO_WEEKS) -> {
                currentSession.currentTerm.nextReviewHours = ONE_WEEK
            }
            (ONE_MONTH) -> {
                currentSession.currentTerm.nextReviewHours = TWO_WEEKS
            }
            (TWO_MONTHS) -> {
                currentSession.currentTerm.nextReviewHours = ONE_MONTH
            }
            (FOUR_MONTHS) -> {
                currentSession.currentTerm.nextReviewHours = TWO_MONTHS
            }
            (EIGHT_MONTHS) -> {
                currentSession.currentTerm.nextReviewHours = FOUR_MONTHS
            }
            (ONE_YEAR) -> {
                currentSession.currentTerm.nextReviewHours = EIGHT_MONTHS
            }
        }
        return currentSession.currentTerm.nextReviewHours
    }

    private fun swapSteps() {
        if (currentSession.currentStep == RevisionSession.AnswerTypes.ENG) {
            currentSession.currentStep = RevisionSession.AnswerTypes.TRANS
        } else if (currentSession.currentStep == RevisionSession.AnswerTypes.TRANS) {
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
    private fun sortSessionBy(
        session: MutableList<Vocab>,
        order: SortOrder
    ): MutableList<Vocab> {
        when (order) {
            SortOrder.RANDOM -> {
                session.shuffle()
            }
            SortOrder.TIME -> {
                session.sortBy { it.nextReviewHours }
            }
        }
        return session
    }

    private enum class SortOrder {
        RANDOM,
        TIME
    }

}