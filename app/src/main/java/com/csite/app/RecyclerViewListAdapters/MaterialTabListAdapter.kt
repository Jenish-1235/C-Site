package com.csite.app.RecyclerViewListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.MaterialRequestOrReceived
import com.csite.app.R
import java.text.DateFormat
import java.text.SimpleDateFormat

class MaterialTabListAdapter(materialList: ArrayList<MaterialRequestOrReceived>): RecyclerView.Adapter<MaterialTabListAdapter.MaterialTabViewHolder>() {
    private var materialList = materialList

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
//        date = date.substring(0, 10)
//        holder.dateTextView.text = date
        holder.materialNameView.text = currentItem.materialName
        holder.materialQuantityView.text = currentItem.materialQuantity + " " + currentItem.materialUnit
        holder.materialCategoryView.text = currentItem.materialCategory
        holder.materialStatusView.text = currentItem.type
    }

    override fun getItemCount(): Int {
        return materialList.size
    }
}