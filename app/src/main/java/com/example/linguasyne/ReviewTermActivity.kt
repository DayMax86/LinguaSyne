package com.example.linguasyne

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class ReviewTermActivity : AppCompatActivity() {

    private var termList: List<Term> = RevisionSessionManager.current_session.session_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_term)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        RevisionSessionManager.current_session.currentStep = RevisionSession.AnswerTypes.ENG
        RevisionSessionManager.current_session.currentTerm = termList[0]

        //Start by displaying the first term in the session, and default to asking for the English
        displayTerm(
            RevisionSessionManager.current_session.currentTerm,
            RevisionSessionManager.current_session.currentStep
        )

        findViewById<Button>(R.id.submit_button).setOnClickListener {

            //Check if the answer is right
            //* Show appropriate animation, clear text if correct, reset to blue box for next term
            //Load the next term
            //* Display next term

            //A * denotes visual tasks, otherwise purely logic tasks

        }
    }

    private fun loadNextTerm() {
        val t: Term? = RevisionSessionManager.advanceSession()
        if (t == null) {
            //Must be the end of the session, so launch summary activity
            val intent = Intent(this@ReviewTermActivity, RevisionSummaryActivity::class.java)
            startActivity(intent)
        } else {
            displayTerm(t, RevisionSessionManager.current_session.currentStep)
        }
    }

    private fun answerValidation() {
        if (checkAnswer(RevisionSessionManager.current_session.currentTerm)) {
            //The user got the answer right
            findViewById<EditText>(R.id.answerbox).text.clear()
            findViewById<EditText>(R.id.answerbox).setBackgroundResource(R.drawable.rounded_corners_textbox_bg)
        }
    }


    private fun checkAnswer(t: Term): Boolean {
        val answerBox = findViewById<EditText>(R.id.answerbox)
        when (RevisionSessionManager.current_session.currentStep) {
            //Check answer according to whether it's an ENG or TRANS step being tested
            (RevisionSession.AnswerTypes.TRANS) -> {
                if (answerBox.text.toString() == t.name) {
                    RevisionSessionManager.current_session.transStepComplete = true
                    animateAnswer(true)
                    return true
                }
            }
            (RevisionSession.AnswerTypes.ENG) -> {
                //Need to check for each of the translations in the list
                for (trans in t.translations) {
                    if (answerBox.text.toString() == trans) {
                        RevisionSessionManager.current_session.engStepComplete = true
                        animateAnswer(true)
                        return true
                    }
                }
            }
        }
        animateAnswer(false)
        return false
    }

    private fun animateAnswer(correct: Boolean) {
        val edittext = findViewById<EditText>(R.id.answerbox)
        var animator: ObjectAnimator? = null
        if (correct) {
            animator = ObjectAnimator.ofInt(
                edittext,
                "backgroundResource",
                R.drawable.rounded_corners_textbox_bg_green
            )
        } else {
            animator = ObjectAnimator.ofInt(
                edittext,
                "backgroundResource",
                R.drawable.rounded_corners_textbox_bg_red
            )
        }

        animator?.apply {
            duration = 1000
            addListener(onStart = {
            })
            addListener(onEnd = {
            })
            start()
        }
    }


    private fun displayTerm(t: Term?, answerType: RevisionSession.AnswerTypes) {
        //Depending on whether it's the ENG or TRANS answer that's required, the layout will change accordingly
        //   Use colours to differentiate between the two.

        val termText = findViewById<TextView>(R.id.term_textbox)

        if (t == null) { //The session is complete!
        } else {
            when (answerType) {
                (RevisionSession.AnswerTypes.ENG) -> {
                    termText.text = t.name
                    //answerBox.hint = "enter English translation"      NOT WORKING
                }
                (RevisionSession.AnswerTypes.TRANS) -> {
                    termText.text = t.translations[0]
                    //answerBox.hint = "enter French translation"       NOT WORKING
                }
                else -> {/*--error!--*/
                }
            }
        }
    }
}



