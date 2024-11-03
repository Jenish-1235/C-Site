package com.csite.app.RecyclerViewListAdapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.Party

class PartyLibraryListAdapter(context: Context, partyList: List<Party>) : RecyclerView.Adapter<PartyLibraryListAdapter.PartyLibraryViewHolder>(){

    private val mContext = context
    private val mPartyList = partyList

    class PartyLibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PartyLibraryListAdapter.PartyLibraryViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: PartyLibraryListAdapter.PartyLibraryViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}