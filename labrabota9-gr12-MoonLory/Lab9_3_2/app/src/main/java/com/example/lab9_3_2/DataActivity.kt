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

        dbHandler = DBHelper(this, null, null, 1)

        val list = dbHandler.getTable()

        dtView.text = ""
        for (i in list)
        {
            dtView.text = dtView.text.toString() + "${i.id}: ${i.fio} - ${i.phone} - ${i.card}\n - ${i.date}- ${i.addr}- ${i.mail}- ${i.aSum} \n"
        }
    }
}