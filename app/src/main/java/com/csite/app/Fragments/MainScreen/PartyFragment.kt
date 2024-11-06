package com.csite.app.Fragments.MainScreen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Activites.Library.AddNewPartyActivity
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactions
import com.csite.app.Objects.CommonTransaction
import com.csite.app.Objects.Party
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.PaymentsListAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PartyFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_party, container, false)
        // Inflate the layout for this fragment
        val externalPartiesTabRecyclerView = view.findViewById<RecyclerView>(R.id.externalPartiesTabRecyclerView)
        externalPartiesTabRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val addPartyButton = view.findViewById<View>(R.id.addPartyButton)
        addPartyButton.setOnClickListener{
            val addNewPartyActivity = Intent(requireActivity(), AddNewPartyActivity::class.java)
            startActivity(addNewPartyActivity)
        }

        val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        val projectReference = FirebaseDatabase.getInstance().getReference("Projects")
        val firebaseOperationsForProjectInternalTransactions = FirebaseOperationsForProjectInternalTransactions()

        var paymentsHashMap = HashMap<String, Double>()
        firebaseOperationsForLibrary.fetchPartyFromPartyLibrary(object : FirebaseOperationsForLibrary.onPartyListReceived{
            override fun onPartyListReceived(partyList: ArrayList<Party>) {
                projectReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            for(projectSnapshot in snapshot.children){
                                if (projectSnapshot.hasChild("Transactions")){
                                    val projectId = projectSnapshot.child("projectId").value.toString()
                                    firebaseOperationsForProjectInternalTransactions.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactions.OnTransactionsFetched{
                                        override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                            for(party in partyList){
                                                var amount = 0.0
                                                for (transaction in transactions) {
                                                    if (transaction.transactionParty == party.partyName) {
                                                        if (transaction.transactionType == "Payment In"){
                                                            amount += transaction.transactionAmount.toDouble()
                                                        }else if (transaction.transactionType == "Payment Out"){
                                                            amount -= transaction.transactionAmount.toDouble()
                                                        }else if (transaction.transactionType == "Other Expense"){
                                                            amount -= transaction.transactionAmount.toDouble()
                                                        }else if (transaction.transactionType == "Material Purchase"){
                                                            amount -= transaction.transactionAmount.toDouble()
                                                        }else if (transaction.transactionType == "Sales Invoice"){
                                                            amount += transaction.transactionAmount.toDouble()
                                                        }
                                                    }
                                                }
                                                paymentsHashMap[party.partyName] = amount
                                                var adapter = PaymentsListAdapter(paymentsHashMap)
                                                externalPartiesTabRecyclerView.adapter = adapter
                                                externalPartiesTabRecyclerView.setHasFixedSize(true)
                                                externalPartiesTabRecyclerView.adapter?.notifyDataSetChanged()

                                                var totalToPay = 0.0
                                                var totalToReceive = 0.0
                                                for(partyEntry in paymentsHashMap){
                                                    if (partyEntry.value > 0){
                                                        totalToPay += partyEntry.value
                                                    }else if (partyEntry.value < 0){
                                                        totalToReceive += partyEntry.value
                                                    }
                                                }
                                                val totalToPayTextView = view.findViewById<TextView>(R.id.totalToPayView)
                                                val totalToReceiveTextView = view.findViewById<TextView>(R.id.totalToRecieveView)
                                                totalToPayTextView.text = "Total To Pay: ₹$totalToPay "
                                                totalToReceiveTextView.text = "Total To Receive: ₹${totalToReceive.toString().replace("-", "")} "

                                            }
                                        }

                                    }, object : FirebaseOperationsForProjectInternalTransactions.OnCalculated{
                                        override fun onCalculated(calculations: ArrayList<String>) {
                                        }

                                    })
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
            }
        })


        return view
    }


}