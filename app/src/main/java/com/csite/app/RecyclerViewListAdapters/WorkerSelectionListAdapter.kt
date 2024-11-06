package com.csite.app.RecyclerViewListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.ProjectWorker
import com.csite.app.Objects.Workforce
import com.csite.app.R
import com.google.android.material.button.MaterialButton.OnCheckedChangeListener

class WorkerSelectionListAdapter(workforceList: ArrayList<ProjectWorker>) : RecyclerView.Adapter<WorkerSelectionListAdapter.WorkerSelectionListViewHolder>(){
    var workForceList = workforceList

    var selectedWorkersHashMap = HashMap<String, ProjectWorker>()

    class WorkerSelectionListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workforceSelectedCheckbox: CheckBox = itemView.findViewById(R.id.workforceSelectedCheckbox)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkerSelectionListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_workers_selection, parent, false)
        return WorkerSelectionListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return workForceList.size
    }

    override fun onBindViewHolder(holder: WorkerSelectionListViewHolder, position: Int) {
        val currentItem = workForceList[position]
        holder.itemView.findViewById<TextView>(R.id.workforceTypeView).text = currentItem.wName
        holder.itemView.findViewById<TextView>(R.id.workforceCategoryView).text = currentItem.wCategory
        holder.itemView.findViewById<TextView>(R.id.workforceSalaryPerShiftView).text = "\u20b9" + currentItem.wSalaryPerDay.toString() + "/day"

        if (currentItem.wIsSelected){
            holder.workforceSelectedCheckbox.isChecked = true
            selectedWorkersHashMap.put(currentItem.wId, currentItem)
        }else{
            holder.workforceSelectedCheckbox.isChecked = false
        }

        holder.workforceSelectedCheckbox.setOnCheckedChangeListener{
            _, isChecked ->
            if(isChecked){
                val worker = ProjectWorker()
                worker.wId = currentItem.wId
                worker.wName = currentItem.wName
                worker.wCategory = currentItem.wCategory
                worker.wSalaryPerDay = currentItem.wSalaryPerDay
                worker.wIsSelected = true
                worker.wNoOfWorker = "0"
                worker.wNoOfShifts = "0"
                worker.wIsOvertime = false
                worker.wIsLate = false
                worker.wIsAllowance = false
                worker.wIsDeduction = false
                worker.wNote = ""
                worker.wOvertimeAmount = "0"
                worker.wLateAmount = "0"
                worker.wAllowanceAmount = "0"
                worker.wDeductionAmount = "0"
                selectedWorkersHashMap.put(worker.wId, worker)
            }else {
                selectedWorkersHashMap.remove(currentItem.wId)
            }
        }
    }

    fun getSelectedWorkers(): HashMap<String, ProjectWorker> {
        return selectedWorkersHashMap
    }

}