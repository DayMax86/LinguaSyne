package com.example.linguasyne.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun HomeDrawerContent() {

    Column(
        modifier = Modifier
            .width(intrinsicSize = IntrinsicSize.Max),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            /*modifier = Modifier.clickable { //TODO() FOR DEVELOPER USE ONLY!! Will be removed in release version.
    CSVManager.importVocabCSV(this@StartActivity.applicationContext)
},*/
            horizontalArrangement = Arrangement.Start,
        ) {

            Row(
                modifier = Modifier
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.about),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                    maxLines = 1,
                )
                Icon(
                    Icons.Default.Info,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 6.dp)
                        .size(20.dp, 20.dp),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary
                )
            }

        }

        Divider(
            modifier = Modifier
                .height(1.dp),
            color = MaterialTheme.colors.onBackground,
        )
    }

    Column(
        modifier = Modifier
            .width(intrinsicSize = IntrinsicSize.Max),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    //launchTermBase()
                },
            horizontalArrangement = Arrangement.Start,
        ) {

            Row(
                modifier = Modifier
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.term_base),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                    maxLines = 1,
                )
                Icon(
                    Icons.Default.Search,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 6.dp)
                        .size(20.dp, 20.dp),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary
                )
            }

        }

        Divider(
            modifier = Modifier
                .height(1.dp),
            color = MaterialTheme.colors.onBackground,
        )
    }

    Column(
        modifier = Modifier
            .width(intrinsicSize = IntrinsicSize.Max),
        horizontalAlignment = Alignment.Start,
    ) {

        Row(
            modifier = Modifier
                .clickable {
                    //signOut()
                },
            horizontalArrangement = Arrangement.Start,
        ) {

            Row(
                modifier = Modifier
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.sign_out),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary,
                    maxLines = 1,
                )
                Icon(
                    Icons.Default.AccountCircle,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 6.dp)
                        .size(20.dp, 20.dp),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary
                )
            }

        }
        Divider(
            modifier = Modifier
                .height(1.dp),
            color = MaterialTheme.colors.onBackground,
        )
    }

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
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        BackHandler {
            backBehaviour()
        }

        /*---------------------------FIRST LAYER------------------------------------------*/
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
                /*--Right column with profile info--*/

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


        /*---------------------------THIRD LAYER------------------------------------------*/
        Column(
            modifier = Modifier
                .fillMaxHeight(.8f) //
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            ApiBox(
                user = FirebaseManager.currentUser ?: User(""),
                viewModel = apiViewModel,
                lazyListState = lazyListState,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center,
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




