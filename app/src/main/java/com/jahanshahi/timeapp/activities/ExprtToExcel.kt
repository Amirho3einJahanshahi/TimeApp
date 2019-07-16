package com.jahanshahi.timeapp.activities

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jahanshahi.timeapp.R
import com.jahanshahi.timeapp.csvUtil.CSVWriter
import com.jahanshahi.timeapp.dbUtil.DBHelper
import com.jahanshahi.timeapp.history.HistoryRecyclerViewAdapter
import java.io.File
import java.io.FileWriter


class ExprtToExcel : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_exprt_to_excel)
        val dbHelper = DBHelper(this)
        val recyclerView: RecyclerView = findViewById(R.id.excel_recycler_view)
        val historyRecyclerViewAdapter = HistoryRecyclerViewAdapter(this, dbHelper.readAllData())
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = historyRecyclerViewAdapter
        val export: Button = findViewById(R.id.export_to_excel_button)
        export.setOnClickListener(View.OnClickListener {
            if (isStoragePermissionGranted()) {
                exportDBToCSV(dbHelper)
            }
        })
    }

    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return false
            }
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Thanks!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun exportDBToCSV(dbhelper: DBHelper) {
        val exportDir = File(Environment.getExternalStorageDirectory(), "Time")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        val file = File(exportDir, "times.csv")
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            val db = dbhelper.readableDatabase
            val curCSV = db.rawQuery("SELECT * FROM times", null)
            val arrColumns = arrayOf(
                curCSV.getColumnName(1),
                curCSV.getColumnName(2),
                curCSV.getColumnName(3),
                curCSV.getColumnName(4)
            )
            csvWrite.writeNext(arrColumns)
            while (curCSV.moveToNext()) {
                val arrStr =
                    arrayOf(
                        curCSV.getString(1).toString(),
                        curCSV.getString(2),
                        curCSV.getString(3),
                        curCSV.getString(4)
                    )
                csvWrite.writeNext(arrStr)
            }
            csvWrite.close()
            curCSV.close()
        } catch (sqlEx: Exception) {
            Log.e("EnterDate : ", sqlEx.message, sqlEx)
        }

    }
}
