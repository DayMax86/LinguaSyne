package com.daymax86.linguasyne.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.daymax86.linguasyne.classes.User
import com.daymax86.linguasyne.enums.ComposableDestinations
import com.daymax86.linguasyne.managers.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class StartViewModel(
    private val navController: NavHostController,
) : ViewModel() {

    var darkMode by mutableStateOf(false)
    var showSettings by mutableStateOf(false)

    fun loginCheck(): Boolean {
        var loggedIn = false
        try {
            FirebaseAuth.getInstance().currentUser?.let {
                FirebaseManager.currentUser = User(it.email!!.lowercase().filter { !it.isWhitespace() } ?: "")
                loggedIn = true
                fetchUserDarkModePreference()
            }
        } catch (e: Exception) {
            Log.e("StartViewModel", "$e")
            loggedIn = false
        }
        return loggedIn
    }

    fun aboutLinguaSyne(uriHandler: UriHandler) {
        uriHandler.openUri("https://github.com/DayMax86/LinguaSyne")
    }

    fun signOut() {
        FirebaseManager.signOutUser()
        navController.navigate(ComposableDestinations.LOGIN)
    }

    fun toggleSettings() {
        showSettings = !showSettings
    }

    fun toggleDarkMode(isDark: Boolean) {
        darkMode = isDark
        viewModelScope.launch {
            FirebaseManager.currentUser?.let {
                it.darkModeEnabled = isDark
                FirebaseManager.updateUserDarkModeChoice()
            }
        }
    }

    private fun fetchUserDarkModePreference() {
        viewModelScope.launch {
            darkMode = FirebaseManager.getUserDarkModePreference()
        }
    }

}
