package com.example.linguasyne

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Path
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModel
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import coil.compose.AsyncImage
import coil.transform.CircleCropTransformation

class ReviewTermActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ReviewTermViewModel()

        setContent {
            LaunchSummary(needsLaunching = viewModel.launchSummary)
            LinguaSyneTheme(
                false,
            ) {
                Surface(

                ) {
                    ViewTerm(
                        viewModel.currentTermName,
                        viewModel.userInput,
                        onClick = viewModel::handleSubmit,
                        handleChange = viewModel::handleInput,
                    )
                }
            }
        }

        oldOnCreate()
    }

    @Composable
    fun LaunchSummary(
        needsLaunching: Boolean,
    ) {
        // Create Intent, startActivity
    }

    @Composable
    fun ViewTerm(
        termName: String,
        userInput: String,
        handleChange: (String) -> Unit,
        onClick: () -> Unit
    ) {

        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .background(MaterialTheme.colors.background)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = termName,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h1
            )

            Spacer(modifier = Modifier.height(50.dp))

        }

        Column(
            modifier = Modifier
                .padding(all = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "Translation:",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.body1
            )
        }

        Column(
            modifier = Modifier
                .padding(all = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(100.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = userInput,
                onValueChange = { handleChange(it) },
                label = { Text("Enter translation") },
                singleLine = true,
                textStyle = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(50.dp))

            /* NOT WORKING
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
                    .clip(CircleShape),
                model = painterResource(id = R.drawable.opaquemars),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            ) */
        }

        Column(
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(250.dp))

            Button(
                onClick = { onClick() },
                shape = RoundedCornerShape(100),
                modifier = Modifier.size(200.dp, 45.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            )
            {
                Text("Submit")
            }
        }

    }


    @Preview(showBackground = true)
    @Composable
    fun PreviewSummary() {
        val viewModel = ReviewTermViewModel()
        LinguaSyneTheme(
            darkTheme = false
        ) {
            Surface(

            ) {
                ViewTerm(
                    "example",
                    onClick = viewModel::handleSubmit,
                    handleChange = viewModel::handleInput,
                    userInput = viewModel.userInput
                )
            }
        }
    }


    //-------------------------------------------------------------------------------------------//


    private fun oldOnCreate() {
        //TODO() Temporarily storing old onCreate here while working on compose ui
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

    private fun validateAnswer() = runBlocking {
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
        if (!TermDisplayManager.loadNextTerm(this@ReviewTermActivity)) {
            //false means that there is no next term to be loaded
            this@ReviewTermActivity.finish()
        }
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
        //TODO() Use colours/another visual element to differentiate between the two - emoji flag?

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

    fun endReviewActivity() {
        this.finish()
    }
}




