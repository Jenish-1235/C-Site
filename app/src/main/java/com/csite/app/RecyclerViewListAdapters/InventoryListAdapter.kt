package com.csite.app.RecyclerViewListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.InventoryItem
import com.csite.app.R

class InventoryListAdapter(private val inventoryList: ArrayList<InventoryItem>, private val projectId: String): RecyclerView.Adapter<InventoryListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val materialName = itemView.findViewById<TextView>(R.id.materialNameView)
        val materialUnit = itemView.findViewById<TextView>(R.id.materialUnitView)
        val materialCategory = itemView.findViewById<TextView>(R.id.materialCategoryView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_inventory, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return inventoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = inventoryList[position]
        holder.materialName.text = currentItem.materialName
        holder.materialUnit.text = currentItem.totalQuantity.toString() + " " + currentItem.materialUnit
        holder.materialCategory.text = currentItem.materialCategory
    }
}