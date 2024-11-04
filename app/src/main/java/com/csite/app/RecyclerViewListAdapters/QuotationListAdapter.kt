package com.csite.app.RecyclerViewListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.Material
import com.csite.app.Objects.MaterialSelection
import com.csite.app.R
import com.site.app.Objects.Quotation

class QuotationListAdapter(quotationList: ArrayList<Quotation>): RecyclerView.Adapter<QuotationListAdapter.QuotationViewHolder>(){
    var quotationList = quotationList
    class QuotationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val quotationPartyView = itemView.findViewById<TextView>(R.id.quotationPartyView)
        val quotationNoteView = itemView.findViewById<TextView>(R.id.quotationNoteView)
        val quotationDateView = itemView.findViewById<TextView>(R.id.quotationDateView)
        val quotationAmountView = itemView.findViewById<TextView>(R.id.quotationAmountView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_quotation, parent, false)
        return QuotationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return quotationList.size
    }

    override fun onBindViewHolder(holder: QuotationViewHolder, position: Int) {
        val currentItem = quotationList[position]
        holder.quotationPartyView.text = currentItem.quoteParty
        holder.quotationNoteView.text = currentItem.quoteNotes
        holder.quotationDateView.text = currentItem.quoteDate
        holder.quotationAmountView.text = "\u20b9" + currentItem.quoteTotal

        holder.itemView.setOnClickListener{
            // Click Handle here.
        }
    }

}