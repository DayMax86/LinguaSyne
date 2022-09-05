package com.example.linguasyne

import com.example.linguasyne.managers.LessonManager
import org.junit.Assert.assertEquals
import org.junit.Test
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
class CreateLessonTest {
    @Test
    fun createLesson_isCorrect() {
        //assertThat("Current vocab should have size 5", VocabRepository.currentVocab.size,5)
        assertEquals(5, LessonManager.currentLesson.lessonList.size)
    }
}

/*@RunWith(JUnit4::class)
class SortLessonTest {
    @Test
    fun sortLesson_isCorrect() {
        val lt: LessonTypes = LessonTypes.VOCAB
        val u = User("test@test.com")
        FirebaseManager.current_user = u
        LessonManager.createLesson(lt)
        LessonManager.sortById()
        }
    }
}*/
