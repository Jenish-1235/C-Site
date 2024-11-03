package com.csite.app.RecyclerViewListAdapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.material3.TopAppBar
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.Party
import com.csite.app.R

class PartyLibraryListAdapter(context: Context, partyList: List<Party>) : RecyclerView.Adapter<PartyLibraryListAdapter.PartyLibraryViewHolder>(){

    private val mContext = context
    private val mPartyList = partyList

    class PartyLibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val partyNameView = itemView.findViewById<TextView>(R.id.partyNameView)
        val partyTypeView = itemView.findViewById<TextView>(R.id.partyTypeView)
        val partyAmountView = itemView.findViewById<TextView>(R.id.partyAmountView)
        val partyAmountRemarkView = itemView.findViewById<TextView>(R.id.partyAmountRemarkView)
        val partyGSTView = itemView.findViewById<TextView>(R.id.partyGSTView)
        val partyBankView = itemView.findViewById<TextView>(R.id.partyBankView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PartyLibraryListAdapter.PartyLibraryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_party_library, parent, false)
        return PartyLibraryViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: PartyLibraryListAdapter.PartyLibraryViewHolder,
        position: Int
    ) {
        val party = mPartyList[position]
        holder.itemView.setOnClickListener {
            Toast.makeText(mContext, "Party Clicked ${party.partyId}", Toast.LENGTH_SHORT).show()
        }
        holder.partyNameView.text = party.partyName
        holder.partyTypeView.text = party.partyType
        holder.partyAmountView.text = party.partyAmountToPayOrReceive + " INR"
        holder.partyAmountRemarkView.text = party.partyOpeningBalanceDetails

        if (party.partyCondition.equals("000") || party.partyCondition.equals("100") ){
            holder.partyGSTView.setBackgroundResource(R.drawable.background_red_rounded_corner)
            holder.partyBankView.setBackgroundResource(R.drawable.background_red_rounded_corner)
        }else if (party.partyCondition.equals("001") || party.partyCondition.equals("101")){
            holder.partyGSTView.setBackgroundResource(R.drawable.background_red_rounded_corner)
            holder.partyBankView.setBackgroundResource(R.drawable.background_green_rounded_corner)
        }else if (party.partyCondition.equals("010") || party.partyCondition.equals("110")){
            holder.partyGSTView.setBackgroundResource(R.drawable.background_green_rounded_corner)
            holder.partyBankView.setBackgroundResource(R.drawable.background_red_rounded_corner)
        }else if (party.partyCondition.equals("111") || party.partyCondition.equals("011")){
            holder.partyGSTView.setBackgroundResource(R.drawable.background_green_rounded_corner)
            holder.partyBankView.setBackgroundResource(R.drawable.background_green_rounded_corner)
        }

    }

    override fun getItemCount(): Int {
        return mPartyList.size
    }


}