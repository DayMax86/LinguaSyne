package com.example.linguasyne

import com.example.linguasyne.classes.User
import com.example.linguasyne.managers.FirebaseManager
import com.example.linguasyne.managers.RevisionSessionManager
import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionSessionCreationTests {

    class ExampleUnitTest {
        @Test
        fun createRevisionSessionTest() {
            FirebaseManager.current_user = User("test@test.com")
            //When user level is 0 and first 3/21 terms in vocab have unlock level changed to > 0
            RevisionSessionManager.createSession()
            assertEquals(18, RevisionSessionManager.current_session.sl.size)
        }
    }
}




