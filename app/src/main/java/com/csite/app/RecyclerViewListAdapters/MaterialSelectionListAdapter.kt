package com.csite.app.RecyclerViewListAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.Material
import com.csite.app.Objects.MaterialSelection
import com.csite.app.R

class MaterialSelectionListAdapter(materialList: ArrayList<Material>) : RecyclerView.Adapter<MaterialSelectionListAdapter.MaterialSelectionViewHolder>(){
    var materialList: ArrayList<Material> = materialList
    var selectedMaterialHashMap: HashMap<String, MaterialSelection> = HashMap()
    class MaterialSelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var materialNameView = itemView.findViewById<TextView>(R.id.materialNameView)
        var materialGSTView = itemView.findViewById<TextView>(R.id.materialGSTView)
        var materialUnitView = itemView.findViewById<TextView>(R.id.materialUnitView)
        var materialCategoryView = itemView.findViewById<TextView>(R.id.materialCategoryView)
        var materialSelectedInput = itemView.findViewById<CheckBox>(R.id.materialSelectedInput)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialSelectionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_material_selection, parent, false)
        return MaterialSelectionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return materialList.size
    }

    override fun onBindViewHolder(holder: MaterialSelectionViewHolder, position: Int) {
        val material = materialList[position]

        // Set the material details
        holder.materialNameView.text = material.materialName
        holder.materialGSTView.text = material.materialGST
        holder.materialUnitView.text = material.materialUnit
        holder.materialCategoryView.text = material.materialCategory

        // Remove any previously attached listener to avoid unwanted triggers
        holder.materialSelectedInput.setOnCheckedChangeListener(null)

        // Set checkbox state based on the model's `isSelected` property
        holder.materialSelectedInput.isChecked = material.isSelected

        // Attach a listener to update the `isSelected` property and HashMap
        holder.materialSelectedInput.setOnCheckedChangeListener { _, isChecked ->
            material.isSelected = isChecked // Update selection state in the model
            if (isChecked) {
                // Add the item to the selected items HashMap
                val materialSelection = MaterialSelection(
                    material.materialName,
                    material.materialCategory,
                    material.materialGST,
                    material.materialUnit,
                    material.materialId,
                    true
                )
                selectedMaterialHashMap[material.materialId] = materialSelection
            } else {
                // Remove the item from the selected items HashMap
                selectedMaterialHashMap.remove(material.materialId)
            }
        }
    }


    fun getSelectedMaterialHashmap():HashMap<String, MaterialSelection>{
        return selectedMaterialHashMap
    }


}