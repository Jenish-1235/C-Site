package com.csite.app.RecyclerViewListAdapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Activites.ProjectFeatures.TransactionTab.TransactionDetails.MaterialPurchaseOrSalesInvoiceTransactionDetailsActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.TransactionDetails.OtherExpenseTransactionDetailsActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.TransactionDetails.PaymentInOutTransactionDetailsActivity
import com.csite.app.Objects.CommonTransaction
import com.csite.app.R

class TransactionListAdapter(transactionList: MutableList<CommonTransaction>, projectId:String): RecyclerView.Adapter <TransactionListAdapter.TransactionListViewHolder>() {
    private var transactionList: MutableList<CommonTransaction> = transactionList
    var projectId = projectId;
    class  TransactionListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val transactionPartyView = itemView.findViewById<TextView>(R.id.transactionPartyView)
        val transactionAmountView = itemView.findViewById<TextView>(R.id.transactionAmountView)
        val transactionDateView = itemView.findViewById<TextView>(R.id.transactionDateView)
        val transactionTypeView = itemView.findViewById<TextView>(R.id.transactionTypeView)
        val transactionDescriptionView = itemView.findViewById<TextView>(R.id.transactionDescriptionView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_transaction, parent, false)
        return TransactionListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: TransactionListViewHolder, position: Int) {
        val currentItem = transactionList[position]
        holder.transactionPartyView.text = currentItem.transactionParty
        holder.transactionAmountView.text = "\u20b9" + currentItem.transactionAmount
        holder.transactionDateView.text = currentItem.transactionDate
        holder.transactionTypeView.text = currentItem.transactionType
        holder.transactionDescriptionView.text = currentItem.transactionDescription
        if (currentItem.transactionType.equals("Payment In") || currentItem.transactionType.equals("Sales Invoice")){
            holder.transactionAmountView.setTextColor(holder.transactionAmountView.resources.getColor(R.color.green))
        } else {
            holder.transactionAmountView.setTextColor(holder.transactionAmountView.resources.getColor(R.color.red))
        }


        if (currentItem.transactionType.equals("Payment In") || currentItem.transactionType.equals("Payment Out")){
            holder.itemView.setOnClickListener{
                val intent = Intent(holder.itemView.context, PaymentInOutTransactionDetailsActivity::class.java)
                intent.putExtra("transactionType", currentItem.transactionType)
                intent.putExtra("transactionId", currentItem.transactionId)
                intent.putExtra("projectId", projectId)
                holder.itemView.context.startActivity(intent)
            }
        }else if (currentItem.transactionType.equals("Other Expense")){
            holder.itemView.setOnClickListener{
                val intent = Intent(holder.itemView.context, OtherExpenseTransactionDetailsActivity::class.java)
                intent.putExtra("transactionType", currentItem.transactionType)
                intent.putExtra("transactionId", currentItem.transactionId)
                intent.putExtra("projectId", projectId)
                holder.itemView.context.startActivity(intent)
            }
        }else if (currentItem.transactionType.equals("Material Purchase") || currentItem.transactionType.equals("Sales Invoice")){
            holder.itemView.setOnClickListener{
                val intent = Intent(holder.itemView.context, MaterialPurchaseOrSalesInvoiceTransactionDetailsActivity::class.java)
                intent.putExtra("transactionType", currentItem.transactionType)
                intent.putExtra("transactionId", currentItem.transactionId)
                intent.putExtra("projectId", projectId)
                holder.itemView.context.startActivity(intent)
            }
        }

    }
}