package com.example.linguasyne.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.ReviewTermViewModel
import kotlin.reflect.KFunction0

class ReviewTermActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ReviewTermViewModel()

        setContent {
            LaunchSummary(needsLaunching = viewModel.launchSummary)
            viewModel.initiateSession()
            LinguaSyneTheme(
                false,
            ) {
                Surface(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                ) {
                    ViewTerm(
                        viewModel.currentTermTitle,
                        viewModel.userInput,
                        onClick = viewModel::handleSubmit,
                        handleChange = viewModel::handleInput,
                        outlineColour = viewModel.outlineColour,
                    )
                }
            }
        }

    }

    @Composable
    fun LaunchSummary(
        needsLaunching: Boolean,
    ) {
        if (needsLaunching) {
            this.finish()
            val intent = Intent(this, RevisionSummaryActivity::class.java)
            startActivity(intent)
        }
    }

    @Composable
    fun ViewTerm(
        termName: String?,
        userInput: String,
        handleChange: (String) -> Unit,
        onClick: KFunction0<Unit>,
        outlineColour: Color,
    ) {

        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = termName.toString(),
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

            Spacer(modifier = Modifier.height(110.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = userInput,
                onValueChange = { handleChange(it) },
                label = { Text("Enter translation") },
                singleLine = true,
                textStyle = MaterialTheme.typography.body1,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = outlineColour
                ),
            )

            Spacer(modifier = Modifier.height(50.dp))

            //TODO() Implement gender images that can be clicked on to select a gender
            /* This current crashes the app on activity start
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

    //------------------------------PREVIEW---------------------------------------------
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
                    userInput = viewModel.userInput,
                    outlineColour = viewModel.outlineColour
                )
            }
        }
    }
    //-------------------------------------------------------------------------------------------//
}
