package com.example.lab9_part1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val PREFS_GAME = "com.example.lab9_3_1.GamePlay"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener{
            val sharedPref = getSharedPreferences(PREFS_GAME, MODE_PRIVATE)
            val editor = sharedPref.edit()

            editor.putString("NAME", txtName.text.toString())
            editor.apply()

            val intent = Intent(this, PlayActivity::class.java)
            startActivity(intent)
        }
    }
}