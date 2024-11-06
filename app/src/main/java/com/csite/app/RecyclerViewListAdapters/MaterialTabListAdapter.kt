package com.csite.app.RecyclerViewListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalMaterialTab
import com.csite.app.Objects.MaterialRequestOrReceived
import com.csite.app.R
import java.text.DateFormat
import java.text.SimpleDateFormat

class MaterialTabListAdapter(materialList: ArrayList<MaterialRequestOrReceived>, projectId: String): RecyclerView.Adapter<MaterialTabListAdapter.MaterialTabViewHolder>() {
    private var materialList = materialList
    private var projectId = projectId

    class MaterialTabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView = itemView.findViewById<TextView>(R.id.materialDateView)
        val materialNameView = itemView.findViewById<TextView>(R.id.materialNameView)
        val materialQuantityView = itemView.findViewById<TextView>(R.id.materialUnitView)
        val materialCategoryView = itemView.findViewById<TextView>(R.id.materialCategoryView)
        val materialStatusView = itemView.findViewById<TextView>(R.id.materialStatusView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MaterialTabListAdapter.MaterialTabViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_material_tab, parent, false)
        return MaterialTabViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: MaterialTabListAdapter.MaterialTabViewHolder,
        position: Int
    ) {
        val currentItem = materialList[position]
        var date = currentItem.dateTimeStamp
//
        date = date.substring(0, 10)
        holder.dateTextView.text = date
        holder.materialNameView.text = currentItem.materialName
        holder.materialQuantityView.text = currentItem.materialQuantity + " " + currentItem.materialUnit
        holder.materialCategoryView.text = currentItem.materialCategory
        holder.materialStatusView.text = currentItem.type

        if (holder.materialStatusView.text == "Received") {
            holder.materialStatusView.setTextColor(holder.materialStatusView.context.resources.getColor(R.color.green))
            holder.materialStatusView.setOnClickListener{
                val firebaseOperationsForProjectInternalMaterialTab = FirebaseOperationsForProjectInternalMaterialTab()
                val materialRequestOrReceived = MaterialRequestOrReceived()
                materialRequestOrReceived.materialId = currentItem.materialId
                materialRequestOrReceived.type = "Request"
                materialRequestOrReceived.dateTimeStamp = currentItem.dateTimeStamp
                materialRequestOrReceived.materialName = currentItem.materialName
                materialRequestOrReceived.materialQuantity = currentItem.materialQuantity
                materialRequestOrReceived.materialUnit = currentItem.materialUnit
                materialRequestOrReceived.materialCategory = currentItem.materialCategory
                if (projectId != null && projectId != "") {
                    firebaseOperationsForProjectInternalMaterialTab.updateMaterialReceivedToRequest(
                        projectId,
                        materialRequestOrReceived
                    )
                }
                materialList.clear()
            }
        } else {
            holder.materialStatusView.setTextColor(holder.materialStatusView.context.resources.getColor(R.color.red))
            holder.materialStatusView.setOnClickListener{
                val firebaseOperationsForProjectInternalMaterialTab = FirebaseOperationsForProjectInternalMaterialTab()
                val materialRequestOrReceived = MaterialRequestOrReceived()
                materialRequestOrReceived.materialId = currentItem.materialId
                materialRequestOrReceived.type = "Received"
                materialRequestOrReceived.dateTimeStamp = currentItem.dateTimeStamp
                materialRequestOrReceived.materialName = currentItem.materialName
                materialRequestOrReceived.materialQuantity = currentItem.materialQuantity
                materialRequestOrReceived.materialUnit = currentItem.materialUnit
                materialRequestOrReceived.materialCategory = currentItem.materialCategory
                if (projectId != null && projectId != "") {
                    firebaseOperationsForProjectInternalMaterialTab.updateMaterialRequestToReceived(
                        projectId,
                        materialRequestOrReceived
                    )
                }
                materialList.clear()
            }
        }

    }

    override fun getItemCount(): Int {
        return materialList.size
    }
}