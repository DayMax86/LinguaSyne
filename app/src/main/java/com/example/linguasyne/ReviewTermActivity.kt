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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_term)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Initiate the first term, defaulting to the ENG step for it
        RevisionSessionManager.current_session.currentStep = RevisionSession.AnswerTypes.ENG
        RevisionSessionManager.current_session.currentTerm = TermDisplayManager.termList[0]
        displayTerm()

        findViewById<Button>(R.id.submit_button).setOnClickListener {

            //Check if the answer is right
            if (TermDisplayManager.checkAnswer(
                    findViewById<EditText>(R.id.answerbox).text.toString()
                )
            ) {
                //User got the answer correct so show appropriate animation
                animateAnswer(true)
                //Animation needs to complete before the UI is reset!
                //Load the next term
                TermDisplayManager.loadNextTerm()
                resetUI()
                //* Display next term
                displayTerm()
            } else {
                //User got the answer wrong so show appropriate animation
                animateAnswer(false)
            }
        }
    }

    private fun resetUI() {
        //Put the edittext drawable back to the default blue
        //Empty the contents of the edittext
    }


    private fun animateAnswer(correct: Boolean) {
        val edittext = findViewById<EditText>(R.id.answerbox)
        var animator: ObjectAnimator? = null
        if (correct) {
            animator = ObjectAnimator.ofInt(
                edittext,
                "background",
                R.drawable.rounded_corners_textbox_bg_green
            )
        } else {
            animator = ObjectAnimator.ofInt(
                edittext,
                "background",
                R.drawable.rounded_corners_textbox_bg_red
            )
        }

        animator.start()
        edittext.invalidate()
    }


    private fun displayTerm() {
        //Depending on whether it's the ENG or TRANS answer that's required, the layout will change accordingly
        //TODO() Use colours/another visual element to differentiate between the two.

        val termText = findViewById<TextView>(R.id.term_textbox)
        val ct = RevisionSessionManager.current_session.currentTerm

        when (RevisionSessionManager.current_session.currentStep) {
            (RevisionSession.AnswerTypes.ENG) -> {
                termText.text = ct.name
                //answerBox.hint = "enter English translation"      NOT WORKING
            }
            (RevisionSession.AnswerTypes.TRANS) -> {
                termText.text = ct.translations[0]
                //answerBox.hint = "enter French translation"       NOT WORKING
            }
            else -> {/*--error!--*/
            }
        }
    }
}




