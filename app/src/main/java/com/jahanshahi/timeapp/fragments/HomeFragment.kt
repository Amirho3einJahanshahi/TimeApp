package com.jahanshahi.timeapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.jahanshahi.timeapp.R
import com.jahanshahi.timeapp.activities.EnterData
import com.jahanshahi.timeapp.activities.ExportToPdf
import com.jahanshahi.timeapp.activities.ExprtToExcel
import com.jahanshahi.timeapp.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment :Fragment(){
    companion object{
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view :View = inflater.inflate(R.layout.fragment_home,container,false)
        val time:RelativeLayout = view.findViewById(R.id.time_layout) as RelativeLayout
        val timeTable:RelativeLayout = view.findViewById(R.id.time_table_layout) as RelativeLayout
        val exportToPDF:RelativeLayout = view.findViewById(R.id.export_to_pdf) as RelativeLayout
        val exportToEXCEL:RelativeLayout = view.findViewById(R.id.export_to_excell) as RelativeLayout
        time.setOnClickListener(View.OnClickListener {
            context!!.startActivity(Intent(context,EnterData::class.java))
        })
        timeTable.setOnClickListener(View.OnClickListener {
            context!!.startActivity(Intent(context,MainActivity::class.java))
        })
        exportToPDF.setOnClickListener(View.OnClickListener {
            context!!.startActivity(Intent(context,ExportToPdf::class.java))
        })
        exportToEXCEL.setOnClickListener(View.OnClickListener {
            context!!.startActivity(Intent(context, ExprtToExcel::class.java))
        })
        return view
    }
}