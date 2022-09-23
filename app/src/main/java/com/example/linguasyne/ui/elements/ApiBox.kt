package com.example.linguasyne.ui.elements

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.linguasyne.R
import com.example.linguasyne.classes.User
import com.example.linguasyne.viewmodels.ApiViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior


@OptIn(ExperimentalSnapperApi::class)
@Composable
fun ApiBox(
    user: User,
    selectedNewsColour: Color,
    unselectedNewsColour: Color,
) {

    val viewModel = remember { ApiViewModel() }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
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
                viewModel.news
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
                    //--Rounded corner card housing API info--//

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
                                    modifier = Modifier
                                        .fillMaxWidth()
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
                                    //.fillParentMaxWidth()
                                    //.fillMaxSize()
                                    .padding(all = 5.dp)
                                    .border(
                                        2.dp,
                                        color = MaterialTheme.colors.secondary,
                                        shape = RoundedCornerShape(5.dp)
                                    ),
                                /*.clip(RoundedCornerShape(5.dp))
                                .clipToBounds()*/
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
                    totalDots = viewModel.news.size,
                    selectedIndex = lazyListState.firstVisibleItemIndex,
                    selectedColor = selectedNewsColour,
                    unSelectedColor = unselectedNewsColour,
                )

            }
        }


    }

}
