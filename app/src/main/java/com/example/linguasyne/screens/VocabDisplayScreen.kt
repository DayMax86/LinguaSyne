package com.example.linguasyne.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.linguasyne.R
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.managers.LessonManager
import com.example.linguasyne.ui.animations.AnimateSuccess
import com.example.linguasyne.ui.elements.DotsIndicator
import com.example.linguasyne.viewmodels.VocabDisplayViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSnapperApi::class)
@Composable
fun VocabDisplayScreen(navController: NavHostController) {

    val viewModel =  remember { VocabDisplayViewModel(navController) }

    ShowMainDisplay(
        show = viewModel.showDisplay,
        viewModel = viewModel,
        source = viewModel.vSource
    )

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowMainDisplay(
    show: Boolean,
    viewModel: VocabDisplayViewModel,
    source: VocabDisplayViewModel.Sources,
) {
    if (show) {
        MainDisplay(viewModel, source)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSnapperApi::class)
@Composable
fun MainDisplay(
    viewModel: VocabDisplayViewModel,
    source: VocabDisplayViewModel.Sources
) {
    when (source) {
        VocabDisplayViewModel.Sources.LESSON -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {


                val lazyListState = rememberLazyListState()
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.95f),
                    state = lazyListState,
                    flingBehavior = rememberSnapperFlingBehavior(lazyListState),
                ) {
                    items(
                        LessonManager.currentLesson.lessonList
                    ) { item ->

                        Card(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(10.dp),
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
                                DisplayTerm(
                                    item,
                                    viewModel.mascOutlineColour,
                                    viewModel.femOutlineColour,
                                    viewModel::handleTransTextPress,
                                    viewModel::handleMnemTextPress,
                                    viewModel::handleBackPress,
                                )
                            }

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


                        DotsIndicator(
                            totalDots = LessonManager.currentLesson.lessonList.size,
                            selectedIndex = lazyListState.firstVisibleItemIndex,
                            selectedColor = viewModel.selectedDotColour,
                            unSelectedColor = viewModel.unselectedDotColour,
                        )
                    }
                }

            }
        }
        VocabDisplayViewModel.Sources.SEARCH -> {
            Surface(
                modifier = Modifier
                    .blur(
                        viewModel.blurAmount.dp,
                        viewModel.blurAmount.dp,
                        BlurredEdgeTreatment.Rectangle
                    )
            ) {
                DisplayTerm(
                    viewModel.termToDisplay,
                    viewModel.mascOutlineColour,
                    viewModel.femOutlineColour,
                    viewModel::handleTransTextPress,
                    viewModel::handleMnemTextPress,
                    viewModel::handleBackPress,
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
                textFieldOutlineColour = viewModel.textFieldOutlineColour,
                buttonOnClick = viewModel::handleButtonPress,
                onBackBehaviour = viewModel::togglePopUp,
            )


        }
        else -> {}
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayTerm(
    vocab: Vocab,
    mascOutlineColour: Color,
    femOutlineColour: Color,
    onAddTransPress: () -> Unit,
    onAddMnemPress: () -> Unit,
    backBehaviour: () -> Unit,
) {

    BackHandler {
        backBehaviour()
    }

    Column() {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            //------------------------------ FIRST ROW --------------------------------//

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
                            color = MaterialTheme.colors.secondary,
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
                            .border(2.dp, mascOutlineColour, RoundedCornerShape(10))
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
                            .border(2.dp, femOutlineColour, RoundedCornerShape(10))
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
                    color = MaterialTheme.colors.secondary,
                )

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
                vocab.translations.forEach {
                    Text(
                        modifier = Modifier
                            .padding(all = 5.dp),
                        text = "$it,",
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.body1,
                    )
                }
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
                    color = MaterialTheme.colors.secondary,
                )

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
                vocab.mnemonics.forEach {
                    Text(
                        modifier = Modifier.padding(all = 5.dp),
                        text = it.replace("%'", ","),
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.body1,
                    )
                }
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
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary,
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                //progress = (term.current_level_term.toFloat()) / 100
                progress = 0.8f // TODO() TEST VALUE
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
                        text = vocab.currentLevelTerm.toString(),
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(10.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(end = 5.dp),
                        text = (vocab.currentLevelTerm + 1).toString(),
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(10.dp))

        //---------------------- SIXTH ROW ----------------------------//

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

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
                            .padding(all = 10.dp),
                        text = stringResource(id = R.string.next_review),
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
                            .padding(all = 10.dp),
                        text = vocab.nextReview.toString(),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondary,
                    )

                }
            }

        }
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
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f)
                .padding(all = 16.dp),
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





