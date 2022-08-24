package com.example.linguasyne.managers

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.linguasyne.activities.LoginActivity
import com.example.linguasyne.classes.Term
import com.example.linguasyne.classes.User
import com.example.linguasyne.classes.Vocab
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import okhttp3.internal.wait

object FirebaseManager {

    var current_user: User = User("")

    fun loadVocabFromFirebase() {
        val ref = FirebaseFirestore.getInstance()
        ref.collection("vocab")
            .get()
            .addOnSuccessListener { documents ->
                val results = mutableListOf<Vocab>()
                for (document in documents) {
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
                Log.e("VocabSearchActivity", "Error getting documents: ", exception)
            }
    }

    fun addVocabToFirebase(term: Term) {
        FirebaseFirestore.getInstance()
            .collection("vocab")
            .add(term)
            .addOnSuccessListener {
                Log.d("HomeActivity", "Vocab item #" + term.id + " added to firebase")
            }
    }

    fun uploadUserImageToFirebaseStorage(localUri: Uri?, imageUploaded: (Uri?) -> Unit) {
        val filename = "profileImage"
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val storageRef =
                FirebaseStorage.getInstance()
                    .getReference("/users/${current_user.user_id}/image/$filename")
            Log.d("HomeActivity", "current_user.user_id = ${current_user.user_id}")
            if (localUri != null) {
                storageRef.putFile(localUri)
                    .addOnSuccessListener {
                        updateUserImageUriOnFirestore(imageUploaded)
                        Log.d("HomeActivity", "User image successfully uploaded to Firestore")
                        Log.d(
                            "HomeActivity",
                            "User image path on firestore = /users/${current_user.user_id}/image/$filename"
                        )
                    }
            }
        } else {
            Log.e("HomeActivity", "No firebaseUser is logged in!!")
        }
    }

    private fun updateUserImageUriOnFirestore(imageUploaded: (Uri?) -> Unit) {
        val filename = "profileImage"
        val firebaseUser = current_user

        val firestoreRef =
            Firebase.firestore.collection("users").document(firebaseUser.user_email)
        // firestoreRef is the user's document on firestore, NOT the image in storage

        FirebaseStorage.getInstance().getReference()
            .child("users/${current_user.user_id}/image/$filename").downloadUrl
            .addOnSuccessListener { uri ->

                firestoreRef
                    .update("user_image_uri", uri)
                    .addOnSuccessListener {
                        Log.d(
                            "HomeActivity",
                            "user_image_uri field on Firebase has been set for ${current_user.user_email}"
                        )
                        current_user.user_image_uri = uri
                        imageUploaded(uri)
                        Log.d(
                            "HomeActivity",
                            "user_image_uri field on Firebase has assigned value: $uri"
                        )
                    }
                    .addOnFailureListener {
                        Log.e("HomeActivity", "Failed to update user_image_uri on Firebase: $it")
                        imageUploaded(getDefaultUserImageUri())

                    }


            }


    }

    fun getDefaultUserImageUri(): Uri {
        return Uri.parse("https://firebasestorage.googleapis.com/v0/b/linguasyne.appspot.com/o/default%2FAngrySteveEmote.png?alt=media&token=b3ba69f5-0ba8-41f4-9449-b58327b7f4d0")
    }


    fun getUserImageFromFirestore(imageFetched: (Uri?) -> Unit){
        val firebaseUser = current_user
        val firestoreRef =
            Firebase.firestore.collection("users").document(firebaseUser.user_email)

        firestoreRef.get().addOnSuccessListener {
            current_user.user_image_uri = Uri.parse(it.get("user_image_uri") as String?)
            imageFetched(current_user.user_image_uri)
        }

    }


    fun createNewAccount(email: String, password: String, accountCreated: () -> Unit) {

        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        /*------------ ADD TO FIREBASE 'AUTHENTICATION' -------------*/
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Sign in success, update UI with the signed-in user's information
                Log.d("CreateAccountActivity", "createUserWithEmail:SUCCESS, $email")
                val user = User(email)
                current_user = user
                /*------------ ADD TO FIREBASE 'FIRESTORE' -------------*/
                addUserToFirestore(user) { accountCreated() }
            }
            .addOnFailureListener {
                // If sign in fails, display a message to the user.
                Log.e(
                    "CreateAccountActivity",
                    "createUserWithEmail:FAILURE",
                )
            }
    }


    fun addUserToFirestore(user: User, accountCreated: () -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("users").document(current_user.user_id)
            .set(user)
            .addOnSuccessListener {
                Log.d("CreateAccountActivity", "User added to Firestore (Overloaded function)")
                accountCreated()
            }
            .addOnFailureListener {
                Log.e("CreateAccountActivity", it.toString())
            }
    }

    fun logInUser(): Boolean {
        var success = false
        val cu = FirebaseAuth.getInstance().currentUser

        if (cu != null) {
            Log.d("LoginActivity", "User already logged in")
            success = true
            //Is this creating a new user without any of their data? If so their data needs to be fetched and assigned
            current_user = User(cu.email!!)
        } else {
            success = false
            Log.e("LoginActivity", "No current user logged in")
        }

        return success
    }

    //Overload function so there is a separate version for using the login button rather than the automatic login
    fun logInUser(email: String, password: String) {
//        var success = false

        if (email != "" && password != "") {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    Log.d("LoginActivity", "User logged in")
//                    success = true
                    current_user = User(email)
                }
                .addOnFailureListener {
//                    success = false
                    Log.e("LoginActivity", "User log in failed: $it")
                }
        } else {
            Log.e("LoginActivity", "Either email or password is null")
        }

//        return success
    }

    fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
        Log.d("HomeActivity", "User signed out")
        Log.d("HomeActivity", "Current user id: ${FirebaseAuth.getInstance().currentUser?.uid}")
    }

}