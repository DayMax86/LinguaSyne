package com.daymax86.linguasyne.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

//TODO(Not yet implemented) Framework for help button functionality
open class BaseViewModel: ViewModel() {
    open var helpText by mutableStateOf("test")

    open fun drawerContent() {
    }

    open fun showHelp() {
    }
}

