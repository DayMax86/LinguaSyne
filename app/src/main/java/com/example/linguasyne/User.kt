package com.example.linguasyne

import java.util.*

class User(email: String) {

    var user_id: String = ""
    var user_email: String = email
    var user_level: Int = 0

    init{
        this.user_level = 0
        this.user_email = email

        user_id = UUID.randomUUID().toString()
    }

}