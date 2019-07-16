package com.jahanshahi.timeapp.history

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jahanshahi.timeapp.R
import com.jahanshahi.timeapp.activities.HomeActivity
import com.jahanshahi.timeapp.dataModel.TimeUtil
import com.jahanshahi.timeapp.dbUtil.DBHelper


class HistoryRecyclerViewAdapter(val context: Context, val items: MutableList<TimeUtil>) :
    RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder>() {
    val dbHelper: DBHelper = DBHelper(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items.get(position)
        holder.dateText.text = item.date
        holder.startTimeText.text = item.startTime
        holder.endTimeText.text = item.endTime
        holder.editImage.setOnClickListener(View.OnClickListener {
            showDialog(item,position)
        })
        holder.deleteImage.setOnClickListener(View.OnClickListener {
            dbHelper.deleteData(item)
            items.remove(item)
            notifyDataSetChanged()
        })
    }

    private fun showDialog(item: TimeUtil,index:Int){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_edit)
        val dateEt = dialog.findViewById(R.id.edit_date) as EditText
        val startHourEt = dialog.findViewById(R.id.edit_hour_start_et) as EditText
        val startMinuteEt = dialog.findViewById(R.id.edit_minute_start_et) as EditText
        val startSecondEt = dialog.findViewById(R.id.edit_second_start_et) as EditText
        val endHourET = dialog.findViewById(R.id.edit_hour_stop_et) as EditText
        val endMinuteEt = dialog.findViewById(R.id.edit_minute_stop_et) as EditText
        val endSecondEt = dialog.findViewById(R.id.edit_second_stop_et) as EditText
        val descEt = dialog.findViewById(R.id.edit_desc) as EditText
        val cancel = dialog.findViewById(R.id.edit_cancel) as TextView
        val apply = dialog.findViewById(R.id.edit_apply) as TextView
        var startHour: String = item.startTime.substring(0, 2)
        var startMinute: String = item.startTime.substring(3, 5)
        var startSecond: String = item.startTime.substring(6, 8)
        var endHour: String = item.endTime.substring(0, 2)
        var endMinute: String = item.endTime.substring(3, 5)
        var endSecond: String = item.endTime.substring(6, 8)
        dateEt.setText(item.date)
        startHourEt.setText(startHour)
        startMinuteEt.setText(startMinute)
        startSecondEt.setText(startSecond)
        endHourET.setText(endHour)
        endMinuteEt.setText(endMinute)
        endSecondEt.setText(endSecond)
        descEt.setText(item.desc)
        cancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        apply.setOnClickListener(View.OnClickListener {
            if (!dateEt.text.toString().isEmpty() && !startHourEt.text.toString().isEmpty() && !startMinuteEt.text.toString().isEmpty()
                && !startSecondEt.text.toString().isEmpty() && !endHourET.text.toString().isEmpty() && !endMinuteEt.text.toString().isEmpty()
                && !endSecondEt.text.toString().isEmpty() && !descEt.text.toString().isEmpty()
            ) {
                val updatedItem = TimeUtil(
                    item.id,
                    dateEt.text.toString(),
                    startHourEt.text.toString() +":"+ startMinuteEt.text.toString() +":"+startSecondEt.text.toString(),
                    endHourET.text.toString() + ":"+endMinuteEt.text.toString() + ":"+endSecondEt.text.toString(),
                    descEt.text.toString()
                )
                dbHelper.updateData(updatedItem)
                items.remove(item)
                items.add(index,updatedItem)
            }else{
                Toast.makeText(context,"Please Enter All Fields!",Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(context,"Updated!",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            notifyDataSetChanged()

        })
        dialog.show()
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dateText: TextView
        var startTimeText: TextView
        var endTimeText: TextView
        var editImage: ImageView
        var deleteImage: ImageView

        init {
            dateText = itemView.findViewById(R.id.date_text)
            startTimeText = itemView.findViewById(R.id.start_time_text)
            endTimeText = itemView.findViewById(R.id.end_time_text)
            editImage = itemView.findViewById(R.id.item_edit)
            deleteImage = itemView.findViewById(R.id.item_delete)
        }
    }
}