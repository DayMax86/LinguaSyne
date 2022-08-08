package com.example.linguasyne

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val test = MutableLiveData<String>()

    init {
        Firebase.database("https://linguasyne-default-rtdb.firebaseio.com/")
            .getReference("/test")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    test.value = snapshot.value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("ViewModel", "Error: $error")
                }
            })
    }

    fun handleSubmit() {
        viewModelScope.launch {
            test.value = "loading..."
            delay(2000)
        }
    }

}