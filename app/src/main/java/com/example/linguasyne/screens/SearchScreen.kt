package com.example.linguasyne.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.linguasyne.R
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.ui.animations.AnimateLoading
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.VocabSearchViewModel

@Composable
fun SearchScreen(
    viewModel: VocabSearchViewModel,
) {
    viewModel.onSearchLaunch()

    Surface()
    {
        DisplayVocab(
            vocabItems = viewModel.vocabList,
            onClick = viewModel::handleCardPress,
            onBackPress = viewModel::handleBackPress,
            showAnim = viewModel.showLoadingAnim,
        )
    }
    
}


@Composable
fun DisplayVocab(
    vocabItems: MutableList<Vocab>,
    onClick: (Vocab) -> Unit,
    onBackPress: () -> Unit,
    showAnim: Boolean,
) {

    BackHandler {
        onBackPress()
    }

    LazyColumn(
        contentPadding = PaddingValues(all = 10.dp)
    ) {
        items(
            items = vocabItems,
            itemContent = {
                VocabItem(
                    vocab = it,
                    onClick = onClick,
                    showAnim = showAnim,
                )
            }
        )
    }
}

@Composable
fun VocabItem(
    vocab: Vocab,
    onClick: (Vocab) -> Unit,
    showAnim: Boolean,
) {
    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .border(
                2.dp,
                MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(size = 10.dp)
            )
            .fillMaxWidth()
            .clickable {
                onClick(vocab)
            },
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.onBackground,
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
    )
    {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            AnimateLoading(
                animate = showAnim,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
            )

            Text(
                text = vocab.name,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h1,
                overflow = TextOverflow.Visible,
                softWrap = true,
            )

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = stringResource(id = R.string.unlock_level) + " ${vocab.unlockLevel}",
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    text = stringResource(id = R.string.your_level) + " ${FirebaseManager.currentUser!!.level}",
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.body1,
                )
            }


        }
    }
}
