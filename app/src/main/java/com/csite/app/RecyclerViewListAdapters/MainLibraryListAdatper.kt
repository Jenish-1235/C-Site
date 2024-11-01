package com.csite.app.RecyclerViewListAdapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Activites.Library.MaterialLibraryActivity
import com.csite.app.Activites.Library.PartyLibraryActivity
import com.csite.app.Activites.Library.WorkforceLibraryActivity
import com.csite.app.R

class MainLibraryListAdapter(context: Context, list: HashMap<String , String>): RecyclerView.Adapter<MainLibraryListAdapter.MainLibraryViewHolder>() {

    private var context: Context = context
    private var list = list

    class MainLibraryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val libraryName = itemView.findViewById<TextView>(R.id.libraryNameView)
        val libraryItem = itemView.findViewById<TextView>(R.id.libraryItemsView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainLibraryViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_library, parent, false)
        return MainLibraryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MainLibraryViewHolder, position: Int) {

        val library: String = list.keys.elementAt(position)
        val libraryItems: String = list.get(library).toString()

        holder.libraryName.text = library
        holder.libraryItem.text = libraryItems

        holder.itemView.setOnClickListener {

            if (library.equals("Material")){
                val materialLibraryIntent: Intent = Intent(context, MaterialLibraryActivity::class.java)
                context.startActivity(materialLibraryIntent)
            }else if (library.equals("Party")){
                val partyLibraryIntent: Intent = Intent(context, PartyLibraryActivity::class.java)
                context.startActivity(partyLibraryIntent)
            }else if (library.equals("Workforce")){
                val workforceLibraryIntent: Intent = Intent(context, WorkforceLibraryActivity::class.java)
                context.startActivity(workforceLibraryIntent)
            }

        }

    }

}