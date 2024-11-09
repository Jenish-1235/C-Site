package com.csite.app.RecyclerViewListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.Contractor
import com.csite.app.Objects.Workforce
import com.csite.app.R

class ContractorLibraryListAdapter(contractorList: ArrayList<Contractor>): RecyclerView.Adapter<ContractorLibraryListAdapter.ContractorListViewHolder>() {
    var contractorList = contractorList
    class ContractorListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val contractorNameView = itemView.findViewById<TextView>(R.id.contractorNameView)
        val contractorMobileNumberView = itemView.findViewById<TextView>(R.id.contractorMobileNumberView)
        val contractorTotalSalaryView = itemView.findViewById<TextView>(R.id.contractorTotalSalaryView)
        val contractorNumberOfWorkforceView = itemView.findViewById<TextView>(R.id.contractorNumberOfWorkforceView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractorListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_contractor_library, parent, false)
        return ContractorListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contractorList.size
    }

    override fun onBindViewHolder(holder: ContractorListViewHolder, position: Int) {
        val currentItem = contractorList[position]
        holder.contractorNameView.text = currentItem.contractorName
        holder.contractorMobileNumberView.text = currentItem.contractorPhoneNumber
        var totalSalary = 0.0
        var workforceCount = 0
        for (workforce in currentItem.contractorWorkforce){
            val currentWorkforce = workforce.value
            totalSalary += currentWorkforce.workforceSalaryPerDay.toDouble()
            workforceCount++
        }
        holder.contractorTotalSalaryView.text = "Total Salary: \u20b9 ${totalSalary}"
        holder.contractorNumberOfWorkforceView.text = "Total Workforces: ${workforceCount}"

    }
}