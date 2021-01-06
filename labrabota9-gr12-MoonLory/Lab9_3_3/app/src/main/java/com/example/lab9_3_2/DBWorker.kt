package com.example.lab9_3_2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.Editable
import android.util.Log

import java.io.Serializable
import kotlin.math.cos

class Car(val id: Int, var mark: String, val number: String,
          var color: String, var year: Int, var cost: Int): Serializable

class DBHelper(context: Context, name: String?,
               factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = ("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MARK + " TEXT NOT NULL, "
                + COLUMN_NUMBER + " TEXT NOT NULL);")

        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        if (newVersion > oldVersion) {
            db.execSQL( "ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_COLOR TEXT;")
            db.execSQL( "ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_YEAR INTEGER;")
            db.execSQL( "ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_COST INTEGER;")

        }
    }

    fun addNewItemToTable(table: Car) {
        val values = ContentValues()
        values.put(COLUMN_MARK, table.mark)
        values.put(COLUMN_NUMBER, table.number)

        values.put(COLUMN_COLOR, table.color)
        values.put(COLUMN_YEAR, table.year)
        values.put(COLUMN_COST, table.cost)

        writableDatabase.insert(TABLE_NAME, null, values)
        writableDatabase.close()
    }

    fun getOldTable(): ArrayList<Car> {
        val querySQL =
                "SELECT * FROM $TABLE_NAME"

        val cursor = writableDatabase.rawQuery(querySQL, null)

        var table = ArrayList<Car>()

        if (cursor.moveToFirst()) {
            do {
                val id = Integer.parseInt(cursor.getString(0))
                val name = cursor.getString(1)
                val time = cursor.getString(2)

                table.add(Car(id, name, time, "", 0, 0))
            } while (cursor.moveToNext())

        }
        cursor.close()
        writableDatabase.close()
        return table
    }

    fun getTable(): ArrayList<Car> {
        val querySQL =
            "SELECT * FROM $TABLE_NAME"

        val cursor = writableDatabase.rawQuery(querySQL, null)

        var table = ArrayList<Car>()

        if (cursor.moveToFirst()) {
            do {
                val id = Integer.parseInt(cursor.getString(0))
                val name = cursor.getString(1)
                val time = cursor.getString(2)

                val color = cursor.getString(3) ?: ""
                val year = (cursor.getString(4) ?: "").toIntOrNull() ?: 0
                val cost = (cursor.getString(5) ?: "").toIntOrNull() ?: 0

                table.add(Car(id, name, time, color, year, cost))
            } while (cursor.moveToNext())

        }
        cursor.close()
        writableDatabase.close()
        return table
    }

    fun updateItem(tableItem: Car) {
        var values = ContentValues()
        values.put(COLUMN_ID, tableItem.id)
        values.put(COLUMN_MARK, tableItem.mark)
        values.put(COLUMN_NUMBER, tableItem.number)

        values.put(COLUMN_COLOR, tableItem.color)
        values.put(COLUMN_YEAR, tableItem.year)
        values.put(COLUMN_COST, tableItem.cost)

        writableDatabase.update(TABLE_NAME, values, "id = " +  tableItem.id, null)
        writableDatabase.close()
    }

    fun deleteAll() {
        val deleteAllFRomTableSQL = "DELETE FROM $TABLE_NAME"

        writableDatabase.execSQL(deleteAllFRomTableSQL)
    }


    companion object {
        private val DATABASE_NAME = "carsDB.db"
        val TABLE_NAME = "cars"
        val COLUMN_ID = "id"
        val COLUMN_MARK = "mark"
        val COLUMN_NUMBER = "number"
        val COLUMN_COLOR = "color"
        val COLUMN_YEAR = "year"
        val COLUMN_COST = "cost"
    }
}