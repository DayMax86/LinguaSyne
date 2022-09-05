package com.example.linguasyne.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import com.example.linguasyne.managers.*
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import com.example.linguasyne.viewmodels.HomeViewModel
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.linguasyne.R
import com.example.linguasyne.classes.NewsItem
import com.example.linguasyne.classes.User
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    val viewModel = HomeViewModel()

    override fun onStart() {
        super.onStart()
        viewModel.init()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            LaunchTermBase(launchTermBase = viewModel.launchTermBase)
            LaunchVocabLesson(
                launchVocabLesson = viewModel.launchVocabLesson,
                viewModel::createLesson
            )
            LaunchRevisionSession(
                launchRevisionSession = viewModel.launchRevisionSession,
                viewModel::createSession
            )
            SignOut(launchLogin = viewModel.launchLogin, viewModel::signOut)

            val scope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))


            LinguaSyneTheme(darkTheme = false) {

                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerContent = {
                        MainDrawerContent(
                            viewModel::handleTermBaseClick,
                            viewModel::signOut,
                        )
                    },
                    topBar = {
                        HomeTopAppBar(
                            scope = scope,
                            scaffoldState = scaffoldState,
                        )
                    },
                    content = { padding ->
                        Surface(
                            modifier = Modifier
                                .padding(padding)
                                .background(MaterialTheme.colors.background)
                                .fillMaxHeight()
                        ) {
                            DisplayHome(
                                viewModel.user,
                                viewModel::handleHelpClick,
                                viewModel.userImage,
                                viewModel::handleVocabLessonClick,
                                viewModel::handleRevisionClick,
                                viewModel::handleTermBaseClick,
                                viewModel::handleProfileImageClick,
                                viewModel.newsItems,
                            )

                        }
                    },
                )
            }
        }

    }


    @Composable
    fun HomeTopAppBar(
        scope: CoroutineScope,
        scaffoldState: ScaffoldState,
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colors.onSurface
                )
            },
            backgroundColor = MaterialTheme.colors.secondary,
            navigationIcon = {
                Icon(
                    Icons.Default.Menu,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable(
                            onClick = {
                                scope.launch {
                                    if (scaffoldState.drawerState.isClosed) {
                                        scaffoldState.drawerState.open()
                                    } else {
                                        scaffoldState.drawerState.close()

                                    }
                                }
                                Log.d(
                                    "HomeActivity",
                                    "Is closed = ${scaffoldState.drawerState.isClosed}"
                                )
                            }
                        ),
                    contentDescription = ""
                )
            }
        )

    }

    @Composable
    fun MainDrawerContent(
        launchTermBase: () -> Unit,
        signOut: () -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                ) {

                    Row(
                        modifier = Modifier
                            .width(intrinsicSize = IntrinsicSize.Min)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = stringResource(id = R.string.settings),
                            style = MaterialTheme.typography.h1,
                            color = MaterialTheme.colors.primary,
                            maxLines = 1,
                        )
                        Icon(
                            Icons.Default.Settings,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(40.dp, 40.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colors.secondary
                        )
                    }

                }

                Divider(
                    modifier = Modifier
                        .height(3.dp),
                    color = MaterialTheme.colors.onBackground,
                )
            }

            Column(
                modifier = Modifier
                    .width(intrinsicSize = IntrinsicSize.Max),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            launchTermBase()
                        },
                    horizontalArrangement = Arrangement.Start,
                ) {

                    Row(
                        modifier = Modifier
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = stringResource(id = R.string.term_base),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary,
                            maxLines = 1,
                        )
                        Icon(
                            Icons.Default.Search,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 6.dp)
                                .size(20.dp, 20.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colors.secondary
                        )
                    }

                }

                Divider(
                    modifier = Modifier
                        .height(1.dp),
                    color = MaterialTheme.colors.onBackground,
                )
            }


            Column(
                modifier = Modifier
                    .width(intrinsicSize = IntrinsicSize.Max),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                ) {

                    Row(
                        modifier = Modifier
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = stringResource(id = R.string.help),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary,
                            maxLines = 1,
                        )
                        Icon(
                            Icons.Default.Info,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 6.dp)
                                .size(20.dp, 20.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colors.secondary
                        )
                    }

                }

                Divider(
                    modifier = Modifier
                        .height(1.dp),
                    color = MaterialTheme.colors.onBackground,
                )
            }

            Column(
                modifier = Modifier
                    .width(intrinsicSize = IntrinsicSize.Max),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    /*modifier = Modifier.clickable { //TODO() FOR DEVELOPER USE ONLY!! Will be removed in release version.
                CSVManager.importVocabCSV(this@HomeActivity.applicationContext)
            },*/
                    horizontalArrangement = Arrangement.Start,
                ) {

                    Row(
                        modifier = Modifier
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = stringResource(id = R.string.about),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary,
                            maxLines = 1,
                        )
                        Icon(
                            Icons.Default.Info,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 6.dp)
                                .size(20.dp, 20.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colors.secondary
                        )
                    }

                }

                Divider(
                    modifier = Modifier
                        .height(1.dp),
                    color = MaterialTheme.colors.onBackground,
                )
            }

            Column(
                modifier = Modifier
                    .width(intrinsicSize = IntrinsicSize.Max),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                ) {

                    Row(
                        modifier = Modifier
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = stringResource(id = R.string.share),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary,
                            maxLines = 1,
                        )
                        Icon(
                            Icons.Default.Share,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 6.dp)
                                .size(20.dp, 20.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colors.secondary
                        )
                    }

                }

                Divider(
                    modifier = Modifier
                        .height(1.dp),
                    color = MaterialTheme.colors.onBackground,
                )
            }

            Column(
                modifier = Modifier
                    .width(intrinsicSize = IntrinsicSize.Max),
                horizontalAlignment = Alignment.Start,
            ) {

                Row(
                    modifier = Modifier
                        .clickable {
                            signOut()
                        },
                    horizontalArrangement = Arrangement.Start,
                ) {

                    Row(
                        modifier = Modifier
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = stringResource(id = R.string.sign_out),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary,
                            maxLines = 1,
                        )
                        Icon(
                            Icons.Default.AccountCircle,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 6.dp)
                                .size(20.dp, 20.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colors.secondary
                        )
                    }

                }
                Divider(
                    modifier = Modifier
                        .height(1.dp),
                    color = MaterialTheme.colors.onBackground,
                )
            }

        }
    }


    @OptIn(ExperimentalSnapperApi::class)
    @Composable
    fun DisplayHome(
        user: User,
        onClickHelp: () -> Unit,
        userImage: Uri?,
        onClickVocabLesson: () -> Unit,
        onClickRevision: () -> Unit,
        onClickTermBase: () -> Unit,
        onClickProfileImage: () -> Unit,
        newsItems: List<NewsItem.Data>,
    ) {
        Column(

        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                /*---------------------------FIRST LAYER------------------------------------------*/
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Row(
                        modifier = Modifier
                            .padding(start = 10.dp),
                    ) {
                        /*--Left column with 'lessons' and 3 buttons--*/

                        Column(

                        ) {

                            Text(
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                                text = stringResource(id = R.string.lessons),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondary,
                            )

                            Button(
                                modifier = Modifier
                                    .width(150.dp),
                                onClick = { onClickVocabLesson() },
                                shape = RoundedCornerShape(100),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.secondary,
                                    contentColor = MaterialTheme.colors.onSurface,
                                ),
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Row(

                                        ) {
                                            Text(stringResource(id = R.string.vocab))
                                        }

                                        Row(

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
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.secondary,
                                    contentColor = MaterialTheme.colors.onSurface,
                                ),
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Row(

                                        ) {
                                            Text(stringResource(id = R.string.verbs))
                                        }

                                        Row(

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
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.secondary,
                                    contentColor = MaterialTheme.colors.onSurface,
                                ),
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Row(

                                        ) {
                                            Text(stringResource(id = R.string.phrases))
                                        }

                                        Row(

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
                            .padding(end = 10.dp),
                    ) {
                        /*--Right column with profile info--*/

                        Column(

                        ) {


                            AsyncImage(
                                modifier = Modifier
                                    .padding(top = 30.dp)
                                    .clickable {
                                        onClickProfileImage()
                                        selectImage()
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

                            FirebaseManager.currentUser?.let {
                                Text(
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .align(Alignment.CenterHorizontally),
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.primary,
                                    text = it.email
                                )
                            }

                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.primary,
                                text = stringResource(id = R.string.level) + "${user.level}"
                            )

                        }

                    }

                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                /*---------------------------SECOND LAYER------------------------------------------*/

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(

                    ) {
                        /*--Left column with 'revision' and button--*/

                        Column(

                        ) {

                            Text(
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                                text = stringResource(id = R.string.revision),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondary,
                            )

                            Button(
                                modifier = Modifier
                                    .width(150.dp),
                                onClick = { onClickRevision() },
                                shape = RoundedCornerShape(100),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.secondary,
                                    contentColor = MaterialTheme.colors.onSurface,
                                ),
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Row(

                                        ) {
                                            Text(stringResource(id = R.string.to_revise))
                                        }

                                        Row(

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
                    ) {
                        /*--Right column with 'exam' button--*/

                        Button(
                            modifier = Modifier
                                .width(150.dp),
                            onClick = { },
                            shape = RoundedCornerShape(100),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.secondary,
                                contentColor = MaterialTheme.colors.onSurface,
                            ),
                            content = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Row(

                                    ) {
                                        Text(stringResource(id = R.string.exam))
                                    }

                                }

                            },
                        )

                    }

                }
            }


            Column(

            ) {
                /*---------------------------THIRD LAYER------------------------------------------*/

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(

                    ) {
                        /*--Left column with 'streak' icon and text--*/

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {

                            Text(
                                //Fire emoji
                                text = String(Character.toChars(0x1F525)),
                            )

                            Text(
                                modifier = Modifier
                                    .padding(start = 2.dp),
                                text = stringResource(id = R.string.day_streak, user.streak),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondary
                            )

                        }

                    }

                    Row(

                    ) {
                        /*--Right column with 'term base' icon and text--*/

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {

                            Text(
                                //Books emoji
                                text = String(Character.toChars(0x1F4DA)),
                            )

                            Text(
                                modifier = Modifier
                                    .padding(start = 2.dp)
                                    .clickable { onClickTermBase() },
                                text = stringResource(id = R.string.term_base),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondary,
                            )

                        }

                    }

                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            /*---------------------------FOURTH LAYER------------------------------------------*/

            Column(

            ) {

                val lazyListState = rememberLazyListState()
                LazyRow(
                    modifier = Modifier
                        //.padding(10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    state = lazyListState,
                    flingBehavior = rememberSnapperFlingBehavior(lazyListState),
                )
                {
                    items(
                        newsItems
                    ) { item ->
                        Card(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .fillParentMaxWidth(1f)
                                .wrapContentHeight()
                                .padding(all = 16.dp),
                            elevation = 6.dp,
                            backgroundColor = MaterialTheme.colors.onSurface,
                        ) {
                            /*--Rounded corner card housing API info--*/

                            Column(
                                modifier = Modifier
                                    .border(width = 2.dp, color = MaterialTheme.colors.secondary)
                                    .padding(all = 5.dp),
                            ) {

                                Column(
                                ) {

                                    Row(

                                    )
                                    {


                                        Column(

                                        ) {


                                            Text(
                                                modifier = Modifier
                                                    .padding(start = 5.dp, end = 5.dp),
                                                text = stringResource(id = R.string.today_in, user.studyCountry),
                                                style = MaterialTheme.typography.body1,
                                                color = MaterialTheme.colors.secondary
                                            )


                                        }
                                    }

                                    AsyncImage(
                                        modifier = Modifier
                                            .fillParentMaxWidth()
                                            .padding(all = 5.dp)
                                            .border(
                                                2.dp,
                                                color = MaterialTheme.colors.secondary,
                                                shape = RoundedCornerShape(5.dp)
                                            )
                                            .clip(RoundedCornerShape(5.dp)),
                                        model = item.image,
                                        contentDescription = null,
                                    )



                                    Row(
                                        modifier = Modifier
                                            .padding(all = 5.dp),
                                    ) {

                                        Text(
                                            modifier = Modifier
                                                .clipToBounds(),
                                            text = item.title,
                                            style = MaterialTheme.typography.body1,
                                            color = MaterialTheme.colors.secondary,
                                            overflow = TextOverflow.Visible,
                                            softWrap = true,
                                        )
                                    }

                                }

                            }


                        }
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom,
                    ) {

                        DotsIndicator(
                            totalDots = viewModel.newsItems.size,
                            selectedIndex = lazyListState.firstVisibleItemIndex,
                            selectedColor = viewModel.selectedNewsColour,
                            unSelectedColor = viewModel.unselectedNewsColour,
                        )

                    }
                }


            }
        }

    }


    @Composable
    fun DotsIndicator(
        totalDots: Int,
        selectedIndex: Int,
        selectedColor: Color,
        unSelectedColor: Color,
    ) {

        LazyRow(

        ) {

            items(totalDots) { index ->
                if (index == selectedIndex) {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(selectedColor)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(unSelectedColor)
                    )
                }

                if (index != totalDots - 1) {
                    Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                }
            }
        }
    }


/*-----------------------------------------LAUNCHERS----------------------------------------------*/

    @Composable
    fun LaunchTermBase(launchTermBase: Boolean) {
        if (launchTermBase) {
            val intent = Intent(this, VocabSearchActivity::class.java)
            startActivity(intent)
        }
    }

    @Composable
    fun LaunchVocabLesson(launchVocabLesson: Boolean, createLesson: () -> Unit) {
        if (launchVocabLesson) {
            createLesson()
            val intent = Intent(this, DisplayTermActivity::class.java)
            startActivity(intent)
        }
    }

    @Composable
    fun LaunchRevisionSession(launchRevisionSession: Boolean, createSession: () -> Unit) {
        if (launchRevisionSession) {
            createSession()
            val intent = Intent(this, ReviseTermActivity::class.java)
            startActivity(intent)
        }
    }

    @Composable
    fun SignOut(launchLogin: Boolean, signOut: () -> Unit) {
        if (launchLogin) {
            this.finish()
            signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
    }


    private fun selectImage() {
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val uri = data?.data
        viewModel.uploadUserImage(uri)
    }
}

//---------------------------------- OLD -------------------------------------------//
/*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sign_out -> {
                //signOut()
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

    } */