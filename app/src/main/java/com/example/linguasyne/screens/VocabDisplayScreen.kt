package com.example.linguasyne.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.linguasyne.R
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.enums.Gender
import com.example.linguasyne.managers.LessonManager
import com.example.linguasyne.ui.animations.AnimateLoading
import com.example.linguasyne.ui.animations.AnimateSuccess
import com.example.linguasyne.ui.elements.BottomFadedBox
import com.example.linguasyne.ui.elements.EndLessonCard
import com.example.linguasyne.ui.elements.TopFadedBox
import com.example.linguasyne.ui.theme.LsLightPrimaryVariant
import com.example.linguasyne.ui.theme.LsLightOnPrimary
import com.example.linguasyne.viewmodels.VocabDisplayViewModel
import com.google.accompanist.pager.*


@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VocabDisplayScreen(navController: NavHostController) {

    val viewModel = remember { VocabDisplayViewModel(navController) }
    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState()

    ShowMainDisplay(
        show = viewModel.showDisplay,
        viewModel = viewModel,
        source = viewModel.vSource,
        pagerState = pagerState,
        scrollState = scrollState,
    )

}

@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowMainDisplay(
    show: Boolean,
    viewModel: VocabDisplayViewModel,
    source: VocabDisplayViewModel.Sources,
    pagerState: PagerState,
    scrollState: ScrollState,
) {
    if (show) {
        MainDisplay(viewModel, source, pagerState, scrollState)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainDisplay(
    viewModel: VocabDisplayViewModel,
    source: VocabDisplayViewModel.Sources,
    pagerState: PagerState,
    scrollState: ScrollState,
) {
    when (source) {
        VocabDisplayViewModel.Sources.LESSON -> {

            Surface(
                modifier = Modifier
                    .blur(
                        viewModel.blurAmount.dp,
                        viewModel.blurAmount.dp,
                        BlurredEdgeTreatment.Rectangle
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {

                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.95f),
                        state = pagerState,
                        count = LessonManager.currentLesson.lessonList.size + 1,
                    ) { page ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            elevation = 3.dp,
                        ) {

                            if (page <= LessonManager.currentLesson.lessonList.size - 1) {

                                Surface(
                                    modifier = Modifier
                                        .verticalScroll(scrollState)
                                        .background(MaterialTheme.colors.background)
                                        .fillMaxHeight()
                                        .fillMaxWidth()
                                )
                                {

                                    DisplayTerm(
                                        LessonManager.currentLesson.lessonList[page],
                                        viewModel::handleTransTextPress,
                                        viewModel::handleMnemTextPress,
                                        viewModel::handleBackPress,
                                        viewModel.progressBarValue,
                                        viewModel.showLoadingAnim,
                                    )


                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.Top,
                                ) {
                                    TopFadedBox(
                                        show =
                                        (scrollState.value > 0)
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.Bottom,
                                ) {
                                    BottomFadedBox(
                                        show =
                                        (scrollState.value < scrollState.maxValue) && (scrollState.maxValue > 0)
                                    )
                                }
                            }

                        }

                        if (page == LessonManager.currentLesson.lessonList.size) {
                            EndLessonCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(10.dp),
                                viewModel::handleBackPress
                            )
                        }

                    }

                }
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {

                        HorizontalPagerIndicator(
                            modifier = Modifier
                                .align(Alignment.Bottom)
                                .padding(top = 8.dp, bottom = 16.dp),
                            pagerState = pagerState,
                            activeColor = MaterialTheme.colors.primary,
                            inactiveColor = MaterialTheme.colors.secondaryVariant,
                        )
                    }
                }

                DisplayEndSessionWarning(
                    display = viewModel.displayEndSessionWarning,
                    onConfirmDialogueButton = { viewModel.handleEndPress() },
                    onDismissDialogueButton = { viewModel.handleBackPress() },
                )

            }

        }

        VocabDisplayViewModel.Sources.SEARCH -> {

            Surface(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .blur(
                        viewModel.blurAmount.dp,
                        viewModel.blurAmount.dp,
                        BlurredEdgeTreatment.Rectangle
                    )
            ) {
                DisplayTerm(
                    viewModel.termToDisplay,
                    viewModel::handleTransTextPress,
                    viewModel::handleMnemTextPress,
                    viewModel::handleBackPress,
                    viewModel.progressBarValue,
                    viewModel.showLoadingAnim,
                )


            }

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
            ) {
                TopFadedBox(
                    show =
                    (scrollState.value > 0)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
            ) {
                BottomFadedBox(
                    show =
                    (scrollState.value < scrollState.maxValue) && (scrollState.maxValue > 0)
                )
            }

            AnimateSuccess(
                animate = viewModel.animateSuccess,
                animationSpec = tween(viewModel.animateDuration.toInt()),
                initialScale = 0f,
                transformOrigin = TransformOrigin.Center,
            )

            DisplayPopUpInput(
                show = viewModel.showPopUpInput,
                tOrM = viewModel.selectedInputType,
                onTextValueChanged = viewModel::handleTextChange,
                userInput = viewModel.userInput,
                textFieldOutlineColour = MaterialTheme.colors.secondary,
                buttonOnClick = viewModel::handleButtonPress,
                onBackBehaviour = viewModel::togglePopUp,
            )

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayTerm(
    vocab: Vocab,
    onAddTransPress: () -> Unit,
    onAddMnemPress: () -> Unit,
    backBehaviour: () -> Unit,
    termProgress: Float,
    showAnim: Boolean,
) {

    BackHandler {
        backBehaviour()
    }

    Column(

    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            //------------------------------ FIRST ROW --------------------------------//

            AnimateLoading(
                animate = showAnim,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                )
                {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(all = 10.dp),
                            text = vocab.name,
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.h1,
                            textAlign = TextAlign.Center,
                        )

                        Text(
                            text = vocab.translations[0],
                            color = MaterialTheme.colors.primaryVariant,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                        )
                    }

                }
            }

        }


        //------------------------------ SECOND ROW --------------------------------//

        Spacer(modifier = Modifier.height(10.dp))

        Column(
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Row(
                ) {
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .border(
                                2.dp,
                                if (vocab.gender == Gender.M || vocab.gender == Gender.MF) {
                                    MaterialTheme.colors.secondary
                                } else {
                                    MaterialTheme.colors.onBackground
                                },
                                RoundedCornerShape(10)
                            )
                            .padding(5.dp)
                            .height(100.dp),
                        painter = painterResource(id = R.drawable.opaquemars),
                        contentDescription = null,
                        alignment = Alignment.CenterStart,
                    )
                }

                Row(
                ) {
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .border(
                                2.dp,
                                if (vocab.gender == Gender.F || vocab.gender == Gender.MF) {
                                    MaterialTheme.colors.secondary
                                } else {
                                    MaterialTheme.colors.onBackground
                                },
                                RoundedCornerShape(10)
                            )
                            .padding(5.dp)
                            .height(100.dp),
                        painter = painterResource(id = R.drawable.opaquevenus),
                        contentDescription = null,
                        alignment = Alignment.CenterStart,
                    )
                }

            }

        }

        //---------------------- THIRD ROW ----------------------------//

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .wrapContentHeight()
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.translations),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                )

                if (!LessonManager.activeLesson) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                onAddTransPress()
                            }
                            .padding(all = 10.dp),
                        text = stringResource(R.string.add_translation),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondary,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 10.dp, end = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.onBackground)
                    .border(
                        2.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(16.dp),
                    )
            ) {

                Text(
                    modifier = Modifier
                        .padding(all = 8.dp),
                    text = vocab.translations.joinToString(
                        separator = ", ",
                        truncated = "",
                    ),
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.body1,
                )

            }

        }

        Spacer(modifier = Modifier.height(10.dp))

        //---------------------- FOURTH ROW ----------------------------//

        Column(
            modifier = Modifier
                .wrapContentHeight()
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {


                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = stringResource(R.string.mnemonics),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                )

                if (!LessonManager.activeLesson) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                onAddMnemPress()
                            }
                            .padding(all = 10.dp),
                        text = stringResource(R.string.add_mnemonic),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondary,
                    )
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 10.dp, end = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.onBackground)
                    .border(
                        2.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(16.dp),
                    )
            ) {

                Text(
                    modifier = Modifier.padding(all = 8.dp),
                    text = vocab.mnemonics.joinToString(
                        separator = "\n\n",
                        //truncated = "",
                    ).replace("%'", ","),
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.body1,
                )

            }


        }

        Spacer(modifier = Modifier.height(10.dp))

        //---------------------- FIFTH ROW ----------------------------//

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(all = 10.dp),
                text = stringResource(id = R.string.your_progress),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary,
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                //progress = (term.current_level_term.toFloat()) / 100
                progress = termProgress // TODO() TEST VALUE
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    modifier = Modifier
                        .padding(10.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 5.dp),
                        text = stringResource(id = R.string.unlocked),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondary,
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(10.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(end = 5.dp),
                        text = stringResource(id = R.string.memorised),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondary,
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(10.dp))

    }


}


