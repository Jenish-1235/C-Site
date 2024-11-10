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

class AttendanceTabListAdapter(workforceList: HashMap<String, ArrayList<Workforce>>, currentDate: String, size:Int, projectId:String): RecyclerView.Adapter<AttendanceTabListAdapter.AttendanceListViewHolder>() {
    var workforceList = workforceList
    var date = currentDate
    val listOfWorkforce = ArrayList<Workforce>()
    val listOfContractor = ArrayList<String>()
    val size = size
    val projectId = projectId
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

        if (currentWorkforce.workforcePresentWorkers.toInt() == 0){
            holder.workforceMarkAttendanceButton.text = "Mark Attendance"
        }else{
            holder.workforceMarkAttendanceButton.text = "Update Attendance"
            holder.workforceMarkAttendanceButton.backgroundTintList = holder.itemView.context.getColorStateList(R.color.green)
        }

        holder.workforceMarkAttendanceButton.setOnClickListener{
            val intent = Intent(holder.itemView.context, MarkAttendanceActivity::class.java)
            intent.putExtra("date", date)
            intent.putExtra("contractorName", currentContractor)
            intent.putExtra("projectId", projectId)

            intent.putExtra("workforceId", currentWorkforce.workforceId)
            intent.putExtra("workforceCategory", currentWorkforce.workforceCategory)
            intent.putExtra("workforceType", currentWorkforce.workforceType)
            intent.putExtra("salaryPerDay", currentWorkforce.workforceSalaryPerDay)
            intent.putExtra("workforceNumberOfPresent", currentWorkforce.workforcePresentWorkers)
            intent.putExtra("workforceNumberOfAbsent", currentWorkforce.workforceAbsentWorkers)
            intent.putExtra("workforceIsOverTime", currentWorkforce.workforceIsOverTime)
            intent.putExtra("workforceIsLate", currentWorkforce.workforceIsLate)
            intent.putExtra("workforceHasAllowance", currentWorkforce.workforceHasAllowance)
            intent.putExtra("workforceHasDeduction", currentWorkforce.workforceHasDeduction)
            intent.putExtra("overtimeAmount", currentWorkforce.workforceOverTimePay)
            intent.putExtra("lateAmount", currentWorkforce.workforceLateFine)
            intent.putExtra("allowanceAmount", currentWorkforce.workforceAllowance)
            intent.putExtra("deductionAmount", currentWorkforce.workforceDeduction)
            intent.putExtra("totalWorkers", currentWorkforce.workforceNumberOfWorkers)

            holder.itemView.context.startActivity(intent)
        }
    }
}