package com.csite.app.RecyclerViewListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.Contractor
import com.csite.app.R
import java.util.HashMap

class ContractorSelectionListAdapter(contractorList: ArrayList<Contractor>):RecyclerView.Adapter<ContractorSelectionListAdapter.ContractorSelectionViewHolder>() {
    var contractorList = contractorList

    class ContractorSelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val contractorNameView = itemView.findViewById<TextView>(R.id.contractorNameView)
        val contractorWorkforceCountView = itemView.findViewById<TextView>(R.id.numberOfWorkforceView)
        val contractorTotalSalaryView = itemView.findViewById<TextView>(R.id.contractorTotalSalaryView)
        val contractorPhoneView = itemView.findViewById<TextView>(R.id.contractorPhoneNumberView)
        val contractorIsSelectedInput = itemView.findViewById<CheckBox>(R.id.contractorSelectedInput)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContractorSelectionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_contractor_selection , parent, false)
        return ContractorSelectionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contractorList.size
    }

    override fun onBindViewHolder(holder: ContractorSelectionViewHolder, position: Int) {
        val currentItem = contractorList[position]
        holder.contractorNameView.text = currentItem.contractorName
        holder.contractorPhoneView.text = currentItem.contractorPhoneNumber
        holder.contractorIsSelectedInput.isChecked = currentItem.contractorSelection

        var totalSalary = 0.0
        var totalWorkforce = 0
        for(workforce in currentItem.contractorWorkforce){
            totalSalary += workforce.value.workforceSalaryPerDay.toDouble()
            totalWorkforce += 1
        }
        holder.contractorWorkforceCountView.text = "Workforces: ${totalWorkforce}"
        holder.contractorTotalSalaryView.text = "Total Salary\n \u20b9 ${totalSalary}"

        holder.contractorIsSelectedInput.setOnCheckedChangeListener{
            _, isChecked ->
            currentItem.contractorSelection = isChecked
            if (isChecked){
                selectedContractorIdList.put(currentItem.contractorId, currentItem)
            }else{
                selectedContractorIdList.remove(currentItem.contractorId)
            }
        }

    }

    var selectedContractorIdList = HashMap<String,Contractor>()
    fun sendSelectedContractorIdList():HashMap<String,Contractor>{
        return  selectedContractorIdList
    }
}