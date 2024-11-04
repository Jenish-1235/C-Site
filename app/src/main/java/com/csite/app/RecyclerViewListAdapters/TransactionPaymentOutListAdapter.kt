package com.csite.app.RecyclerViewListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.TransactionPaymentIn
import com.csite.app.Objects.TransactionPaymentOut
import com.csite.app.R

class TransactionPaymentOutListAdapter(paymentOut: MutableList<TransactionPaymentOut>): RecyclerView.Adapter<TransactionPaymentOutListAdapter.TransactionViewHolder>() {
    var paymentOut = paymentOut
    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionDateView = itemView.findViewById<TextView>(R.id.transactionDateView)
        val transactionAmountView = itemView.findViewById<TextView>(R.id.transactionAmountView)
        val transactionPartyView = itemView.findViewById<TextView>(R.id.transactionPartyView)
        val transactionDescriptionView = itemView.findViewById<TextView>(R.id.transactionDescriptionView)
        val transactionTypeView = itemView.findViewById<TextView>(R.id.transactionTypeView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_transaction, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return paymentOut.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentItem = paymentOut[position]
        holder.transactionDateView.text = currentItem.paymentOutTransactionDate
        holder.transactionAmountView.text = currentItem.paymentOutTransactionAmount
        holder.transactionPartyView.text = currentItem.paymentOutTransactionPaymentToParty
        holder.transactionDescriptionView.text = currentItem.paymentOutTransactionDescription
        holder.transactionTypeView.text = "Payment Out"
        holder.transactionTypeView.setTextColor(holder.transactionTypeView.resources.getColor(R.color.red))
    }
}