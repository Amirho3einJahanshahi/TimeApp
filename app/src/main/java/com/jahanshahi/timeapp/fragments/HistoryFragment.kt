package com.jahanshahi.timeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jahanshahi.timeapp.R
import com.jahanshahi.timeapp.dataModel.TimeUtil
import com.jahanshahi.timeapp.dbUtil.DBHelper
import com.jahanshahi.timeapp.history.HistoryRecyclerViewAdapter

class HistoryFragment : Fragment(){
    companion object{
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_history,container,false)
        val dbHelper: DBHelper = DBHelper(context!!)
        val recyclerView:RecyclerView = view.findViewById(R.id.history_recycler_view)
        val historyRecyclerViewAdapter : HistoryRecyclerViewAdapter = HistoryRecyclerViewAdapter(context!!,dbHelper.readAllData())
        recyclerView.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = historyRecyclerViewAdapter
        return view
    }
}