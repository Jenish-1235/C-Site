package com.csite.app.FirebaseOperations

import com.csite.app.Objects.CommonTransaction
import com.csite.app.Objects.Party
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseOperationsForExternalPartyTab {
    constructor()

    fun fetchAllTransactionsForSpecificParty(partyId:String,callback: partyWiseTransactionFetched){
        val transactions = mutableListOf<CommonTransaction>()
        val dbRef = FirebaseDatabase.getInstance().getReference("Projects")
        // iterate through all the transactions of all the projects and check wherever partyid matches and add those transactions to common transaction..
        // first get the exact name of party from party library

        val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        firebaseOperationsForLibrary.fetchPartyFromPartyLibrary(object :FirebaseOperationsForLibrary.onPartyListReceived {
            override fun onPartyListReceived(partyList: ArrayList<Party>) {
                for (party in partyList) {
                    if (party.partyId.equals(partyId)) {
                        val partyName = party.partyName
                        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (projectSnapshot in snapshot.children) {
                                    val projectId = projectSnapshot.key
                                    val transactionRef =
                                        projectSnapshot.child("Transactions")
                                    for(transactionSnapshot in transactionRef.children){
                                        for(transactionIdSnapshot in transactionSnapshot.children) {
                                            val transactionData =
                                                transactionIdSnapshot.getValue(CommonTransaction::class.java)
                                            if (transactionData?.transactionParty.equals(partyName)) {
                                                transactions.add(transactionData!!)
                                            }
                                        }
                                    }
                                }
                                callback.partyWiseTransactionFetched(transactions)
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
                    }
                }
            }
        })


    }

    interface partyWiseTransactionFetched{
        fun partyWiseTransactionFetched(transactions:MutableList<CommonTransaction>)
    }
}