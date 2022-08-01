package com.example.linguasyne

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.linguasyne.enums.Gender
import javax.xml.transform.Source

open class DisplayTerm : AppCompatActivity() {

    private lateinit var t: Term
    private var tPos: Int = 0
    private lateinit var vSource: Sources

    private enum class Sources {
        LESSON,
        SEARCH
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_term)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.display_term_translations_textview).movementMethod =
            ScrollingMovementMethod()
        findViewById<TextView>(R.id.display_term_mnemonics_textview).movementMethod =
            ScrollingMovementMethod()

        display()
    }

    private fun display() {

        //One function to run them all! Separated from onCreate so it can be called when navigating through items.

        //Determine source of term (i.e. lesson or repo)
        fetchTermSource()

        //Get current term to be displayed
        fetchTerm()

        //Populate text fields
        displayVocabData()

        //Load and display images (depends on list position as to which get displayed)
        //Enable navigation between terms (loadNext() and loadPrev())
        displayImages()

    }


    private fun fetchTermSource(): Sources {
        vSource =
            if (LessonManager.activeLesson) {
                Sources.LESSON
            } else {
                Sources.SEARCH
            }
        return vSource
    }

    private fun fetchTerm(): Term {
        val src = fetchTermSource()
        when (src) {
            (Sources.LESSON) -> {
                t = LessonManager.current_lesson.lesson_list.elementAt(tPos)
            }
            (Sources.SEARCH) -> {
                t = VocabRepository.currentVocab.elementAt(0)
            }
        }

        return t
    }


    private fun displayVocabData() {
        clearUI()

        //populate the text fields with the term's data
        findViewById<TextView>(R.id.term_textbox).text =
            t.name

        findViewById<TextView>(R.id.term_unlock_level_textbox).text =
            t.unlock_level.toString()

        for (translations in t.translations) {
            findViewById<TextView>(R.id.display_term_translations_textview).text =
                (findViewById<TextView>(R.id.display_term_translations_textview).text.toString()
                    .plus("\''$translations', "))
        }

        for (mnemonics in t.mnemonics) {
            findViewById<TextView>(R.id.display_term_mnemonics_textview).text =
                (findViewById<TextView>(R.id.display_term_mnemonics_textview).text.toString()
                    .plus("$mnemonics. "))
        }

        findViewById<TextView>(R.id.term_search_current_level_textview).text =
            t.current_level_term.toString()

        findViewById<TextView>(R.id.term_search_next_level_textview).text =
            (t.current_level_term + 1).toString()

        findViewById<TextView>(R.id.next_review_date).text =
            t.next_review.toString()
    }

    private fun displayImages() {
        var firstInList: Boolean = false
        var lastInList: Boolean = false
        var arrowsEnabled: Boolean = true

        //If this is the first element in the list then the previous term navigating image should be hidden/disabled

        when (vSource) {
            (Sources.LESSON) -> {
                if (tPos == LessonManager.current_lesson.lesson_list.size - 1) {
                    lastInList = true
                } else if (tPos == 0) {
                    firstInList = true
                }
            }
            (Sources.SEARCH) -> {
                /* if (VocabRepository.currentVocab.indexOf(t) == 0) {
                    firstInList = true
                } else if (VocabRepository.currentVocab.indexOf(t) == VocabRepository.currentVocab.size - 1) {
                    lastInList = true
                } */
                arrowsEnabled = false
            }
            else -> {/**/
            }
        }

        //Cache the ImageView references
        val leftArrowImageView = findViewById<ImageView>(R.id.left_arrow_image)
        leftArrowImageView.visibility = View.VISIBLE
        val rightArrowImageView = findViewById<ImageView>(R.id.right_arrow_image)
        rightArrowImageView.visibility = View.VISIBLE

        if (arrowsEnabled) {
            if (firstInList) {
                //Disable left arrow ImageView onClickListener
                leftArrowImageView.setOnClickListener(null)
                leftArrowImageView.setImageResource(R.drawable.alphaleftarrow)
                rightArrowImageView.setOnClickListener { loadNext() }
                rightArrowImageView.setImageResource(R.drawable.opaquerightarrow)
            } else if (lastInList) {
                //Disable right arrow ImageView onClickListener
                rightArrowImageView.setOnClickListener(null)
                rightArrowImageView.setImageResource(R.drawable.alpharightarrow)
                leftArrowImageView.setOnClickListener { loadPrev() }
                leftArrowImageView.setImageResource(R.drawable.opaqueleftarrow)
            } else {
                //must be somewhere away from the ends of the list so both can be enabled

                leftArrowImageView.setOnClickListener { loadPrev() }
                leftArrowImageView.setImageResource(R.drawable.opaqueleftarrow)

                rightArrowImageView.setOnClickListener { loadNext() }
                rightArrowImageView.setImageResource(R.drawable.opaquerightarrow)
            }
        } else {
            leftArrowImageView.visibility = View.INVISIBLE
            rightArrowImageView.visibility = View.INVISIBLE
        }

        //Set the gender images
        setGenderImages()
    }

    private fun setGenderImages() {
        if (t is Vocab) {
            //Go through all the genders
            for (g: Gender in (t as Vocab).genders) {
                when (g) {
                    Gender.NO -> {
                        //No gender so leave alpha versions of images. Explicitly set to avoid empty case, but default should be alpha anyway.
                        findViewById<ImageView>(R.id.display_term_masculine_imageview).setImageResource(
                            R.drawable.alphamars
                        )
                        findViewById<ImageView>(R.id.display_term_feminine_imageview).setImageResource(
                            R.drawable.alphavenus
                        )
                    }
                    Gender.F -> {
                        findViewById<ImageView>(R.id.display_term_feminine_imageview).setImageResource(
                            R.drawable.opaquevenus
                        )
                    }
                    Gender.M -> {
                        findViewById<ImageView>(R.id.display_term_masculine_imageview).setImageResource(
                            R.drawable.opaquemars
                        )
                    }
                    Gender.MF -> {
                        findViewById<ImageView>(R.id.display_term_masculine_imageview).setImageResource(
                            R.drawable.opaquemars
                        )
                        findViewById<ImageView>(R.id.display_term_feminine_imageview).setImageResource(
                            R.drawable.opaquevenus
                        )
                    }
                }
            }
        }
    }


    private fun loadPrev() {
        tPos--
        display()
    }

    private fun loadNext() {
        tPos++
        display()
    }

    private fun clearUI() {
        findViewById<TextView>(R.id.term_textbox).text = ""
        findViewById<TextView>(R.id.term_unlock_level_textbox).text = ""
        findViewById<TextView>(R.id.display_term_translations_textview).text = ""
        findViewById<TextView>(R.id.display_term_mnemonics_textview).text = ""
        findViewById<TextView>(R.id.term_search_current_level_textview).text = ""
        findViewById<TextView>(R.id.term_search_next_level_textview).text = ""
        findViewById<TextView>(R.id.next_review_date).text = ""
        //Set gender images to alpha versions as default
        findViewById<ImageView>(R.id.display_term_masculine_imageview).setImageResource(R.drawable.alphamars)
        findViewById<ImageView>(R.id.display_term_feminine_imageview).setImageResource(R.drawable.alphavenus)
        //Left and right arrows should also be set to alpha version as default
        findViewById<ImageView>(R.id.left_arrow_image).setImageResource(R.drawable.alphaleftarrow)
        findViewById<ImageView>(R.id.right_arrow_image).setImageResource(R.drawable.alpharightarrow)
    }

    override fun onDestroy() {
        super.onDestroy()
        LessonManager.activeLesson = false
    }
}


