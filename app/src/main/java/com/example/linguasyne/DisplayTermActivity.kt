package com.example.linguasyne

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import com.example.linguasyne.enums.Gender
import kotlin.properties.Delegates

class DisplayTerm : AppCompatActivity() {

    private lateinit var t: Term
    private var tPos: Int = 0
    private lateinit var vSource: List<Term>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_term)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.display_term_translations_textview).movementMethod =
            ScrollingMovementMethod()
        findViewById<TextView>(R.id.display_term_mnemonics_textview).movementMethod =
            ScrollingMovementMethod()

    }

    private fun displayTerm() {

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


    private fun fetchTermSource(): List<Term> {
        vSource =
            if (LessonManager.activeLesson) { //Source of term is lesson
                LessonManager.current_lesson.lesson_list
            } else { //Source must be normal repo
                VocabRepository.allVocab
            }
        return vSource
    }

    private fun fetchTerm(): Term {
        t = fetchTermSource().elementAt(tPos)
        return t
    }

    private fun displayVocabData() {
        clearUI()

        //populate the text fields with the term's data
        findViewById<TextView>(R.id.term_name_textbox).text =
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

        //If this is the first element in the list then the previous term navigating image should be hidden/disabled
        if (tPos == 0) {
            firstInList = true
        }
        if (tPos == vSource.size) {
            lastInList = true
        }

        //Cache the ImageView references
        val leftArrowImageView = findViewById<ImageView>(R.id.left_arrow_image)
        val rightArrowImageView = findViewById<ImageView>(R.id.right_arrow_image)

        if (firstInList) {
            //Disable left arrow ImageView onClickListener
            leftArrowImageView.setOnClickListener(null)
            leftArrowImageView.setBackgroundResource(0)
        } else if (lastInList) {
            //Disable right arrow ImageView onClickListener
            rightArrowImageView.setOnClickListener(null)
            rightArrowImageView.setBackgroundResource(0)
        } else {
            //must be somewhere away from the ends of the list so both can be enabled

            leftArrowImageView.setOnClickListener { loadPrev() }
            leftArrowImageView.setBackgroundResource(R.drawable.opaqueleftarrow)

            rightArrowImageView.setOnClickListener { loadNext() }
            rightArrowImageView.setBackgroundResource(R.drawable.opaquerightarrow)
        }

        //Set the gender images
        if (t.class_type == LessonTypes.VOCAB) {
            //Go through all the genders...
        }
    }


    private fun loadPrev() {
        tPos--
        DisplayTerm()
    }

    private fun loadNext() {
        tPos++
        DisplayTerm()
    }

    private fun clearUI() {
        findViewById<TextView>(R.id.term_name_textbox).text = ""
        findViewById<TextView>(R.id.term_unlock_level_textbox).text = ""
        findViewById<TextView>(R.id.display_term_translations_textview).text = ""
        findViewById<TextView>(R.id.display_term_mnemonics_textview).text = ""
        findViewById<TextView>(R.id.term_search_current_level_textview).text = ""
        findViewById<TextView>(R.id.term_search_next_level_textview).text = ""
        findViewById<TextView>(R.id.next_review_date).text = ""
    }

    private fun hideGenderImages() {
        findViewById<ImageView>(R.id.display_term_masculine_imageview).setBackgroundResource(
            R.drawable.alphamars
        )
        findViewById<ImageView>(R.id.display_term_feminine_imageview).setBackgroundResource(
            R.drawable.alphavenus
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        LessonManager.activeLesson = false
    }
}


