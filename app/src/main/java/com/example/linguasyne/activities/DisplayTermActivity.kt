package com.example.linguasyne.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.linguasyne.*
import com.example.linguasyne.R
import com.example.linguasyne.classes.Term
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.enums.Gender
import com.example.linguasyne.managers.LessonManager
import com.example.linguasyne.managers.VocabRepository
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.DisplayTermViewModel
import com.example.linguasyne.viewmodels.VocabSearchViewModel

open class DisplayTermActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = DisplayTermViewModel()

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
                    )
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        val viewModel = DisplayTermViewModel()
        viewModel.onActivityEnd()
    }


    @Composable
    fun DisplayTerm(
        term: Term,
        onClickLeft: () -> Unit,
        onClickRight: () -> Unit,
    ) {


        Column(
            modifier = Modifier
                .border(width = 1.dp, color = Color.Yellow)
        ) {

            //------------------------------ FIRST ROW --------------------------------//

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.Red),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.Red),
                ) {
                    Image(
                        modifier = Modifier
                            .clickable { onClickLeft }
                            .padding(start = 10.dp, top = 10.dp)
                            .height(100.dp),
                        painter = painterResource(id = R.drawable.opaqueleftarrow),
                        contentDescription = null,
                        alignment = Alignment.CenterStart,
                    )
                }

                Row(
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.Red)
                )
                {
                    Column(
                        modifier = Modifier.border(width = 1.dp, color = Color.Yellow),
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
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.Red),
                    //verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .clickable { onClickRight }
                            .padding(end = 10.dp, top = 10.dp)
                            .height(100.dp),
                        //.padding(10.dp),
                        painter = painterResource(id = R.drawable.opaquerightarrow),
                        contentDescription = null,
                        alignment = Alignment.CenterStart,
                    )
                }

            }


            //------------------------------ SECOND ROW --------------------------------//

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Yellow)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = Color.Red),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Row(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Red),
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
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Red),
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
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Yellow)
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
                    //How to get all translations from the term's translations list without breaking MVVM structure??
                    onValueChange = { /**/ },
                    enabled = false,
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            //---------------------- FOURTH ROW ----------------------------//

            Column(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Yellow)
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
                    .border(width = 1.dp, color = Color.Yellow)
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
                        .fillMaxWidth()
                        .border(width = 1.dp, color = Color.Red),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Red)
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
                            .border(width = 1.dp, color = Color.Red)
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
                    .border(width = 1.dp, color = Color.Yellow)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = Color.Red),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Red)
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
                            .border(width = 1.dp, color = Color.Red)
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


    /*
    @Composable
    fun LeftArrow(show: Boolean, onClick: () -> Unit) {
        if (show) {

            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.opaqueleftarrow),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(all = 10.dp)
                    .size(32.dp)

            )
        }
    }


    @Composable
    fun RightArrow(show: Boolean, onClick: () -> Unit) {
        if (show) {

            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .width(200.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.opaquerightarrow),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(all = 10.dp)
                    .size(32.dp)
                    .clickable { onClick }
            )
        }
    }
    */



}


