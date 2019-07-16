package com.jahanshahi.timeapp.activities

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.jahanshahi.timeapp.R
import com.jahanshahi.timeapp.dbUtil.DBHelper
import com.jahanshahi.timeapp.history.HistoryRecyclerViewAdapter
import java.io.File
import java.io.FileOutputStream

class ExportToPdf : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_export_to_pdf)
        val dbHelper= DBHelper(this)
        val recyclerView: RecyclerView = findViewById(R.id.pdf_recycler_view)
        val historyRecyclerViewAdapter = HistoryRecyclerViewAdapter(this,dbHelper.readAllData())
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = historyRecyclerViewAdapter
        val export:Button = findViewById(R.id.export_to_pdf_button)
        export.setOnClickListener(View.OnClickListener {
            if (isStoragePermissionGranted()){
                sqlToPdf(dbHelper)
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
            Toast.makeText(this,"Thanks!", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    fun sqlToPdf(dbhelper: DBHelper) {
        val db = dbhelper.getWritableDatabase()
        val c1 = db.rawQuery("SELECT * FROM times", null)
        val filename = "times.pdf"
        val document = Document()  // create the document
        val root = File(Environment.getExternalStorageDirectory(), "Notes")
        if (!root.exists()) {
            root.mkdirs()   // create root directory in sdcard
        }
        val gpxfile = File(root, filename) // generate pdf_ic file in that directory
        PdfWriter.getInstance(document, FileOutputStream(gpxfile))
        document.open()  // open the directory
        val p3 =
            Paragraph()  // to enter value you have to create paragraph  and add value in it then paragraph is added into document
        p3.add("Times : \n\n")
        p3.alignment = Paragraph.ALIGN_CENTER
        document.add(p3)
        // now for ad table in pdf_ic use below code
        val table = PdfPTable(4) // Code 1
        // Code 2
        table.addCell("Date")
        table.addCell("Start Time")
        table.addCell("End Time")
        table.addCell("Description")
        // now fetch data from database and display it in pdf_ic
        while (c1.moveToNext()) {
            // get the value from database
            val date = c1.getString(1)
            val startTime = c1.getString(2)
            val endTime = c1.getString(3)
            val desc = c1.getString(4)

            table.addCell(date)
            table.addCell(startTime)
            table.addCell(endTime)
            table.addCell(desc)
        }
        // add table into document
        document.add(table)
        document.addCreationDate()
        document.close()
    }
}
