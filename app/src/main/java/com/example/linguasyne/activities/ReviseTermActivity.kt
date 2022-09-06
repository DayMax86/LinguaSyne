package com.example.linguasyne.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.linguasyne.R
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.White
import com.example.linguasyne.viewmodels.ReviseTermViewModel

class ReviseTermActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ReviseTermViewModel()

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

                    AnimateCorrectAnswer(
                        animate = viewModel.animateCorrect,
                        animationSpec = tween(viewModel.animateDuration.toInt()),
                        initialScale = 0f,
                        transformOrigin = TransformOrigin.Center,
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


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun AnimateCorrectAnswer(
        animate: Boolean,
        animationSpec: FiniteAnimationSpec<Float>,
        initialScale: Float,
        transformOrigin: TransformOrigin,
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight(0.33f),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            )
            {
                AnimatedVisibility(
                    modifier = Modifier
                        .padding(2.dp),
                    visible = animate,

                    enter = scaleIn(
                        animationSpec = animationSpec,
                        initialScale = initialScale,
                        transformOrigin = transformOrigin,
                    ) + expandVertically(
                        expandFrom = Alignment.CenterVertically
                    ) + expandHorizontally(
                        expandFrom = Alignment.CenterHorizontally
                    ),

                    exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
                ) {
                    Row(
                        Modifier
                            .size(200.dp)
                            .border(4.dp, MaterialTheme.colors.secondary, shape = CircleShape)
                            .background(
                                color = LsCorrectGreen, shape = CircleShape
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row {
                            Text(
                                text = String(Character.toChars(0x2713)),
                                //color = LsCorrectGreen,
                                style =
                                TextStyle(
                                    color = White,
                                    fontSize = 150.sp,
                                ),

                                )
                        }
                    }
                }
            }

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
                    label = { Text(stringResource(id = R.string.enter_translation)) },
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
                    text = stringResource(id = R.string.select_gender),
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
                        text = stringResource(id = R.string.submit),
                    color = MaterialTheme.colors.onBackground)
                }
            }

        }
    }

}
