package com.example.linguasyne.classes

import android.net.Uri

class User(email: String = "") {

    var imageUri: Uri? = null
    var id: String = ""
    val email: String
    var level: Int = 0
    var streak: Int = 0
    var studyCountry: String = "France"

    init{
        this.email = email
        generateId()
    }

    fun generateId() {
        id = email
    }

}