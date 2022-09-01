package com.example.linguasyne.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.linguasyne.R
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.ReviewTermViewModel

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
                        .fillMaxHeight()
                ) {
                    ViewTerm(
                        viewModel.currentTermTitle,
                        viewModel.userInput,
                        onClickSubmit = viewModel::handleSubmit,
                        onClickMasc = viewModel::handleMascClick,
                        onClickFem = viewModel::handleFemClick,
                        handleChange = viewModel::handleInput,
                        textFieldOutlineColour = viewModel.textFieldOutlineColour,
                        mascOutlineColour = viewModel.mascOutlineColour,
                        femOutlineColour = viewModel.femOutlineColour,
                        selectGenderTextColour = viewModel.selectGenderTextColour,
                        mascImage = viewModel.mascImage,
                        femImage = viewModel.femImage,
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
        onClickSubmit: () -> Unit,
        onClickMasc: () -> Unit,
        onClickFem: () -> Unit,
        textFieldOutlineColour: Color,
        mascOutlineColour: Color,
        femOutlineColour: Color,
        selectGenderTextColour: Color,
        mascImage: Int,
        femImage: Int,
    ) {

        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = termName.toString(),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h1
                )


            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = userInput,
                    onValueChange = { handleChange(it) },
                    label = { Text("${resources.getText(R.string.enter_translation)}") },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.body1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = textFieldOutlineColour,
                        unfocusedBorderColor = textFieldOutlineColour,
                    ),
                )


            }

            Row(
                modifier = Modifier
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = "${resources.getText(R.string.select_gender)}",
                    color = selectGenderTextColour,
                    style = MaterialTheme.typography.body1,
                )
            }

            Row(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {

                Row(

                ) {
                    Image(
                        painterResource(mascImage),
                        modifier = Modifier
                            .border(2.dp, mascOutlineColour, RoundedCornerShape(10))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onClickMasc()
                            }
                            .size(width = 75.dp, height = 100.dp)
                            .clip(shape = RectangleShape),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )
                }

                Row(
                    modifier = Modifier
                ) {
                    Image(
                        painterResource(femImage),
                        modifier = Modifier
                            .border(2.dp, femOutlineColour, RoundedCornerShape(10))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onClickFem()
                            }
                            .size(width = 75.dp, height = 100.dp)
                            .clip(shape = RectangleShape),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )
                }

            }


            Row(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {

                Button(
                    onClick = { onClickSubmit() },
                    shape = RoundedCornerShape(100),
                    modifier = Modifier.size(200.dp, 45.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                )
                {
                    Text(
                        text = "${resources.getText(R.string.submit)}",
                    color = MaterialTheme.colors.onBackground)
                }
            }

        }
    }

}
