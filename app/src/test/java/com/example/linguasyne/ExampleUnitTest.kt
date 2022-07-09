package com.example.linguasyne

import com.google.firebase.auth.FirebaseAuth
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

@RunWith(JUnit4::class)
class createLessonTest {
    @Test
    fun createLesson_isCorrect() {
        val l_t: LessonTypes = LessonTypes.VOCAB
        val u = User("test@test.com")
        FirebaseManager.current_user = u
        LessonManager.createLesson(l_t)
        //assertThat("Current vocab should have size 5", VocabRepository.currentVocab.size,5)
        assertEquals(5, VocabRepository.currentVocab.size)
    }
}