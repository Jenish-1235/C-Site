package com.csite.app.RecyclerViewListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.TransactionOtherExpense
import com.csite.app.Objects.TransactionPaymentIn
import com.csite.app.R

class TransactionOtherExpenseListAdapter(otherExpense: MutableList<TransactionOtherExpense>): RecyclerView.Adapter<TransactionOtherExpenseListAdapter.TransactionViewHolder>() {
    var otherExpense = otherExpense
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
        return otherExpense.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentItem = otherExpense[position]
        holder.transactionDateView.text = currentItem.otherExpenseTransactionDate
        holder.transactionAmountView.text = currentItem.otherExpenseTransactionTotalAmount
        holder.transactionPartyView.text = currentItem.otherExpenseTransactionTo
        holder.transactionDescriptionView.text = currentItem.otherExpenseTransactionNotes
        holder.transactionTypeView.text = "Other Expense"
        holder.transactionTypeView.setTextColor(holder.transactionTypeView.resources.getColor(R.color.red))
    }
}