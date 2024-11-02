package com.csite.app.RecyclerViewListAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.Material
import com.csite.app.R

class MaterialLibraryListAdapter(context: Context, materialList: List<Material>) : RecyclerView.Adapter<MaterialLibraryListAdapter.MaterialLibraryViewHolder>() {
    private val mContext = context
    private val mMaterialList = materialList

    class MaterialLibraryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val materialNameView: TextView = itemView.findViewById(R.id.materialNameView)
        val materialCategoryView: TextView = itemView.findViewById(R.id.materialCategoryView)
        val materialUnitView: TextView = itemView.findViewById(R.id.materialUnitView)
        val materialGSTView: TextView = itemView.findViewById(R.id.materialGSTView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialLibraryViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_item_material_library, parent, false)
        return MaterialLibraryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mMaterialList.size
    }

    override fun onBindViewHolder(holder: MaterialLibraryViewHolder, position: Int) {
        val material = mMaterialList[position]
        holder.materialNameView.text= material.materialName
        holder.materialCategoryView.text = material.materialCategory
        holder.materialUnitView.text = material.materialUnit
        holder.materialGSTView.text = material.materialGST

        holder.itemView.setOnClickListener {
         Toast.makeText(mContext, "Click ${material.materialId}", Toast.LENGTH_SHORT).show()
        }
    }

}


