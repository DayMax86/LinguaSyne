package com.example.linguasyne

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.linguasyne.classes.User
import com.example.linguasyne.enums.ReviewTimes
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.managers.RevisionSessionManager
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class RevisionSessionCreationTests {

    class ExampleUnitTest {
        @Test
        @RequiresApi(Build.VERSION_CODES.O)
        fun reviewTimeConversionTest() {
            assertEquals(LocalDate.now(), RevisionSessionManager.convertNextReviewHoursToTimestamp(ReviewTimes.NOW))
        }
    }
}




