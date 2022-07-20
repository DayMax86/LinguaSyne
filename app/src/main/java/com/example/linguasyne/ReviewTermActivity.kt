package com.example.linguasyne

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReviewTermActivity : AppCompatActivity() {

    private var termList: List<Term> = RevisionSessionManager.current_session.session_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_term)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        RevisionSessionManager.current_session.currentStep = RevisionSession.AnswerTypes.ENG
        RevisionSessionManager.current_session.currentTerm = termList[0]

        //Start by displaying the first term in the session, and default to asking for the English
        displayTerm(RevisionSessionManager.current_session.currentTerm, RevisionSessionManager.current_session.currentStep)

        findViewById<Button>(R.id.submit_translation_button).setOnClickListener {
            if (checkAnswer(RevisionSessionManager.current_session.currentTerm)) {
                //The user got the answer right
                displayTerm(
                    RevisionSessionManager.advanceSession(),
                    RevisionSessionManager.current_session.currentStep
                )
            }
        }
    }

    private fun checkAnswer(t: Term): Boolean {
        when (RevisionSessionManager.current_session.currentStep) {
            //Check answer according to whether it's an ENG or TRANS step being tested
            (RevisionSession.AnswerTypes.ENG) -> {
                if (findViewById<EditText>(R.id.answerbox).text.toString() == t.name) {
                    RevisionSessionManager.current_session.engStepComplete = true
                    return true
                }
            }
            (RevisionSession.AnswerTypes.TRANS) -> {
                //Need to check for each of the translations in the list
                for (trans in t.translations) {
                    if (findViewById<EditText>(R.id.answerbox).text.toString() == trans) {
                        RevisionSessionManager.current_session.transStepComplete = true
                        return true
                    }
                }
            }
            else -> {/*--How did we get here?--*/
            }
        }
        return false
    }

    private fun displayTerm(t: Term?, answerType: RevisionSession.AnswerTypes) {
        //Depending on whether it's the ENG or TRANS answer that's required, the layout will change accordingly
        //   Use colours to differentiate between the two.

        //cache references to views
        val termText = findViewById<TextView>(R.id.term_name_textbox)
        val answerBox = findViewById<EditText>(R.id.answerbox)

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