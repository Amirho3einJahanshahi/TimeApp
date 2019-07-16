package com.jahanshahi.timeapp.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.jahanshahi.timeapp.R
import com.jahanshahi.timeapp.dataModel.TimeUtil
import com.jahanshahi.timeapp.csvUtil.CSVWriter
import com.jahanshahi.timeapp.dbUtil.DBHelper
import kotlinx.android.synthetic.main.activity_enter_data.*
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter


class EnterData : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_enter_data)
        val dbHelper: DBHelper = DBHelper(this)
        var start_hour: EditText = findViewById(R.id.hour_start_et) as EditText
        var start_minute: EditText = findViewById(R.id.minute_start_et) as EditText
        var start_second: EditText = findViewById(R.id.second_start_et) as EditText
        var end_hour: EditText = findViewById(R.id.hour_stop_et) as EditText
        var end_minute: EditText = findViewById(R.id.minute_stop_et) as EditText
        var end_second: EditText = findViewById(R.id.second_stop_et) as EditText
        var des:EditText = findViewById(R.id.desc_et) as EditText
        var submit: Button = findViewById(R.id.submit);
        submit.setOnClickListener(View.OnClickListener {
            if (validateHour(start_hour) && validateMinute(start_minute) && validateSecond(start_second)
                && validateHour(end_hour) && validateMinute(end_minute) && validateSecond(end_second)
            ) {
                val persionDate: PersianDate = PersianDate()
                val persianDateFormat: PersianDateFormat = PersianDateFormat("Y/m/d")
                val date: String = persianDateFormat.format(persionDate)
                val startTime: String =
                    start_hour.text.toString() + ":" + start_minute.text.toString() + ":" + start_second.text.toString()
                val endTime: String =
                    end_hour.text.toString() + ":" + end_minute.text.toString() + ":" + end_second.text.toString()
                val desc:String = desc_et.text.toString()
                val time = TimeUtil(1, date, startTime, endTime,desc)
                if (dbHelper.addData(time) != -1L) {
                    Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()
                }
            }
//            Toast.makeText(applicationContext, dbHelper.readData("1398/04/21")?.startTime.toString(),Toast.LENGTH_SHORT).show()
        })
    }

    fun validateHour(et: EditText): Boolean {
        if (et.text.isEmpty()) {
            Toast.makeText(applicationContext, "Please Enter Value", Toast.LENGTH_SHORT).show()
            return false
        } else if (et.text.toString().toInt() > 23) {
            Toast.makeText(applicationContext, "Please Enter Valid Value", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    fun validateMinute(et: EditText): Boolean {
        if (et.text.isEmpty()) {
            Toast.makeText(applicationContext, "Please Enter Value", Toast.LENGTH_SHORT).show()
            return false
        } else if (et.text.toString().toInt() > 59) {
            Toast.makeText(applicationContext, "Please Enter Valid Value", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    fun validateSecond(et: EditText): Boolean {
        if (et.text.isEmpty()) {
            Toast.makeText(applicationContext, "Please Enter Value", Toast.LENGTH_SHORT).show()
            return false
        } else if (et.text.toString().toInt() > 59) {
            Toast.makeText(applicationContext, "Please Enter Valid Value", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun exportDBToCSV(dbhelper: DBHelper) {
        val exportDir = File(Environment.getExternalStorageDirectory(), "")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "times.csv")
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            val db = dbhelper.readableDatabase
            val curCSV = db.rawQuery("SELECT * FROM times", null)
            csvWrite.writeNext(curCSV.columnNames)
            while (curCSV.moveToNext()) {
                val arrStr =
                    arrayOf(curCSV.getInt(0).toString(), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3))
                csvWrite.writeNext(arrStr)
            }
            csvWrite.close()
            curCSV.close()
        } catch (sqlEx: Exception) {
            Log.e("EnterDate : ", sqlEx.message, sqlEx)
        }

    }

}
