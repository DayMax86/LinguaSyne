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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
            LeftArrow(show = viewModel.enabledLeftArrow, onClick = { viewModel.loadPrev() })
            RightArrow(show = viewModel.enabledRightArrow, onClick = { viewModel.loadNext() })
            LinguaSyneTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                )
                {
                    DisplayTerm(
                        viewModel.termToDisplay
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
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp),
            elevation = 2.dp,
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("<")
                //In this row - left arrow image, term name and translation, right arrow

                Column() {
                    Text(
                        modifier = Modifier
                            .padding(all = 10.dp),
                            //.align(Alignment.CenterVertically),
                        text = term.name,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h1,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        modifier = Modifier
                            .padding(all = 10.dp),
                            //.align(Alignment.CenterVertically),
                        text = term.translations[0],
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.body1,
                    )
                }
                Text(">")
            }
        }
    }

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
                    .clickable { onClick }
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


}


