package com.jahanshahi.timeapp.dbUtil

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.jahanshahi.timeapp.dataModel.TimeUtil

class DBHelper(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME,null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "time.db"
        private const val TABLE = "times"
        private const val KEY_ID = "id"
        private const val KEY_DATE = "date"
        private const val KEY_START_TIME = "start_time"
        private const val KEY_END_TIME = "end_time"
        private const val KEY_DESCRIPTION = "description"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_TABLE = ("CREATE TABLE " + TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_DATE + " TEXT, "
                + KEY_START_TIME + " TEXT, "
                + KEY_END_TIME + " TEXT, "
                + KEY_DESCRIPTION + " TEXT "
                + ");")
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE)
        onCreate(db)
    }


    //method to insert data
    fun addData(timeUtil: TimeUtil):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_DATE, timeUtil.date)
        contentValues.put(KEY_START_TIME,timeUtil.startTime)
        contentValues.put(KEY_END_TIME,timeUtil.endTime)
        contentValues.put(KEY_DESCRIPTION,timeUtil.desc)
        val success = db.insert(TABLE, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to read data
    fun readAllData():MutableList<TimeUtil>{
        val list:ArrayList<TimeUtil> = ArrayList()
        val selectQuery = "SELECT  * FROM $TABLE"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var date: String
        var startTime: String
        var endTime: String
        var desc : String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                date = cursor.getString(cursor.getColumnIndex("date"))
                startTime = cursor.getString(cursor.getColumnIndex("start_time"))
                endTime = cursor.getString(cursor.getColumnIndex("end_time"))
                desc = cursor.getString(cursor.getColumnIndex("description"))
                val timeUtil=
                    TimeUtil(
                        id = id,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        desc = desc
                    )
                list.add(timeUtil)
            } while (cursor.moveToNext())
        }
        return list
    }

    fun readData(date: String): TimeUtil? {
        val db = this.writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE WHERE $KEY_DATE = ?"
        db.rawQuery(selectQuery, arrayOf(date)).use { // .use requires API 16
            if (it.moveToFirst()) {
                val myId = it.getInt(it.getColumnIndex(KEY_ID))
                val myDate = it.getString(it.getColumnIndex(KEY_DATE))
                val myStartTime = it.getString(it.getColumnIndex(KEY_START_TIME))
                val myEndTime = it.getString(it.getColumnIndex(KEY_END_TIME))
                val myDesc = it.getString(it.getColumnIndex(KEY_DESCRIPTION))
                return TimeUtil(myId, myDate, myStartTime, myEndTime,myDesc)
            }
        }
        return null
    }

    //method to update data
    fun updateData(timeUtil: TimeUtil):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, timeUtil.id)
        contentValues.put(KEY_DATE, timeUtil.date)
        contentValues.put(KEY_START_TIME,timeUtil.startTime)
        contentValues.put(KEY_END_TIME,timeUtil.endTime)
        contentValues.put(KEY_DESCRIPTION,timeUtil.desc)
        // Updating Row
        val success = db.update(TABLE, contentValues,"id="+timeUtil.id,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteData(timeUtil: TimeUtil):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, timeUtil.id) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE,"id="+timeUtil.id,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}