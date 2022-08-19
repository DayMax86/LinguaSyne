package com.example.linguasyne.viewmodels


import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.linguasyne.activities.HomeActivity
import com.example.linguasyne.classes.User
import com.example.linguasyne.managers.FirebaseManager


class HomeViewModel : ViewModel() {

    var user: User by mutableStateOf(FirebaseManager.current_user)
    var userBitmap: Bitmap by mutableStateOf(FirebaseManager.getUserImageFromFirebase())

    fun handleHelpClick() {
        //Do something
    }


}