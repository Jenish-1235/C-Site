package com.csite.app.RecyclerViewListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.R
import java.util.HashMap

class PaymentsListAdapter (paymentsHashMap: HashMap<String, Double>): RecyclerView.Adapter<PaymentsListAdapter.PaymentListViewHolder>(){
    private val paymentsHashMap: HashMap<String, Double> = paymentsHashMap
    var ToPay: Double = 0.0
    var ToReceive: Double = 0.0

    class PaymentListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val partyNameTextView: TextView = itemView.findViewById(R.id.partyNameView)
        val partyAmountTextView: TextView = itemView.findViewById(R.id.partyAmountView)
        val partyTypeView = itemView.findViewById<TextView>(R.id.partyTypeView)
        val partyRemarkView = itemView.findViewById<TextView>(R.id.partyAmountRemarkView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentsListAdapter.PaymentListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_party_library, parent, false)
        return PaymentListViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: PaymentsListAdapter.PaymentListViewHolder,
        position: Int
    ) {
        val partyName = paymentsHashMap.keys.elementAt(position)
        val partyAmount = paymentsHashMap[partyName]
        holder.partyNameTextView.text = partyName.substring(8)
        holder.partyAmountTextView.text = partyAmount.toString()
        holder.partyTypeView.text = "Payment"
        if (partyAmount.toString().contains("-")) {
            holder.partyAmountTextView.setTextColor(holder.itemView.resources.getColor(R.color.red))
            ToReceive += partyAmount.toString().toDouble()
            holder.partyRemarkView.text = "To Receive"
        } else {
            holder.partyAmountTextView.setTextColor(holder.itemView.resources.getColor(R.color.green))
            holder.partyRemarkView.text = "To Pay"
            ToPay += partyAmount.toString().toDouble()
        }
    }

    override fun getItemCount(): Int {
        return paymentsHashMap.size
    }

    fun getBalance():ArrayList<Double>{
        return arrayListOf(ToPay, ToReceive)
    }

}