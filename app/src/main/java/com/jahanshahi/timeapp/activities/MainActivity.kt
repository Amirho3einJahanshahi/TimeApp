package com.jahanshahi.timeapp.activities

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.jahanshahi.timeapp.R
import com.jahanshahi.timeapp.dataModel.TimeUtil
import com.jahanshahi.timeapp.dbUtil.DBHelper
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)
        val dbHelper: DBHelper = DBHelper(this)
        val button_start : Button = findViewById(R.id.button_start) as Button
        val button_stop : Button = findViewById(R.id.button_stop) as Button
        val button_save : Button = findViewById(R.id.button_save) as Button
        val dateText : TextView = findViewById(R.id.date_text) as TextView
        val startTimeText : TextView = findViewById(R.id.start_time_text) as TextView
        val endTimeText : TextView = findViewById(R.id.end_time_text) as TextView
        val descEt: EditText = findViewById(R.id.desc_et) as EditText
        button_start.setOnClickListener(View.OnClickListener {
            var persionDate = PersianDate()
            var persianDateFormat = PersianDateFormat("Y/m/d")
            var timeFormat = PersianDateFormat("H:m:s")
            dateText.text = persianDateFormat.format(persionDate)
            startTimeText.text = timeFormat.format(persionDate)
            button_start.visibility = View.GONE
            button_stop.visibility = View.VISIBLE
        })
        button_stop.setOnClickListener(View.OnClickListener {
            var persionDate : PersianDate = PersianDate()
            var persianTimeFormat : PersianDateFormat = PersianDateFormat("H:m:s")
            endTimeText.text = persianTimeFormat.format(persionDate)
            button_stop.visibility = View.GONE
            button_save.visibility = View.VISIBLE
            descEt.visibility = View.VISIBLE
        })
        button_save.setOnClickListener(View.OnClickListener {
            val time = TimeUtil(1 , dateText.text.toString(),startTimeText.text.toString(),endTimeText.text.toString(),descEt.text.toString())
            dbHelper.addData(time)
        })
    }
}
