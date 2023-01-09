package com.daymax86.linguasyne.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.daymax86.linguasyne.classes.Vocab
import com.daymax86.linguasyne.enums.ComposableDestinations
import com.daymax86.linguasyne.managers.VocabRepository

class VocabSearchViewModel(
    private val navController: NavController,
): BaseViewModel() {

    var showLoadingAnim: Boolean by mutableStateOf(false) //Not currently used

    lateinit var vocabList: MutableList<Vocab>

    /*override fun showHelp() {
        helpText = "Help!"
    } */ //TODO(Not yet implemented)

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