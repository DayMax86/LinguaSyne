package com.example.linguasyne.viewmodels


import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.linguasyne.R
import com.example.linguasyne.activities.HomeActivity
import com.example.linguasyne.classes.User
import com.example.linguasyne.managers.FirebaseManager


class HomeViewModel : ViewModel() {

    var user: User by mutableStateOf(FirebaseManager.current_user)
    var userImage: Uri? by mutableStateOf(FirebaseManager.current_user.user_image_uri)

    fun init() {

    }

    fun handleHelpClick() {
        //Do something
    }


}