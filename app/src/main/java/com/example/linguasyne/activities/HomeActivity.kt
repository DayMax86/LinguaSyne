package com.example.linguasyne.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import com.google.firebase.auth.FirebaseAuth
import com.example.linguasyne.managers.*
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.HomeViewModel
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.linguasyne.R
import com.example.linguasyne.classes.User

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = HomeViewModel()

        viewModel.init()

        setContent {
            LinguaSyneTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxHeight()
                ) {
                    DisplayHome(
                        viewModel.user,
                        viewModel::handleHelpClick,
                        viewModel.userImage,
                    )

                }
            }
        }

    }


    @Composable
    fun DisplayHome(
        user: User,
        onClickHelp: () -> Unit,
        userImage: Uri?,
    ) {
        Column(

        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Yellow)
                    .fillMaxWidth(),
            ) {
                /*---------------------------FIRST LAYER------------------------------------------*/
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = Color.Red),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Row(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Red)
                            .padding(start = 10.dp),
                    ) {
                        /*--Left column with 'lessons' and 3 buttons--*/

                        Column(
                            modifier = Modifier
                                .border(width = 1.dp, color = Color.Yellow),
                        ) {

                            Text(
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                                text = "Lessons",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondary,
                            )

                            Button(
                                modifier = Modifier
                                    .width(150.dp),
                                onClick = { },
                                shape = RoundedCornerShape(100),
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .border(width = 1.dp, color = Color.Red)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .border(width = 1.dp, color = Color.Red),
                                        ) {
                                            Text("Vocab")
                                        }

                                        Row(
                                            modifier = Modifier
                                                .border(width = 1.dp, color = Color.Red),
                                        ) {
                                            Text("50")
                                        }
                                    }

                                },
                            )

                            Button(
                                modifier = Modifier
                                    .width(150.dp),
                                onClick = { },
                                shape = RoundedCornerShape(100),
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .border(width = 1.dp, color = Color.Red)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .border(width = 1.dp, color = Color.Red),
                                        ) {
                                            Text("Verbs")
                                        }

                                        Row(
                                            modifier = Modifier
                                                .border(width = 1.dp, color = Color.Red),
                                        ) {
                                            Text("50")
                                        }
                                    }

                                },
                            )

                            Button(
                                modifier = Modifier
                                    .width(150.dp),
                                onClick = { },
                                shape = RoundedCornerShape(100),
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .border(width = 1.dp, color = Color.Red)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .border(width = 1.dp, color = Color.Red),
                                        ) {
                                            Text("Phrases")
                                        }

                                        Row(
                                            modifier = Modifier
                                                .border(width = 1.dp, color = Color.Red),
                                        ) {
                                            Text("50")
                                        }
                                    }

                                },
                            )

                        }
                    }

                    Row(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Red)
                            .padding(end = 10.dp),
                    ) {
                        /*--Right column with profile info--*/

                        Column(
                            modifier = Modifier
                                .border(width = 1.dp, color = Color.Yellow),
                        ) {


                            AsyncImage(
                                modifier = Modifier
                                    .clickable {
                                        //TESTING------------------------------
                                        Toast
                                            .makeText(
                                                this@HomeActivity,
                                                "current user image URL = ${user.user_image_uri}",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                        //TESTING-----------------------------
                                    }
                                    .border(
                                        color = MaterialTheme.colors.primary,
                                        width = 2.dp,
                                        shape = CircleShape
                                    )
                                    .size(width = 150.dp, height = 150.dp)
                                    .clip(shape = CircleShape),
                                model = userImage,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                            )


                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.primary,
                                text = "Level ${user.user_level}"
                            )

                        }

                    }

                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.Yellow),
            ) {
                /*---------------------------SECOND LAYER------------------------------------------*/

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = Color.Red),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Red),
                    ) {
                        /*--Left column with 'revision' and button--*/

                        Column(
                            modifier = Modifier
                                .border(width = 1.dp, color = Color.Yellow),
                        ) {

                            Text(
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                                text = "Revision",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondary,
                            )

                            Button(
                                modifier = Modifier
                                    .width(150.dp),
                                onClick = { },
                                shape = RoundedCornerShape(100),
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .border(width = 1.dp, color = Color.Red)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .border(width = 1.dp, color = Color.Red),
                                        ) {
                                            Text("To revise")
                                        }

                                        Row(
                                            modifier = Modifier
                                                .border(width = 1.dp, color = Color.Red),
                                        ) {
                                            Text("50")
                                        }
                                    }

                                },
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 36.dp) //Why have I had to do this and it doesn't align bottom when I tell it to?
                            .border(width = 1.dp, color = Color.Red),
                    ) {
                        /*--Right column with 'exam' button--*/

                        Button(
                            modifier = Modifier
                                .width(150.dp),
                            onClick = { },
                            shape = RoundedCornerShape(100),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                            content = {
                                Row(
                                    modifier = Modifier
                                        .border(width = 1.dp, color = Color.Red)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .border(width = 1.dp, color = Color.Red),
                                    ) {
                                        Text("Exam")
                                    }

                                }

                            },
                        )

                    }

                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Yellow),
            ) {
                /*---------------------------THIRD LAYER------------------------------------------*/

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = Color.Red),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Red),
                    ) {
                        /*--Left column with 'streak' icon and text--*/

                        Row(
                            modifier = Modifier
                                .border(width = 1.dp, color = Color.Red),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {

                            Text(
                                text = String(Character.toChars(0x1F525)),
                            )

                            Text(
                                text = " X-day streak!",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondary
                            )

                        }

                    }

                    Row(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Red),
                    ) {
                        /*--Right column with 'term base' icon and text--*/

                        Row(
                            modifier = Modifier
                                .border(width = 1.dp, color = Color.Red),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {

                            Text(
                                text = String(Character.toChars(0x1F4DA)),
                            )

                            Text(
                                text = " Term base",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondary
                            )

                        }

                    }

                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            /*---------------------------FOURTH LAYER------------------------------------------*/

            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                elevation = 5.dp,
            ) {
                /*--Rounded corner card housing API info--*/

                Row(
                    modifier = Modifier
                        .padding(all = 5.dp)
                        .border(width = 1.dp, color = Color.Red),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {

                    Row(
                        modifier = Modifier
                            .padding(all = 5.dp)
                            .border(width = 1.dp, color = Color.Red),
                    ) {

                        Row(
                            modifier = Modifier
                                .border(width = 1.dp, color = Color.Red),
                        )
                        {

                            /*--Left column of card with 'today in' text and image--*/

                            Column(
                                modifier = Modifier
                                    .border(width = 1.dp, color = Color.Red),
                            ) {


                                Text(
                                    text = "Today in /COUNTRY/",
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.secondary
                                )

                                Image(
                                    modifier = Modifier
                                        //.size(width = 200.dp, height = 200.dp)
                                        .padding(all = 5.dp)
                                        .align(Alignment.CenterHorizontally)
                                        .clip(RoundedCornerShape(2.dp)),
                                    painter = painterResource(R.drawable.ic_launcher_background),
                                    contentDescription = null,
                                )


                            }

                        }
                        /*--Right column of card with info/article text--*/
                    }
                    Row(
                        modifier = Modifier
                            .padding(all = 5.dp)
                            .border(width = 1.dp, color = Color.Red),
                    ) {

                        Text(
                            text = "Lorem ipsum dolor sit amet...",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }


            }


            /*--help text--*/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.Red),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier
                        .clickable { onClickHelp },
                    text = "Struggling? Tap here for help",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.secondary,
                    textAlign = TextAlign.Center,
                )
            }

        }

    }

