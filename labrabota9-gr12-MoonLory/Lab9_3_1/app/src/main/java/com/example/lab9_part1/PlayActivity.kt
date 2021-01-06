package com.example.lab9_part1

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.play_activity.*
import java.util.*

class PlayActivity : AppCompatActivity() {
    val PREFS_GAME = "com.example.lab9_3_1.GamePlay"


    private var guessNumber = 0
    private var isNumberGuessed = false
    private var countGuessing = 0
    private val totalAttemps = 10

    private var username = ""

    private lateinit var gestureLibrary: GestureLibrary

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.play_activity)

        updateSavedPref()

        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures)
        if (!gestureLibrary.load()) {
            finish()
        }
        gestureOverlayView.addOnGesturePerformedListener { _, gesture ->
            val numbers = gestureLibrary.recognize(gesture)
            if (numbers.size > 0) {
                val number = numbers[0]
                if (number.score > 1.0) {
                    when (number.name) {
                        "zero" -> numberEditText.append("0")
                        "one" -> numberEditText.append("1")
                        "two" -> numberEditText.append("2")
                        "three" -> numberEditText.append("3")
                        "four" -> numberEditText.append("4")
                        "five" -> numberEditText.append("5")
                        "six" -> numberEditText.append("6")
                        "seven" -> numberEditText.append("7")
                        "eight" -> numberEditText.append("8")
                        "nine" -> numberEditText.append("9")
                        "enter" -> numberGuessing()
                        "remove" -> numberEditText.text = numberEditText.text.dropLast(1) as Editable?
                    }
                }
            }
        }

        infoTextView.text = username + ", " + getString(R.string.info_text_view_init)
        relativeLayout.background = getDrawable(R.drawable.wallpaper)

        guessNumber = randomInt(1, 100)

        tryButton.setOnClickListener {
            startAnimations()
            when {
                !isNumberGuessed -> numberGuessing()
                else -> reloadAll()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun numberGuessing() {
        val userInput = numberEditText.text.toString().toIntOrNull()
        if (userInput == null) {
            infoTextView.text = resources.getString(R.string.no_input)
            return
        }
        numberEditText.setText("")

        countGuessing++
        if (countGuessing >= totalAttemps) {
            showDialog(getString(R.string.lose))
            return
        }

        progressBar.progress = totalAttemps - countGuessing
        when {
            userInput < 1 -> infoTextView.text = resources.getString(R.string.error_less_than_1)
            userInput > 100 -> infoTextView.text = resources.getString(R.string.error_more_than_20)
            userInput == guessNumber -> winAction()
            userInput > guessNumber -> infoTextView.text = resources.getString(R.string.error_more)
            userInput < guessNumber -> infoTextView.text = resources.getString(R.string.error_less)
        }
        resView.text = "${resView.text}\n$countGuessing - $userInput ${infoTextView.text}"

    }

    private fun winAction() {
        showDialog(resources.getString(R.string.win))
        infoTextView.text = resources.getString(R.string.win)
        isNumberGuessed = true

        saveResult()

        resView.text = ""

        updateSavedPref()
    }

    private fun reloadAll() {
        guessNumber = randomInt(1, 20)
        infoTextView.text = resources.getString(R.string.info_text_view_init)
        tryButton.text = resources.getString(R.string.button_play_text)
        isNumberGuessed = false
        countGuessing = 0
        progressBar.progress = totalAttemps
    }

    private fun showDialog(title: String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(title)
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            reloadAll()
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
            Toast.makeText(this, getString(R.string.end_game), Toast.LENGTH_SHORT).show()
            this.finishAffinity()
        }

        builder.create().show()
    }

    private fun startAnimations() {
        tryButton.startAnimation(AlphaAnimation(0.3f, 1.0f).apply { duration = 300; fillAfter = true })

        val animSet = AnimationSet(true)
        animSet.isFillEnabled = true
        animSet.addAnimation(TranslateAnimation(0f, -20f, 0f, 0f).apply { duration = 200; startOffset = 300 })
        animSet.addAnimation(TranslateAnimation(0f, 20f, 0f, 0f).apply { duration = 200 })
        animSet.addAnimation(ScaleAnimation(1.5f, 0.5f, 1f, 1f).apply { duration = 200; startOffset = 300 })
        animSet.addAnimation(ScaleAnimation(0.5f, 1.5f, 1f, 1f).apply { duration = 200 })

        infoTextView.startAnimation(animSet)
    }

    private fun randomInt(start: Int, end: Int): Int {
        return Random().nextInt(end) + start
    }

    override fun onStop() {
        super.onStop()
        val sharedPref = this.getPreferences(MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("GUESSING_ATTEMPTS_ID", countGuessing)
        editor.apply()
    }

    fun saveResult(){
        val sharedPref = getSharedPreferences(PREFS_GAME, MODE_PRIVATE)
        val editor = sharedPref.edit()

        var attempts = sharedPref.getInt("COUNTER", 0)
        var best_res = sharedPref.getInt("BEST_RES", 0)
        var wins = sharedPref.getInt("WINS", 0)
        var fails = sharedPref.getInt("FAILS", 0)

        editor.putInt("COUNTER", ++attempts)

        if(isNumberGuessed) {
            editor.putInt("WINS", ++wins)

            if(best_res == 0 || best_res > countGuessing)
            {
                editor.putInt("BEST_RES", countGuessing)
            }
        }
        else
            editor.putInt("FAILS", ++fails)

        editor.apply()
    }

    @SuppressLint("SetTextI18n")
    fun updateSavedPref(){
        val sharedPref = getSharedPreferences(PREFS_GAME, MODE_PRIVATE)
        val editor = sharedPref.edit()

        countGuessing = sharedPref.getInt("GUESSING_ATTEMPTS_ID", 10)
        val appLaunchValue  = sharedPref.getInt("COUNTER", 0)
        val wins = sharedPref.getInt("WINS", 0)
        val fails = sharedPref.getInt("FAILS", 0)
        username = sharedPref.getString("NAME", "-")  ?: "Not Set"

        val best_res = sharedPref.getInt("BEST_RES", 0)

        if (appLaunchValue == 0) {
            editor.putInt("APP_ID", randomInt(0,123241))
        }

        val appID = sharedPref.getInt("APP_ID", 0)

        editor.apply()

        resView.text = "ID: ${appID}, Attempts: ${appLaunchValue} \nw:${wins} f:${fails}\n " +
                "Best result: ${best_res}"
    }
}