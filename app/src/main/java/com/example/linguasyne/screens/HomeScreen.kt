package com.example.linguasyne.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.linguasyne.R
import com.example.linguasyne.classes.NewsItem
import com.example.linguasyne.classes.User
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.managers.LessonManager
import com.example.linguasyne.ui.elements.DotsIndicator
import com.example.linguasyne.ui.elements.SelectImage
import com.example.linguasyne.viewmodels.HomeViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlin.reflect.KSuspendFunction0

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavHostController) {
    val viewModel = HomeViewModel(navController)

    viewModel.init()

    DisplayHome(
        viewModel.user,
        viewModel::handleHelpClick,
        viewModel.userImage,
        viewModel::handleVocabLessonClick,
        viewModel::handleRevisionClick,
        viewModel::handleTermBaseClick,
        viewModel::uploadUserImage,
       // viewModel.newsItems,
        viewModel.selectedNewsColour,
        viewModel.unselectedNewsColour,
        viewModel::onBackPressed,
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
    //newsItems: List<NewsItem.Data>,
    selectedNewsColour: Color,
    unselectedNewsColour: Color,
    backBehaviour: () -> Unit,
) {
    Column(

    ) {

        BackHandler {
            backBehaviour()
        }

        Spacer(modifier = Modifier.height(10.dp))

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

                        /*Button(
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
                                        Text(stringResource(id = R.string.verbs))
                                    }

                                    Row(

                                    ) {
                                        Text("50")
                                    }
                                }

                            },
                        )

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
                                        Text(stringResource(id = R.string.phrases))
                                    }

                                    Row(

                                    ) {
                                        Text("50")
                                    }
                                }

                            },
                        )*/

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

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            /*---------------------------SECOND LAYER------------------------------------------*/

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(

                ) {
                    /*--Left column with 'revision' and button--*/

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
                }

                Row(
                    modifier = Modifier
                        .padding(top = 36.dp) //Why have I had to do this and it doesn't align bottom when I tell it to?
                ) {
                    /*--Right column with 'exam' button--*/

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

        Spacer(modifier = Modifier.height(10.dp))


        /*---------------------------FOURTH LAYER------------------------------------------*/

       /* Column(

        ) {

            val lazyListState = rememberLazyListState()
            LazyRow(
                modifier = Modifier
                    //.padding(10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                state = lazyListState,
                flingBehavior = rememberSnapperFlingBehavior(lazyListState),
            )
            {
                items(
                    newsItems
                ) { item ->
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .fillParentMaxWidth(1f)
                            .wrapContentHeight()
                            .padding(all = 16.dp),
                        elevation = 6.dp,
                        backgroundColor = MaterialTheme.colors.onSurface,
                    ) {
                        *//*--Rounded corner card housing API info--*//*

                        Column(
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colors.secondary
                                )
                                .padding(all = 5.dp),
                        ) {

                            Column(
                            ) {

                                Row(

                                )
                                {


                                    Column(

                                    ) {


                                        Text(
                                            modifier = Modifier
                                                .padding(start = 5.dp, end = 5.dp),
                                            text = stringResource(
                                                id = R.string.today_in,
                                                user.studyCountry
                                            ),
                                            style = MaterialTheme.typography.body1,
                                            color = MaterialTheme.colors.secondary
                                        )


                                    }
                                }

                                AsyncImage(
                                    modifier = Modifier
                                        .fillParentMaxWidth()
                                        .padding(all = 5.dp)
                                        .border(
                                            2.dp,
                                            color = MaterialTheme.colors.secondary,
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                        .clip(RoundedCornerShape(5.dp)),
                                    model = item.image,
                                    contentDescription = null,
                                )



                                Row(
                                    modifier = Modifier
                                        .padding(all = 5.dp),
                                ) {

                                    Text(
                                        modifier = Modifier
                                            .clipToBounds(),
                                        text = item.title,
                                        style = MaterialTheme.typography.body1,
                                        color = MaterialTheme.colors.secondary,
                                        overflow = TextOverflow.Visible,
                                        softWrap = true,
                                    )
                                }

                            }

                        }


                    }
                }

            }

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
                        totalDots = newsItems.size,
                        selectedIndex = lazyListState.firstVisibleItemIndex,
                        selectedColor = selectedNewsColour,
                        unSelectedColor = unselectedNewsColour,
                    )

                }
            }


        }*/
    }

}




