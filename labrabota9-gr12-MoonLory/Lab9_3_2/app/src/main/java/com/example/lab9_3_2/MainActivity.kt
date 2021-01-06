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

        dbHandler = DBHelper(this, null, null, 1)
    }

    private fun addRecord(){
        val fio = etConsFIO.text.toString()
        val phone = Integer.parseInt(etConsNum.text.toString())
        val card = Integer.parseInt(etConsCard.text.toString())
        val date = etDate.text.toString()
        val addr = etAddr.text.toString()
        val mail = etmail.text.toString()
        val aSum = Integer.parseInt(aSum.text.toString())

        dbHandler.addNewItemToTable(Cons(1, fio, phone, card,date,addr,mail,aSum))
    }

    @SuppressLint("SetTextI18n")
    private fun showRecords(){
        startActivity(Intent(this, DataActivity::class.java))
    }

    private fun changeRecord(){
        val list = dbHandler.getTable()

        val fio = etConsFIO.text.toString()
        val phone = Integer.parseInt(etConsNum.text.toString())
        val card = Integer.parseInt(etConsCard.text.toString())
        val date = etDate.text.toString()
        val addr = etAddr.text.toString()
        val mail = etmail.text.toString()
        val aSum = Integer.parseInt(aSum.text.toString())

        for (i in list.indices) {
            if(list[i].fio == fio) {
                list[i].phone = phone
                list[i].card = card
                list[i].date = date
                list[i].addr = addr
                list[i].mail = mail
                list[i].aSum = aSum

                dbHandler.updateItem(list[i])
                break
            }
        }
    }
}