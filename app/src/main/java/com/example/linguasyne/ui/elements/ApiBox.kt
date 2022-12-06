package com.example.linguasyne.ui.elements

import android.graphics.Color
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.linguasyne.viewmodels.ApiViewModel
import com.example.linguasyne.ui.animations.AnimateLoading
import com.google.accompanist.pager.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ApiBox(
    modifier: Modifier = Modifier,
    viewModel: ApiViewModel,
    pagerState: PagerState,
) {

    val minimumBoxHeight = 310
    val maximumBoxHeight = 380

    AnimateLoading(
        animate = viewModel.showLoadingAnim,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
    )

    Column(
        modifier = modifier,
    ) {

        HorizontalPager(
            state = pagerState,
            count = viewModel.news.size,
        ) { page ->
            val item = viewModel.news[page]
            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    //.wrapContentHeight(unbounded = true)
                    .requiredHeightIn(min = minimumBoxHeight.dp, max = maximumBoxHeight.dp)
                    .padding(all = 16.dp),
                elevation = 6.dp,
                backgroundColor = MaterialTheme.colors.onSurface,
            ) {
                Column(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colors.secondary
                        )
                        .padding(all = 5.dp),
                ) {
                    AsyncImage(
                        modifier = Modifier
                            //.fillParentMaxWidth()
                            //.fillMaxSize()
                            //.fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(all = 8.dp)
                            /*.border(
                                2.dp,
                                color = MaterialTheme.colors.secondary,
                                shape = RoundedCornerShape(5.dp)
                            )*/
                            .clip(RoundedCornerShape(5.dp))
                            .clipToBounds(),
                        model = item.image,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                    )

                    Text(
                        modifier = Modifier
                            .padding(all = 8.dp),
                        text = item.title,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary,
                        overflow = TextOverflow.Visible,
                        softWrap = true,
                    )

                }
            }
        }


    }
}
