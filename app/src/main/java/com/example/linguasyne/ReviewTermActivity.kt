package com.example.linguasyne

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Path
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.widget.doAfterTextChanged
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

        val ansBox = findViewById<EditText>(R.id.answerbox)
        ansBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ansBox.setBackgroundResource(R.drawable.rounded_corners_textbox_bg)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        findViewById<Button>(R.id.submit_button).setOnClickListener {
            validateAnswer()
        }
    }

    private fun ReviewTermActivity.validateAnswer() = runBlocking {
        //Check if the answer is right
        if (TermDisplayManager.checkAnswer(
                findViewById<EditText>(R.id.answerbox).text.toString()
            )
        ) {
            //User got the answer correct so show appropriate animation
            animateAnswer(true)
            //Animation needs to complete before the UI is reset!
            //Load the next term
            advance()
            resetUI()
            //* Display next term
            displayTerm()
        } else {
            //User got the answer wrong so show appropriate animation
            animateAnswer(false)
        }
    }

    private suspend fun advance() = coroutineScope {
        delay(1000L)
        TermDisplayManager.loadNextTerm(this@ReviewTermActivity)
    }

    private fun resetUI() {
        findViewById<EditText>(R.id.answerbox).text.clear()
    }


    private fun animateAnswer(correct: Boolean) {
        val edittext = findViewById<EditText>(R.id.answerbox)
            if (correct) {
                edittext.setBackgroundResource(R.drawable.rounded_corners_textbox_bg_green)
            } else {
                edittext.setBackgroundResource(R.drawable.rounded_corners_textbox_bg_red)
                val shake: Animation =
                    AnimationUtils.loadAnimation(this@ReviewTermActivity, R.anim.shake)
                edittext.startAnimation(shake)
            }
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




