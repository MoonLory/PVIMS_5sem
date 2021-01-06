package com.example.lab6_2_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.res.Configuration
import android.view.View
import java.util.*
import android.text.method.ScrollingMovementMethod
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private enum class SavedInstanceMainActivityStateEnum(val value: String) {
        infoTextView("CURRENT_TEXT_INFO_MAIN_ACTIVITY"),
        guessNumber("CURRENT_GUESS_NUMBER_MAIN_ACTIVITY"),
        isNumberGuessed("CURRENT_NUMBER_IS_GUESSED_MAIN_ACTIVITY"),
        buttonText("CURRENT_BUTTON_TEXT_MAIN_ACTIVITY"),
        countGuessing("CURRENT_COUNT_GUESSING_MAIN_ACTIVITY"),
        historyTextViewInfo("CURRENT_HISTORY_TEXT_VIEW_INFO_MAIN_ACTIVITY")
    }

    private var guessNumber = 0
    private var isNumberGuessed = false
    private var countGuessing = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        guessNumber = randomInt(1, 200)
        if (savedInstanceState != null) {
            restoreActivity(savedInstanceState)
        }

        button.setOnClickListener {
            if (!isNumberGuessed) {
                val str = editTextNumber.text.toString()
                if(str != "") {
                    val userInput = str.toInt()
                    editTextNumber.setText("")
                    countGuessing++
                    when {
                        userInput < 1 -> infoTextView.text =
                                resources.getString(R.string.error_less_than_1)
                        userInput > 200 -> infoTextView.text =
                                resources.getString(R.string.error_more_than_200)
                        userInput == guessNumber -> {
                            infoTextView.text = resources.getString(R.string.win)
                            isNumberGuessed = true
                            button.text = resources.getString(R.string.button_win_text)
                        }
                        userInput > guessNumber -> infoTextView.text =
                                resources.getString(R.string.error_more)
                        userInput < guessNumber -> infoTextView.text =
                                resources.getString(R.string.error_less)
                    }
                    historyTextViewInfo.text =
                            "${historyTextViewInfo.text}$countGuessing: $userInput \n"
                }

            } else {
                guessNumber = randomInt(1, 200)
                infoTextView.text = resources.getString(R.string.info_text_view_init)
                button.text = resources.getString(R.string.button_play_text)
                historyTextViewInfo.text = ""
                isNumberGuessed = false
                countGuessing = 0
            }
        }
        historyTextViewInfo.movementMethod = ScrollingMovementMethod()
    }

    private fun randomInt(start: Int, end: Int): Int {
        return Random().nextInt(end) + start
    }

    private fun restoreActivity(savedInstanceState: Bundle){
        guessNumber = savedInstanceState.getInt(SavedInstanceMainActivityStateEnum.guessNumber.value)
        infoTextView.text = savedInstanceState.getString(SavedInstanceMainActivityStateEnum.infoTextView.value)
        isNumberGuessed = savedInstanceState.getBoolean(SavedInstanceMainActivityStateEnum.isNumberGuessed.value)
        button.text = savedInstanceState.getString(SavedInstanceMainActivityStateEnum.buttonText.value)
        countGuessing = savedInstanceState.getInt(SavedInstanceMainActivityStateEnum.countGuessing.value)
        historyTextViewInfo.text = savedInstanceState.getString(SavedInstanceMainActivityStateEnum.historyTextViewInfo.value)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SavedInstanceMainActivityStateEnum.guessNumber.value, guessNumber)
        outState.putInt(SavedInstanceMainActivityStateEnum.countGuessing.value, countGuessing)
        outState.putBoolean(SavedInstanceMainActivityStateEnum.isNumberGuessed.value, isNumberGuessed)
        outState.putString(SavedInstanceMainActivityStateEnum.buttonText.value, button.text.toString())
        outState.putString(SavedInstanceMainActivityStateEnum.historyTextViewInfo.value, historyTextViewInfo.text.toString())
        outState.putString(SavedInstanceMainActivityStateEnum.infoTextView.value, infoTextView.text.toString())

        super.onSaveInstanceState(outState)
    }
}