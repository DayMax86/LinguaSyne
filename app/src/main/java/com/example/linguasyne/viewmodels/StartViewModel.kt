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

    fun login() {
        //animateLoading = true
        //delay(2000) //TODO() This fixes the issue, but a hardcoded time delay is not the solution!!
        if (loginCheck()) {
            goToHome()
        } else {
            goToLogin()
        }
        //animateLoading = false
    }

    fun loginCheck(): Boolean {
        return try {
            FirebaseAuth.getInstance().currentUser?.let {
                FirebaseManager.currentUser = User(it.email ?: "")
                true
            }
            false
        } catch (e: Exception) {
            false
        }
    }

    private fun goToHome() {
        navController.navigate(ComposableDestinations.HOME)
    }

    private fun goToLogin() {
        navController.navigate(ComposableDestinations.LOGIN)
    }

}