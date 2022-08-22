package com.example.linguasyne.classes

import android.graphics.Bitmap
import android.net.Uri
import com.example.linguasyne.managers.FirebaseManager
import com.google.firebase.storage.StorageReference
import java.util.*

class User(email: String) {

    var user_image_uri: Uri? = null
    var user_id: String = ""
    val user_email: String
    var user_level: Int = 0

    init{
        this.user_email = email
        generateId()
    }

    fun generateId() {
        user_id = user_email
    }

}