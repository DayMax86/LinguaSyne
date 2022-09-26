package com.example.linguasyne.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.linguasyne.R
import com.example.linguasyne.classes.User
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.ui.elements.ApiBox
import com.example.linguasyne.ui.elements.DotsIndicator
import com.example.linguasyne.ui.elements.SelectImage
import com.example.linguasyne.viewmodels.ApiViewModel
import com.example.linguasyne.viewmodels.HomeViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavHostController) {
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
        viewModel.selectedNewsColour,
        viewModel.unselectedNewsColour,
        viewModel::onBackPressed,
        apiViewModel,
    )

}


@OptIn(ExperimentalSnapperApi::class)
@Composable
fun DisplayHome(
    user: User,
    onClickHelp: () -> Unit,
    userImage: Uri?,
    onClickVocabLesson: () -> Unit,
    onClickRevision: () -> Unit,
    onClickTermBase: () -> Unit,
    onClickProfileImage: (Uri?) -> Unit,
    selectedNewsColour: Color,
    unselectedNewsColour: Color,
    backBehaviour: () -> Unit,
    apiViewModel: ApiViewModel,
) {
    val lazyListState = rememberLazyListState()

    Column(

    ) {

        BackHandler {
            backBehaviour()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            /*---------------------------FIRST LAYER------------------------------------------*/
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Row(
                    modifier = Modifier
                        .padding(start = 10.dp),
                ) {
                    /*--Left column with 'lessons' and 3 buttons--*/

                    Column(
                        modifier = Modifier
                            .padding(top = 16.dp)
                    ) {

                        Text(
                            modifier = Modifier
                                .padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                            text = stringResource(id = R.string.lessons),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.secondary,
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
                                color = MaterialTheme.colors.secondary,
                            )

                            Button(
                                modifier = Modifier
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
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Row(

                                    ) {
                                        Text(stringResource(id = R.string.exam))
                                    }

                                }

                            },
                        )


                    }
                }

                Row(
                    modifier = Modifier
                        .padding(end = 10.dp),
                ) {
                    /*--Right column with profile info--*/

                    Column(

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
                            color = MaterialTheme.colors.primary,
                            text = stringResource(id = R.string.level) + "${user.level}"
                        )

                    }

                }

            }
        }


        Column(

        ) {
            /*---------------------------THIRD LAYER------------------------------------------*/

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(

                ) {
                    /*--Left column with 'streak' icon and text--*/

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
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

                ) {
                    /*--Right column with 'term base' icon and text--*/

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
                            color = MaterialTheme.colors.secondary,
                        )

                    }

                }

            }
        }


        /*---------------------------FOURTH LAYER------------------------------------------*/


        Column(
            modifier = Modifier
                .padding(bottom = 32.dp)
        ) {

            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .wrapContentHeight(),
            ) {

                ApiBox(
                    user = FirebaseManager.currentUser ?: User(""),
                    viewModel = apiViewModel,
                    lazyListState = lazyListState,
                )
            }

        }


    }

    Box(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
            ) {

                DotsIndicator(
                    totalDots = apiViewModel.news.size,
                    selectedIndex = lazyListState.firstVisibleItemIndex,
                    selectedColor = selectedNewsColour,
                    unSelectedColor = unselectedNewsColour,
                )

            }
        }
    }

}




