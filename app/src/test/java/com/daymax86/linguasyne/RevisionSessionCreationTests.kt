package com.daymax86.linguasyne

import android.os.Build
import androidx.annotation.RequiresApi
import com.daymax86.linguasyne.classes.User
import com.daymax86.linguasyne.enums.ReviewTimes
import com.daymax86.linguasyne.managers.FirebaseManager
import com.daymax86.linguasyne.managers.RevisionSessionManager
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class RevisionSessionCreationTests {

    class daymax86UnitTest {
        @Test
        @RequiresApi(Build.VERSION_CODES.O)
        fun reviewTimeConversionTest() {
            assertEquals(LocalDate.now(), RevisionSessionManager.convertNextReviewHoursToTimestamp(ReviewTimes.NOW))
        }
    }
}




