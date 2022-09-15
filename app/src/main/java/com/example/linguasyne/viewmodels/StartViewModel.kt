package com.example.linguasyne.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.linguasyne.classes.User
import com.example.linguasyne.enums.ComposableDestinations
import com.example.linguasyne.managers.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartViewModel(
    private val navController: NavHostController,
) : ViewModel() {

    var animateLoading by mutableStateOf(true)

    fun init() {
        animateLoading = true
        viewModelScope
            .launch {
                delay(2000) //TODO() This fixes the issue, but a hardcoded time delay is not the solution!!
                if (loginCheck()) {
                    goToHome()
                } else {
                    goToLogin()
                }
                animateLoading = false
            }
    }

    private fun loginCheck(): Boolean {
        var loggedIn = false
        viewModelScope.launch {
            try {
                val cu = FirebaseAuth.getInstance().currentUser
                if (cu != null) {
                    FirebaseManager.currentUser = User(cu.email!!)
                    loggedIn = true
                }
            } catch (e: Exception) {
                loggedIn = false
            }
        }
        return loggedIn
    }

    private fun goToHome() {
        navController.navigate(ComposableDestinations.HOME)
    }

    private fun goToLogin() {
        navController.navigate(ComposableDestinations.LOGIN)
    }

}