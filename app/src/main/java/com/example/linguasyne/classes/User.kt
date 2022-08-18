package com.example.linguasyne.classes

import android.graphics.Bitmap
import java.util.*

class User(email: String) {

    var user_image: Bitmap? = null
    val user_id: String
    val user_email: String
    var user_level: Int = 0

    init{
        this.user_email = email
        user_id = UUID.randomUUID().toString()
    }

}