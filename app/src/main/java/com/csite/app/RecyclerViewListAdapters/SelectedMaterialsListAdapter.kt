package com.csite.app.RecyclerViewListAdapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.MaterialSelection
import com.csite.app.R

class SelectedMaterialsListAdapter(materialList: ArrayList<MaterialSelection>): RecyclerView.Adapter<SelectedMaterialsListAdapter.SelectedMaterialsViewHolder>() {
    val materialList = materialList

    class SelectedMaterialsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val materialNameView = itemView.findViewById<TextView>(R.id.selectedMaterialNameView)
        val materialGSTView = itemView.findViewById<TextView>(R.id.selectedMaterialGSTView)
        val materialQuantityView = itemView.findViewById<EditText>(R.id.quantityInput)
        val materialUnitView = itemView.findViewById<TextView>(R.id.selectedMaterialUnitView)
        val materialRateView = itemView.findViewById<TextView>(R.id.unitRateInput)
        val subTotalView = itemView.findViewById<TextView>(R.id.subTotalView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedMaterialsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_selected_materials, parent, false)
        return SelectedMaterialsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return materialList.size
    }

    override fun onBindViewHolder(holder: SelectedMaterialsViewHolder, position: Int) {
        val currentItem = materialList[position]
        holder.materialNameView.text = currentItem.materialName
        holder.materialGSTView.text = currentItem.materialGST
        holder.materialUnitView.text = currentItem.materialUnit

        holder.materialQuantityView.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?){
                val quantity = holder.materialQuantityView.text.toString()
                val rate = holder.materialRateView.text.toString()
                if (quantity !="" && rate !=""){

                    holder.subTotalView.text = (quantity.toFloat() * rate.toFloat()).toString()
                }
            }

        })

        holder.materialRateView.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val quantity = holder.materialQuantityView.text.toString()
                val rate = holder.materialRateView.text.toString()

                if (quantity != "" && rate != "") {
                    holder.subTotalView.text = (quantity.toFloat() * rate.toFloat()).toString()
                }
            }

        })

    }

}