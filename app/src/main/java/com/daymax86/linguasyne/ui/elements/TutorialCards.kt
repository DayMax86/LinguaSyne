package com.daymax86.linguasyne.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daymax86.linguasyne.R

@Composable
fun TutorialCardOne(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        elevation = 3.dp,
    ) {
        Surface(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.background)
                .fillMaxHeight()
                .fillMaxWidth()
        )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.welcome),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.about_linguasyne),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.continue_with_arrow),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

            }

        }
    }

}

@Composable
fun TutorialCardTwo(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        elevation = 3.dp,
    ) {
        Surface(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.background)
                .fillMaxHeight()
                .fillMaxWidth()
        )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.lessons),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.about_lessons),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.lessons_found_here),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = Modifier
                            .width(150.dp),
                        onClick = { },
                        shape = RoundedCornerShape(100),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onSurface,
                        ),
                        content = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Row(
                                ) {
                                    Text(stringResource(id = R.string.vocab))
                                }
                                Row(
                                ) {
                                    Text("10")
                                }
                            }

                        },
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.lessons_advice),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Row(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(all = 10.dp),
                        text = stringResource(R.string.masc_preview),
                        color = MaterialTheme.colors.primaryVariant,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        modifier = Modifier
                            .padding(all = 10.dp),
                        text = stringResource(R.string.fem_preview),
                        color = MaterialTheme.colors.primaryVariant,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.padding(top = 32.dp))
                /*Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.continue_with_arrow),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )*/

            }

        }
    }

}

@Composable
fun TutorialCardThree(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        elevation = 3.dp,
    ) {
        Surface(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.background)
                .fillMaxHeight()
                .fillMaxWidth()
        )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.revision),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.about_revision),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.revision_found_here),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = Modifier
                            .width(150.dp),
                        onClick = { },
                        shape = RoundedCornerShape(100),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onSurface,
                        ),
                        content = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Row(
                                ) {
                                    Text(stringResource(id = R.string.to_revise))
                                }
                                Row(
                                ) {
                                    Text("10")
                                }
                            }

                        },
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.revision_advice),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.further_revision_advice),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.padding(top = 32.dp))

                /*Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.continue_with_arrow),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )*/

            }

        }
    }

}

@Composable
fun TutorialCardFour(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        elevation = 3.dp,
    ) {
        Surface(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.background)
                .fillMaxHeight()
                .fillMaxWidth()
        )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.term_base),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.about_term_base),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.term_base_found_here),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 2.dp),
                        text = String(Character.toChars(0x1F4DA)) + stringResource(id = R.string.term_base),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.term_base_advice),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.padding(top = 32.dp))

                /*Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.continue_with_arrow),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )*/

            }

        }
    }

}

@Composable
fun TutorialCardFive(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        elevation = 3.dp,
    ) {
        Surface(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.background)
                .fillMaxHeight()
                .fillMaxWidth()
        )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.side_drawer),
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.about_side_drawer),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.side_drawer_advice),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.padding(top = 32.dp))

                /*Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.continue_with_arrow),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )*/

            }

        }
    }

}

@Composable
fun TutorialCardEnd(
    modifier: Modifier = Modifier,
    endTutorial: () -> Unit,
) {
    Card(
        modifier = modifier,
        elevation = 3.dp,
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                modifier = Modifier
                    .padding(all = 10.dp),
                text = stringResource(R.string.enjoy),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center,
            )

            Text(
                modifier = Modifier
                    .padding(all = 10.dp),
                text = stringResource(R.string.thank_you),
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
            )

            Text(
                modifier = Modifier
                    .padding(all = 10.dp),
                text = stringResource(R.string.get_started),
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
            )

            Button(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .width(150.dp),
                onClick = { endTutorial() },
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSurface,
                )
            ) {
                Text(text = stringResource(R.string.go))
            }

        }


    }

}

