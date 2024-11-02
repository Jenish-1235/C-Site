package com.csite.app.RecyclerViewListAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.Workforce
import com.csite.app.R

class WorkforceLibraryListAdapter(context: Context , workforceList: ArrayList<Workforce>): RecyclerView.Adapter<WorkforceLibraryListAdapter.WorkforceLibraryViewHolder>() {
    val context = context
    var workforceList = workforceList
    class WorkforceLibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workforceTypeView = itemView.findViewById<TextView>(R.id.workforceTypeView)
        val workforceCategoryView = itemView.findViewById<TextView>(R.id.workforceCategoryView)
        val workforceSalaryPerShiftView = itemView.findViewById<TextView>(R.id.workforceSalaryPerShiftView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkforceLibraryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_workforce_library,parent,false)
        return WorkforceLibraryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return workforceList.size
    }

    override fun onBindViewHolder(holder: WorkforceLibraryViewHolder, position: Int) {
        val workforce = workforceList[position]
        holder.workforceTypeView.text = workforce.workforceType
        holder.workforceCategoryView.text = workforce.workforceCategory
        holder.workforceSalaryPerShiftView.text = workforce.workforceSalaryPerShift

        holder.itemView.setOnClickListener {
         Toast.makeText(context, "Workforce ${workforce.workforceId} clicked", Toast.LENGTH_SHORT).show()
        }

    }

}