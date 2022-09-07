package com.example.linguasyne.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.linguasyne.R
import com.example.linguasyne.classes.Term
import com.example.linguasyne.managers.LessonManager
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.DisplayTermViewModel
import com.example.linguasyne.viewmodels.Sources
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import androidx.compose.material.OutlinedTextField
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.linguasyne.ui.theme.LsCorrectGreen
import com.example.linguasyne.ui.theme.White

open class DisplayTermActivity : AppCompatActivity() {

    val viewModel = DisplayTermViewModel()

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSnapperApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LinguaSyneTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    when (viewModel.onActivityLaunch()) {
                        Sources.LESSON -> {
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
                        Sources.SEARCH -> {
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
                                )
                            }

                            AnimateSuccessfulUpload(
                                animate = viewModel.animateSuccess,
                                animationSpec = tween(viewModel.animateDuration.toInt()),
                                initialScale = 0f,
                                transformOrigin = TransformOrigin.Center,
                            )

                            TogglePopUpInput(showPopUpInput = viewModel.showPopUpInput)

                        }
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onActivityEnd()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun DisplayTerm(
        term: Term,
        mascOutlineColour: Color,
        femOutlineColour: Color,
        onAddTransPress: () -> Unit,
        onAddMnemPress: () -> Unit,
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
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
                        horizontalAlignment = CenterHorizontally,
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(all = 10.dp),
                            text = term.name,
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.h1,
                            textAlign = TextAlign.Center,
                        )

                        Text(
                            text = term.translations[0],
                            color = MaterialTheme.colors.secondary,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                        )

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
                    horizontalArrangement = Arrangement.SpaceEvenly
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
                    term.translations.forEach {
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
                    horizontalArrangement = Arrangement.SpaceEvenly
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
                    term.mnemonics.forEach {
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
                        .align(CenterHorizontally)
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
                            text = term.currentLevelTerm.toString(),
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(10.dp),
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(end = 5.dp),
                            text = (term.currentLevelTerm + 1).toString(),
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
                            text = term.nextReview.toString(),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.secondary,
                        )

                    }
                }

            }


        }

    }

    @Composable
    fun TogglePopUpInput(
        showPopUpInput: Boolean,
    ) {
        if (showPopUpInput) {
            DisplayPopUpInput(
                tOrM = viewModel.selectedInputType,
                onTextValueChanged = viewModel::handleTextChange,
                userInput = viewModel.userInput,
                textFieldOutlineColour = viewModel.mascOutlineColour,
                buttonOnClick = viewModel::handleButtonPress,
            )
        }
    }

    @Composable
    fun DisplayPopUpInput(
        tOrM: DisplayTermViewModel.TransOrMnem,
        onTextValueChanged: (String) -> Unit,
        userInput: String,
        textFieldOutlineColour: Color,
        buttonOnClick: () -> Unit,
    ) {

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Column(

            ) {

                Card(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f)
                        .padding(all = 16.dp),
                    elevation = 6.dp,
                    backgroundColor = MaterialTheme.colors.onSurface,
                ) {

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = userInput,
                        onValueChange = { onTextValueChanged(it) },
                        label = {
                            Text(
                                text = stringResource(
                                    id =
                                    when (tOrM) {
                                        DisplayTermViewModel.TransOrMnem.TRANSLATIONS -> {
                                            R.string.enter_translation
                                        }
                                        DisplayTermViewModel.TransOrMnem.MNEMONICS -> {
                                            R.string.enter_mnemonic
                                        }
                                    }
                                ),
                                color = MaterialTheme.colors.secondary
                            )
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.body1,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = textFieldOutlineColour,
                            unfocusedBorderColor = textFieldOutlineColour,
                            textColor = MaterialTheme.colors.primary,
                        ),
                    )
                }

                Button(
                    onClick = { buttonOnClick() },
                    shape = RoundedCornerShape(100),
                    modifier = Modifier
                        .height(60.dp)
                        .width(150.dp)
                        .padding(top = 10.dp),
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

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun AnimateSuccessfulUpload(
        animate: Boolean,
        animationSpec: FiniteAnimationSpec<Float>,
        initialScale: Float,
        transformOrigin: TransformOrigin,
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight(0.75f),
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
                        expandFrom = CenterHorizontally
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
    fun DotsIndicator(
        totalDots: Int,
        selectedIndex: Int,
        selectedColor: Color,
        unSelectedColor: Color,
    ) {

        LazyRow(

        ) {

            items(totalDots) { index ->
                if (index == selectedIndex) {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(selectedColor)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(unSelectedColor)
                    )
                }

                if (index != totalDots - 1) {
                    Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                }
            }

        }
    }

}


