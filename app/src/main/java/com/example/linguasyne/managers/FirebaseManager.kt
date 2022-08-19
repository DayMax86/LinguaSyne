package com.example.linguasyne.managers

import android.content.ContentResolver
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeResource
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.linguasyne.R
import com.example.linguasyne.classes.Term
import com.example.linguasyne.classes.User
import com.example.linguasyne.classes.Vocab
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.storage.FirebaseStorage
import java.net.URL

object FirebaseManager {

    lateinit var current_user: User

    fun loadVocabFromFirebase() {
        Log.d("VocabSearchActivity", "Before attaching listener")
        val ref = FirebaseFirestore.getInstance()
        ref.collection("vocab")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("VocabSearchActivity", "Inside OnSuccessListener")
                val results = mutableListOf<Vocab>()
                Log.d("FirebaseManager", "OnSuccessListener triggered!")
                for (document in documents) {
                    Log.d("FirebaseManager", "${document.id} => ${document.data}")
                    val vocab_translations = document.get("translations")
                    val vocab_mnemonics = document.get("mnemonics")
                    val vocab_genders = document.get("genders")
                    val vocab_types = document.get("types")
                    val vocab = Vocab(
                        document.getField<String>("id").toString(),
                        document.getField<String>("name").toString(),
                        document.getField<Int>("unlock_level")!!.toInt(),
                        vocab_translations as List<String>,
                        vocab_mnemonics as List<String>,
                        vocab_genders as List<String>,
                        vocab_types as List<String>
                    )
                    results.add(vocab)
                }
                VocabRepository.currentVocab = results
                VocabRepository.allVocab = results.sortedBy {
                    it.name
                }
            }
            .addOnFailureListener { exception ->
                Log.d("VocabSearchActivity", "Inside OnFailureListener")
                Log.d("VocabSearchActivity", "Error getting documents: ", exception)
            }
        Log.d("VocabSearchActivity", "After attaching listener")
    }

    fun addVocabToFirebase(term: Term) {
        FirebaseFirestore.getInstance()
            .collection("vocab")
            .add(term)
            .addOnSuccessListener {
                Log.d("HomeActivity", "Vocab item #" + "$term.id" + " added to firebase")
            }
    }

    fun uploadUserImageToFirebase(uri: Uri?) {
        val filename = "profileImage"
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val storageRef =
            FirebaseStorage.getInstance()
                .getReference("/users/${firebaseUser?.uid}/image/$filename")
        if (uri != null) {
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    Log.d("HomeActivity", "User image successfully uploaded to Firebase")
                    Log.d("HomeActivity", "User image uri = $uri")
                    Log.d(
                        "HomeActivity",
                        "User image path = /users/${firebaseUser?.uid}/image/$filename"
                    )
                }

            val imageRef = storageRef.child("/users/${firebaseUser?.uid}/image/$filename")

            current_user.user_image_uri = uri
            Log.d("HomeActivity", "Current user's image has been set")

        } else {
            Log.e("HomeActivity", "Image upload to Firebase UNSUCCESSFUL, uri is null!")
        }

    }

    fun getUserImageFromFirebase(ctxRes: ContentResolver): Bitmap {
        if (current_user.user_image_uri != null) {
            Log.d("HomeActivity", "Image successfully retrieved")
            return MediaStore.Images.Media.getBitmap(ctxRes, current_user.user_image_uri)
        } else {
            //Return a default image
            // TODO() Why can I not use a default image from R.drawable? (without context)
            Log.e("HomeActivity", "No user image so default image has to be used")
            return Bitmap.createBitmap(300,300,Bitmap.Config.ARGB_8888)
        }
    }

    fun createNewAccount(email: String, password: String): Boolean {

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        var success = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Sign in success, update UI with the signed-in user's information
                Log.d("CreateAccountActivity", "createUserWithEmail:SUCCESS, $email")
                val user = User(email)
                current_user = user
                addUserToFirebase(user)
                // Let the source of the function call know if the account has been made successfully or not
                // so it can update the ui accordingly
                success = true
            }
            .addOnFailureListener {
                // If sign in fails, display a message to the user.
                Log.e(
                    "CreateAccountActivity",
                    "createUserWithEmail:FAILURE",
                )
                success = false
            }
        return success
    }

    fun addUserToFirebase(user: User) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("CreateAccountActivity", "User added to Firestore")
            }
            .addOnFailureListener {
                Log.e("CreateAccountActivity", it.toString())
            }
    }

    fun logInUser(email: String, password: String): Boolean {
        var success = true

        if (email != "" && password != "") {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    Log.d("LoginActivity", "User logged in")
                    success = true
                    current_user = User(email)
                }
                .addOnFailureListener {
                    success = false
                    Log.e("LoginActivity", "User log in failed: $it")
                }
        } else {
            Log.e("LoginActivity", "Either email or password is null")
        }

        return success
    }

}