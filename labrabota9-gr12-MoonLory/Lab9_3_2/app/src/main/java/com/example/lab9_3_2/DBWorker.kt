package com.example.lab9_3_2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.Editable
import android.util.Log

import java.io.Serializable

class Cons(val id: Int, var fio: String, var phone: Int, var card: Int, var date: String, var addr: String, var mail: String, var aSum: Int ): Serializable

class DBHelper(context: Context, name: String?,
               factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = ("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIO + " TEXT NOT NULL, "
                + COLUMN_PHONE + " INTEGER NOT NULL, "
                + COLUMN_CARD + " INTEGER NOT NULL, "
                + COLUMN_DATE + " TEXT NOT NULL,  "
                + COLUMN_ADDR + " TEXT NOT NULL,  "
                + COLUMN_MAIL + " TEXT NOT NULL,  "
                + COLUMN_ASUM + " INTEGER NOT NULL);")

        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {

    }

    fun addNewItemToTable(table: Cons) {
        val values = ContentValues()
        values.put(COLUMN_FIO, table.fio)
        values.put(COLUMN_PHONE, table.phone)
        values.put(COLUMN_CARD, table.card)
        values.put(COLUMN_DATE, table.date)
        values.put(COLUMN_ADDR, table.addr)
        values.put(COLUMN_MAIL, table.mail)
        values.put(COLUMN_ASUM, table.aSum)

        writableDatabase.insert(TABLE_NAME, null, values)
        writableDatabase.close()
    }

    fun getTable(): ArrayList<Cons> {
        val querySQL =
            "SELECT * FROM $TABLE_NAME"

        val cursor = writableDatabase.rawQuery(querySQL, null)

        var table = ArrayList<Cons>()

        if (cursor.moveToFirst()) {
            do {
                val id = Integer.parseInt(cursor.getString(0))
                val fio = cursor.getString(1)
                val phone = Integer.parseInt(cursor.getString(2))
                val card = Integer.parseInt(cursor.getString(3))
                val date = cursor.getString(4)
                val addr = cursor.getString(5)
                val mail = cursor.getString(6)
                val aSum = Integer.parseInt(cursor.getString(7))
                table.add(Cons(id, fio, phone, card,date,addr,mail,aSum))
            } while (cursor.moveToNext())

        }
        cursor.close()
        writableDatabase.close()
        return table
    }

    fun updateItem(tableItem: Cons) {
        var values = ContentValues()
        values.put(COLUMN_ID, tableItem.id)
        values.put(COLUMN_FIO, tableItem.fio)
        values.put(COLUMN_PHONE, tableItem.phone)
        values.put(COLUMN_CARD, tableItem.card)
        values.put(COLUMN_DATE, tableItem.date)
        values.put(COLUMN_ADDR, tableItem.addr)
        values.put(COLUMN_MAIL, tableItem.mail)
        values.put(COLUMN_ASUM, tableItem.aSum)
        writableDatabase.update(TABLE_NAME, values, "id = " +  tableItem.id, null)
        writableDatabase.close()
    }

    fun deleteAll() {
        val deleteAllFRomTableSQL = "DELETE FROM $TABLE_NAME"

        writableDatabase.execSQL(deleteAllFRomTableSQL)
    }


    companion object {
        private val DATABASE_NAME = "ConsDB.db"
        val TABLE_NAME = "Custumers"
        val COLUMN_ID = "id"
        val COLUMN_FIO = "FIO"
        val COLUMN_PHONE = "phone"
        val COLUMN_CARD = "card"
        val COLUMN_DATE = "date"
        val COLUMN_ADDR = "address "
        val COLUMN_MAIL = "mail "
        val COLUMN_ASUM = "aversSum"
    }
}