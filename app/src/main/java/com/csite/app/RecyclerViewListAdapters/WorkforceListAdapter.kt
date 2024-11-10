package com.csite.app.RecyclerViewListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.Workforce
import com.csite.app.R
import java.util.Stack

class WorkforceListAdapter(workforceList: Stack<Workforce>): RecyclerView.Adapter<WorkforceListAdapter.WorkforceViewHolder>() {
    val workforceList = workforceList
    class WorkforceViewHolder(view: View): RecyclerView.ViewHolder(view){
        val workforceTypeView = view.findViewById<TextView>(R.id.workforceTypeView)
        val workforceCategoryView = view.findViewById<TextView>(R.id.workforceCategoryView)
        val workforceSalaryPerDayView = view.findViewById<TextView>(R.id.workforceSalaryPerDayView)
        val workforceNumberOfWorkersView = view.findViewById<TextView>(R.id.workforceNumberOfWorkersView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkforceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_workforce, parent, false)
        return WorkforceViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return workforceList.size
    }

    override fun onBindViewHolder(holder: WorkforceViewHolder, position: Int) {
        val currentItem = workforceList[position]
        holder.workforceTypeView.text = currentItem.workforceType
        holder.workforceCategoryView.text = currentItem.workforceCategory
        holder.workforceSalaryPerDayView.text = "\u20b9 " + currentItem.workforceSalaryPerDay.toString()
        holder.workforceNumberOfWorkersView.text = "Workers: " + currentItem.workforceNumberOfWorkers
    }
}