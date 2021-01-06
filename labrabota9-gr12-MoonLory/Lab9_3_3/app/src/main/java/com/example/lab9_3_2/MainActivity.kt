package com.example.lab9_3_2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {
    private lateinit var dbHandler: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addBtn.setOnClickListener { _ -> addRecord() }
        shwBtn.setOnClickListener { _ -> showRecords() }
        chgBtn.setOnClickListener { _ -> changeRecord() }

        dbHandler = DBHelper(this, null, null, 2)
    }

    private fun addRecord(){
        val mark = etCatMark.text.toString()
        val number = etCarNum.text.toString()
        val color = etCarCol.text.toString()
        val year = etCarYear.text.toString().toInt()
        val cost = etCarCost.text.toString().toInt()

        dbHandler.addNewItemToTable(Car(1, mark, number, color, year, cost))
    }

    @SuppressLint("SetTextI18n")
    private fun showRecords(){
        startActivity(Intent(this, DataActivity::class.java))
    }

    private fun changeRecord(){
        val list = dbHandler.getTable()

        val mark = etCatMark.text.toString()
        val number = etCarNum.text.toString()
        val color = etCarCol.text.toString()
        val year = etCarYear.text.toString().toInt()
        val cost = etCarCost.text.toString().toInt()


        for (i in list.indices) {
            if(list[i].number == number) {
                list[i].mark = mark
                list[i].color = color
                list[i].year = year
                list[i].cost = cost

                dbHandler.updateItem(list[i])
                break
            }
        }
    }
}