package com.example.linguasyne.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.linguasyne.R
import com.example.linguasyne.classes.Term
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.DisplayTermViewModel

open class DisplayTermActivity : AppCompatActivity() {

    val viewModel = DisplayTermViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            viewModel.onActivityLaunch()
            LinguaSyneTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxHeight()
                )
                {
                    DisplayTerm(
                        viewModel.termToDisplay,
                        viewModel::loadPrev,
                        viewModel::loadNext,
                        viewModel.leftArrowImage,
                        viewModel.rightArrowImage,
                    )
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onActivityEnd()
    }


    @Composable
    fun DisplayTerm(
        term: Term,
        onClickLeft: () -> Unit,
        onClickRight: () -> Unit,
        leftArrowImage: Int,
        rightArrowImage: Int,
    ) {


        Column(
        ) {

            //------------------------------ FIRST ROW --------------------------------//

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .clickable { onClickLeft() }
                            .padding(start = 10.dp, top = 10.dp)
                            .height(100.dp),
                        model = leftArrowImage,
                        contentDescription = null,
                        alignment = Alignment.CenterStart,
                    )
                }

                Row(
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

                Row(
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .clickable { onClickRight() }
                            .padding(end = 10.dp, top = 10.dp)
                            .height(100.dp),
                        //.padding(10.dp),
                        model = rightArrowImage,
                        contentDescription = null,
                        alignment = Alignment.CenterStart,
                    )
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
            ) {

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = "Translations",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondary,
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .wrapContentHeight()
                        .border(
                            2.dp,
                            color = MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .background(color = MaterialTheme.colors.onBackground),
                    value = "",
                    //TODO() How to get all translations from the term's translations list without breaking MVVM structure??
                    onValueChange = { /**/ },
                    enabled = false,
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            //---------------------- FOURTH ROW ----------------------------//

            Column(
            ) {

                Text(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    text = "Mnemonics",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondary,
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .wrapContentHeight()
                        .border(
                            2.dp,
                            color = MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .background(color = MaterialTheme.colors.onBackground),
                    value = "",
                    //How to get all mnemonics from the term's mnemonics list without breaking MVVM structure??
                    onValueChange = { /**/ },
                    enabled = false,
                )

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
                    text = "Your progress",
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
                            text = term.current_level_term.toString(),
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(10.dp),
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(end = 5.dp),
                            text = (term.current_level_term + 1).toString(),
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
                            text = "Next review",
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
                            text = term.next_review.toString(),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.secondary,
                        )

                    }
                }

            }


        }

    }


}


