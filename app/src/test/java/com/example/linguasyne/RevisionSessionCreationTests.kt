package com.example.linguasyne

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionSessionCreationTests {

    class ExampleUnitTest {
        @Test
        fun createRevisionSessionTest() {
            FirebaseManager.current_user = User("test@test.com")
            //When user level is 0 and first 3/21 terms in vocab have unlock level changed to > 0
            RevisionSessionManager.createSession()
            assertEquals(18,RevisionSessionManager.current_session.session_list.size)
        }
    }
}




