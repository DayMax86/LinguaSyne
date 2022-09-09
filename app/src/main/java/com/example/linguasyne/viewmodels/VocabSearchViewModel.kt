package com.example.linguasyne.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.linguasyne.classes.Vocab
import com.example.linguasyne.enums.ComposableDestinations
import com.example.linguasyne.managers.VocabRepository

class VocabSearchViewModel(
    private val navController: NavController,
): ViewModel() {


    lateinit var vocabList: MutableList<Vocab>

    fun onSearchLaunch() {
        vocabList = VocabRepository.allVocab as MutableList<Vocab>
    }

    fun handleCardPress(item: Vocab) {
        VocabRepository.filterById(item.id)
        navController.navigate(ComposableDestinations.TERM_DISPLAY)
    }

    fun handleBackPress() {
        navController.navigate(ComposableDestinations.HOME)
    }



}