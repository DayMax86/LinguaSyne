package com.example.linguasyne.managers

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.linguasyne.classes.Term
import com.example.linguasyne.classes.User
import com.example.linguasyne.classes.Vocab
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

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
                Log.e("VocabSearchActivity", "Error getting documents: ", exception)
            }
        Log.d("VocabSearchActivity", "After attaching listener")
    }

    fun addVocabToFirebase(term: Term) {
        FirebaseFirestore.getInstance()
            .collection("vocab")
            .add(term)
            .addOnSuccessListener {
                Log.d("HomeActivity", "Vocab item #" + term.id + " added to firebase")
            }
    }

    fun uploadUserImageToFirebaseStorage(localUri: Uri?) {
        val filename = "profileImage"
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val storageRef =
                FirebaseStorage.getInstance()
                    .getReference("/users/${current_user.user_id}/image/$filename")
            Log.e("HomeActivity", "current_user.user_id = ${current_user.user_id}")
            if (localUri != null) {
                storageRef.putFile(localUri)
                    .addOnSuccessListener {
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
        updateUserImageUriOnFirestore()
    }

    fun updateUserImageUriOnFirestore() {
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
                        Log.d(
                            "HomeActivity",
                            "user_image_uri field on Firebase has assigned value: $uri"
                        )
                    }
                    .addOnFailureListener {
                        Log.e("HomeActivity", "Failed to update user_image_uri on Firebase: $it")
                        setDefaultUserImageUri()
                    }


            }


    }

    fun setDefaultUserImageUri() {

        current_user.user_image_uri =
            Uri.parse("https://firebasestorage.googleapis.com/v0/b/linguasyne.appspot.com/o/default%2FAngrySteveEmote.png?alt=media&token=b3ba69f5-0ba8-41f4-9449-b58327b7f4d0")

        Log.e(
            "HomeActivity",
            "Default image URL fetched from Firestore"
        )

    }


    fun createNewAccount(email: String, password: String): Boolean {

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        var success = false

        /*------------ ADD TO FIREBASE 'AUTHENTICATION' -------------*/
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Sign in success, update UI with the signed-in user's information
                Log.d("CreateAccountActivity", "createUserWithEmail:SUCCESS, $email")
                val user = User(email)
                current_user = user
                /*------------ ADD TO FIREBASE 'FIRESTORE' -------------*/
                addUserToFirestore(user)
                setDefaultUserImageUri()
                // Let the source of the function call know if the account has been made successfully or not
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

    fun addUserToFirestore(user: User) {
        FirebaseFirestore.getInstance()
            .collection("users").document(current_user.user_id)
            .set(user)
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