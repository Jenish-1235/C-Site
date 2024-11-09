package com.csite.app.RecyclerViewListAdapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Activites.ProjectFeatures.AttendanceTab.MarkAttendanceActivity
import com.csite.app.Objects.Workforce
import com.csite.app.R

class AttendanceTabListAdapter(workforceList: HashMap<String, ArrayList<Workforce>>, currentDate: String, size:Int): RecyclerView.Adapter<AttendanceTabListAdapter.AttendanceListViewHolder>() {
    var workforceList = workforceList
    var date = currentDate
    val listOfWorkforce = ArrayList<Workforce>()
    val listOfContractor = ArrayList<String>()
    val size = size
    class AttendanceListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val workforceTypeView = itemView.findViewById<TextView>(R.id.workforceTypeView)
        val workforceSalaryView = itemView.findViewById<TextView>(R.id.workforceSalaryPerDayView)
        val workforceContractorView = itemView.findViewById<TextView>(R.id.workforceContractorNameView)
        val workforceMarkAttendanceButton = itemView.findViewById<TextView>(R.id.markAttendanceButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceListViewHolder {

        for(workforce in workforceList){
            for (workforceItem in workforce.value){
                listOfWorkforce.add(workforceItem)
                listOfContractor.add(workforce.key)
            }
        }
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_attendance_tab, parent, false)
        return AttendanceListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return size
    }

    override fun onBindViewHolder(holder: AttendanceListViewHolder, position: Int) {
        val currentWorkforce = listOfWorkforce[position]
        val currentContractor = listOfContractor[position]
        holder.workforceTypeView.text = currentWorkforce.workforceType
        holder.workforceSalaryView.text = currentWorkforce.workforceSalaryPerDay
        holder.workforceContractorView.text = currentContractor

        holder.workforceMarkAttendanceButton.setOnClickListener{
            val intent = Intent(holder.itemView.context, MarkAttendanceActivity::class.java)
            intent.putExtra("date", date)
            intent.putExtra("workforceId", currentWorkforce.workforceId)
            intent.putExtra("contractorName", currentContractor)
            holder.itemView.context.startActivity(intent)
        }
    }
}