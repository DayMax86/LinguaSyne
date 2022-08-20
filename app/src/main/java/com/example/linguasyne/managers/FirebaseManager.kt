package com.example.linguasyne.managers

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.linguasyne.classes.Term
import com.example.linguasyne.classes.User
import com.example.linguasyne.classes.Vocab
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

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
                .getReference("/users/${current_user.user_id}/image/$filename")
        Log.e("HomeActivity", "current_user.user_id = ${current_user.user_id}")
        if (uri != null) {
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    Log.d("HomeActivity", "User image successfully uploaded to Firebase")
                    Log.d("HomeActivity", "User image uri = $uri")
                    Log.d(
                        "HomeActivity",
                        "User image path = /users/${current_user.user_id}/image/$filename"
                    )
                }


            if (firebaseUser != null) {
                val userImageRef = Firebase.firestore.collection("users").document("${firebaseUser.email}")
                userImageRef
                    .update("user_image_uri", uri)
                    .addOnSuccessListener {
                        Log.d("HomeActivity", "user_image_uri field on Firebase has been set for ${current_user.user_email}")
                    }
                    .addOnFailureListener{
                        Log.e("HomeActivity", "Failed to update user_image_uri on Firebase: $it")
                    }
            } else {
                Log.e("HomeActivity", "No firebaseUser is logged in!!")
            }

            current_user.user_image_uri = uri
            Log.d("HomeActivity", "Current user's image has been set")

        } else {
            Log.e("HomeActivity", "Image upload to Firebase UNSUCCESSFUL, uri is null!")
        }

        //

    }

    fun getUserImageFromFirebase(cntRes: ContentResolver) {
        // TODO() I believe this is being called before the uri is set (in the method above)...
        // TODO() ... since the 'upload image to firebase' method is called FROM the HomeActivity.
        // TODO() One solution would be to have the user upload an image when they create an account...
        // TODO() ... but then they wouldn't be able to upload one at any later opportunity. Hmmm.....
        if (current_user.user_image_uri != null) {
            cntRes.takePersistableUriPermission(
                current_user.user_image_uri!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            Log.d("HomeActivity", "Image successfully retrieved")
            current_user.user_bitmap = MediaStore.Images.Media.getBitmap(cntRes, current_user.user_image_uri)
        } else {
            //Return a default image
            // TODO() Why can I not use a default image from R.drawable? (without context)
            Log.e("HomeActivity", "No user image so default image has to be used")
        }
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