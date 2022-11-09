package com.example.linguasyne.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.linguasyne.R
import com.example.linguasyne.classes.User
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.ui.elements.ApiBox
import com.example.linguasyne.ui.elements.SelectImage
import com.example.linguasyne.viewmodels.ApiViewModel
import com.example.linguasyne.viewmodels.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import dev.chrisbanes.snapper.ExperimentalSnapperApi

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    onClickHelp: () -> Unit,
) {
    val viewModel = remember { HomeViewModel(navController) }
    val apiViewModel = remember { ApiViewModel() }

    DisplayHome(
        viewModel.user,
        viewModel::handleHelpClick,
        viewModel.userImage,
        viewModel::handleVocabLessonClick,
        viewModel::handleRevisionClick,
        viewModel::handleTermBaseClick,
        viewModel::uploadUserImage,
        viewModel.activeIndicatorColour,
        viewModel.inactiveIndicatorColour,
        viewModel::onBackPressed,
        apiViewModel,
    )

}

@OptIn(ExperimentalSnapperApi::class, ExperimentalPagerApi::class)
@Composable
fun DisplayHome(
    user: User,
    onClickHelp: () -> Unit,
    userImage: Uri?,
    onClickVocabLesson: () -> Unit,
    onClickRevision: () -> Unit,
    onClickTermBase: () -> Unit,
    onClickProfileImage: (Uri?) -> Unit,
    activeIndicatorColour: Color,
    inactiveIndicatorColour: Color,
    backBehaviour: () -> Unit,
    apiViewModel: ApiViewModel,
) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        BackHandler {
            backBehaviour()
        }

        TopHomeScreen(
            onClickVocabLesson,
            onClickRevision,
            onClickTermBase,
            user,
            onClickProfileImage,
            userImage
        )

        Spacer(modifier = Modifier.height(20.dp))

        BottomHomeScreen(
            user,
            apiViewModel,
            activeIndicatorColour,
            inactiveIndicatorColour
        )

    }

}

@Composable
private fun TopHomeScreen(
    onClickVocabLesson: () -> Unit,
    onClickRevision: () -> Unit,
    onClickTermBase: () -> Unit,
    user: User,
    onClickProfileImage: (Uri?) -> Unit,
    userImage: Uri?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        //.padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Column(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(IntrinsicSize.Max),
        ) {

            Text(
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 5.dp),
                text = stringResource(id = R.string.lessons),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary,
            )

            Button(
                modifier = Modifier
                    .width(150.dp),
                onClick = { onClickVocabLesson() },
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
                            Text("50")
                        }
                    }

                },
            )

            Column(

            ) {

                Text(
                    modifier = Modifier
                        .padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                    text = stringResource(id = R.string.revision),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                )

                Button(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(150.dp),
                    onClick = { onClickRevision() },
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
                                Text("50")
                            }
                        }

                    },
                )
            }

            Row(

            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {

                    Text(
                        //Books emoji
                        text = String(Character.toChars(0x1F4DA)),
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .clickable { onClickTermBase() },
                        text = stringResource(id = R.string.term_base),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                    )

                }

            }

            Row(
                modifier = Modifier
                    .padding(bottom = 2.dp),
                horizontalArrangement = Arrangement.Center,
            ) {

                Text(
                    //Fire emoji
                    text = String(Character.toChars(0x1F525)),
                )

                Text(
                    modifier = Modifier
                        .padding(start = 2.dp),
                    text = stringResource(
                        id = R.string.day_streak,
                        user.streak
                    ),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.secondary
                )

            }

        }




        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(IntrinsicSize.Max),
            //.padding(end = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = Modifier
                    .padding(top = 10.dp)
            ) {


                SelectImage(
                    onImageSelected = onClickProfileImage,
                    userImage = userImage
                )

                FirebaseManager.currentUser?.let {
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        text = it.email
                    )
                }

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primaryVariant,
                    text = stringResource(id = R.string.level) + "${user.level}"
                )

            }

        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun BottomHomeScreen(
    user: User,
    apiViewModel: ApiViewModel,
    activeIndicatorColour: Color,
    inactiveIndicatorColour: Color
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        Text(
            modifier = Modifier
                .padding(top = 16.dp),
            text = stringResource(
                id = R.string.today_in,
                user.studyCountry
            ),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary
        )

        val pagerState = rememberPagerState()
        val scrollState = rememberScrollState()

        ApiBox(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .weight(1f),
            viewModel = apiViewModel,
            pagerState = pagerState
        )

        HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 16.dp),
            pagerState = pagerState,
            activeColor = activeIndicatorColour,
            inactiveColor = inactiveIndicatorColour,
        )

    }
}