@Composable
fun DisplayPopUpInput(
    show: Boolean,
    tOrM: VocabDisplayViewModel.TransOrMnem,
    onTextValueChanged: (String) -> Unit,
    userInput: String,
    textFieldOutlineColour: Color,
    buttonOnClick: () -> Unit,
    onBackBehaviour: () -> Unit,
) {
    if (show) {
        BackHandler {
            onBackBehaviour()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(all = 10.dp)
                    .border(
                        2.dp,
                        MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(size = 10.dp)
                    ),
                backgroundColor = MaterialTheme.colors.onBackground,
                elevation = 6.dp,
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
            ) {

                Column(
                    verticalArrangement = Arrangement.Center
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {

                        OutlinedTextField(
                            modifier = Modifier
                                .padding(all = 16.dp)
                                .fillMaxWidth(),
                            value = userInput,
                            onValueChange = { onTextValueChanged(it) },
                            label = {
                                Text(
                                    text = stringResource(
                                        id =
                                        when (tOrM) {
                                            VocabDisplayViewModel.TransOrMnem.TRANSLATIONS -> {
                                                R.string.enter_translation
                                            }
                                            VocabDisplayViewModel.TransOrMnem.MNEMONICS -> {
                                                R.string.enter_mnemonic
                                            }
                                        }
                                    ),
                                    color = MaterialTheme.colors.secondary
                                )
                            },
                            singleLine = false,
                            textStyle = MaterialTheme.typography.body1,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = textFieldOutlineColour,
                                unfocusedBorderColor = textFieldOutlineColour,
                                textColor = MaterialTheme.colors.primary,
                            ),
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            onClick = { buttonOnClick() },
                            shape = RoundedCornerShape(100),
                            modifier = Modifier
                                .height(60.dp)
                                .width(150.dp)
                                .padding(bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.secondary,
                                contentColor = MaterialTheme.colors.onSurface,
                            ),
                            content = {
                                Text(text = stringResource(id = R.string.submit))
                            }
                        )
                    }
                }
            }
        }
    }
}





