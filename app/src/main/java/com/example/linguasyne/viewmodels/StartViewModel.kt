package com.example.linguasyne.viewmodels

import android.util.Log
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

    fun loginCheck(): Boolean {
        var loggedIn = false
        try {
            FirebaseAuth.getInstance().currentUser?.let {
                FirebaseManager.currentUser = User(it.email ?: "")
                loggedIn = true
            }
        } catch (e: Exception) {
            Log.e("StartViewModel", "$e")
            loggedIn = false
        }
        return loggedIn
    }

    fun signOut() {
        FirebaseManager.signOutUser()
        navController.navigate(ComposableDestinations.LOGIN)
    }

}