//---------------------------------- OLD -------------------------------------------//

    fun oldOnCreate() {
        findViewById<TextView>(R.id.home_term_base_text).setOnClickListener {
            val intent = Intent(this, VocabSearchActivity::class.java)
            startActivity(intent)
        }

        //findViewById<Button>(R.id.vocab_lesson_button).setOnClickListener {
        //    LessonManager.createLesson(LessonTypes.VOCAB)
        //    val intent = Intent(this, DisplayTermActivity::class.java)
        //     startActivity(intent)
        //}

        //findViewById<Button>(R.id.home_revision_button).setOnClickListener {
        //RevisionSessionManager.createSession()
        //     val intent = Intent(this, ReviewTermActivity::class.java)
        //    startActivity(intent)
        //  }

        findViewById<ImageView>(R.id.user_profile_imageview).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            val PICK_IMAGE = 1
            try {
                startActivityForResult(intent, PICK_IMAGE)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
                Toast.makeText(this, "Error launching camera", Toast.LENGTH_LONG)
            }
        }
        //Temporarily loading vocab straight away to see if async error is present
        FirebaseManager.loadVocabFromFirebase()

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Toast.makeText(this, "Image has been selected", Toast.LENGTH_LONG)
        Log.d("ImageSelector", "Image has been selected")

        val uri = data?.data
        FirebaseManager.uploadUserImageToFirebaseStorage(uri)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sign_out -> {
                signOut()
            }
            R.id.menu_csv_import -> {
                CSVManager.importVocabCSV(this.applicationContext)
            }
            R.id.menu_search -> {
                val intent = Intent(this, VocabSearchActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_test_item -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                val PICK_IMAGE = 1
                try {
                    startActivityForResult(intent, PICK_IMAGE)
                } catch (e: ActivityNotFoundException) {
                    // display error state to the user
                    Toast.makeText(this, "Error launching camera", Toast.LENGTH_LONG).show()
                }
            }
            R.id.menu_user_check -> {
                Toast.makeText(
                    this,
                    "current user is ${FirebaseManager.current_user.user_email}",
                    Toast.LENGTH_LONG
                ).show()
                Toast.makeText(
                    this,
                    "current user image URL = ${FirebaseManager.current_user.user_image_uri}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        Log.d("HomeActivity", "User signed out")
        Log.d("HomeActivity", "Current user id: ${user?.uid}")
        //Return to login activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

