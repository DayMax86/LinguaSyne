package com.example.linguasyne.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.managers.VocabRepository

class VocabSearchViewModel {

    var vocabList by mutableStateOf(mutableListOf<Vocab>())

    var launchTermView by mutableStateOf(false)

    fun onActivityLaunch() {
        vocabList = VocabRepository.allVocab as MutableList<Vocab>
        Log.e("VocabSearch", "onActivityLaunch called, launchTermView = $launchTermView")
    }

    fun handlePress(item: Vocab) {
        VocabRepository.filterById(item.id)
        launchTermView = true
        Log.e("VocabSearch", "handlePress called, launchTermView = $launchTermView")
    }

}