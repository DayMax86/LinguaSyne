package com.example.linguasyne.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.VocabSearchViewModel
import kotlin.reflect.KFunction1

class VocabSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = VocabSearchViewModel()

        setContent {
            viewModel.onActivityLaunch()
            VocabCardPress(lTV = viewModel.launchTermView)
            LinguaSyneTheme(darkTheme = false) {
                Surface(
                    //modifier = Modifier
                      //  .background(Color.Red)
                )
                {
                    DisplayVocab(
                        vocabItems = viewModel.vocabList,
                        onClick = viewModel::handlePress,
                    )
                }
            }
        }

    }

    @Composable
    fun VocabCardPress(lTV: Boolean) {
        Log.e("VocabSearch", "VocabCardPress called, checking lTV boolean")
        if (lTV) {
            Log.e("VocabSearch", "attempting to start activity")
            val intent = Intent(this, DisplayTermActivity::class.java)
            startActivity(intent)
        }
    }

    @Composable
    fun DisplayVocab(
        vocabItems: MutableList<Vocab>,
        onClick: (Vocab) -> Unit,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(all = 10.dp)
        ) {
            items(
                items = vocabItems,
                itemContent = {
                    VocabItem(vocab = it, onClick = onClick)
                }
            )
        }
    }

    @Composable
    fun VocabItem(
        vocab: Vocab,
        onClick: (Vocab) -> Unit,
    ) {
        Card(
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxWidth()
                .clickable {
                    onClick(vocab)
                    Log.e("VocabSearch", "within .clickable")
                           },
            elevation = 5.dp,
            backgroundColor = MaterialTheme.colors.background,
            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
            )
        {
            Row {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = vocab.name,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h1,
                    )
                    Text(
                        text = "Unlock level: ${vocab.unlock_level.toString()}",
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Current level: ${vocab.current_level_term}",
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.body1,
                    )

                }

            }
        }
    }

}