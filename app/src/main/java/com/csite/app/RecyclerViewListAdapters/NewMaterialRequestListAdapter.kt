package com.csite.app.RecyclerViewListAdapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Activites.ProjectFeatures.MaterialTab.NewMaterialRequestActivity
import com.csite.app.Objects.Material
import com.csite.app.Objects.MaterialSelection
import com.csite.app.R
import java.util.HashMap

class NewMaterialRequestListAdapter(materialList: ArrayList<MaterialSelection>): RecyclerView.Adapter<NewMaterialRequestListAdapter.NewMaterialRequestViewHolder>() {
    val materialList = materialList
    var soldOrPurchasedMaterialList = HashMap<String, MaterialSelection>()
    class NewMaterialRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val materialNameView = itemView.findViewById<TextView>(R.id.selectedMaterialNameView)
        val materialGSTView = itemView.findViewById<TextView>(R.id.selectedMaterialGSTView)
        val materialQuantityView = itemView.findViewById<EditText>(R.id.quantityInput)
        val materialUnitView = itemView.findViewById<TextView>(R.id.selectedMaterialUnitView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewMaterialRequestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_new_material_request, parent, false)
        return NewMaterialRequestViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return materialList.size
    }

    override fun onBindViewHolder(holder: NewMaterialRequestViewHolder, position: Int) {
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
                if(quantity.isEmpty() || quantity.equals("0")) {
                    soldOrPurchasedMaterialList.remove(currentItem.materialName)
                }else {
                    currentItem.materialQuantity = quantity
                    soldOrPurchasedMaterialList.put(currentItem.materialId, currentItem)
                }
            }

        })


    }

    fun getFinalMaterialList(): HashMap<String, MaterialSelection>{
        return soldOrPurchasedMaterialList
    }

}