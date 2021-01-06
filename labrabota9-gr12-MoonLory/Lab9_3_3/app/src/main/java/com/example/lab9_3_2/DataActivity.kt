package com.example.lab9_3_2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.data_activity.*

class DataActivity : AppCompatActivity() {
    private lateinit var dbHandler: DBHelper

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_activity)

        dbHandler = DBHelper(this, null, null, 2)

        val list = dbHandler.getTable()

        dtView.text = ""
        for (i in list)
        {
            dtView.text = dtView.text.toString() + "\t${i.id}, Mark:${i.mark}, Number:${i.number}" +
                    "\t  Color:${i.color}, Year:${i.color}, Cost:${i.cost}\n"
        }
    }
}