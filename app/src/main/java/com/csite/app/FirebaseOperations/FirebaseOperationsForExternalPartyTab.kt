package com.csite.app.FirebaseOperations

import android.util.Log
import com.cmpte.app.Objects.TransactionMaterialPurchase
import com.csite.app.Objects.CommonTransaction
import com.csite.app.Objects.Party
import com.csite.app.Objects.TransactionIPaid
import com.csite.app.Objects.TransactionIReceived
import com.csite.app.Objects.TransactionOtherExpense
import com.csite.app.Objects.TransactionPaymentIn
import com.csite.app.Objects.TransactionPaymentOut
import com.csite.app.Objects.TransactionSalesInvoice
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

    fun fetchMaterialPurchaseTransaction(transactionId:String, transactionType:String, callback: OnMaterialPurchaseTransactionFetched) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Projects")
        // iterate in all project/Transactions/MaterialPurchase and find the transaction matching transaction Id and return it with callback
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (projectSnapshot in snapshot.children) {
                    val transactionRef = projectSnapshot.child("Transactions").child("MaterialPurchase")
                    for(transactionSnapshot in transactionRef.children){
                        Log.i("transaction", transactionSnapshot.value.toString())
                        if(transactionSnapshot.key.equals(transactionId)){
                            val materialPurchase = transactionSnapshot.getValue(TransactionMaterialPurchase::class.java)
                            if (materialPurchase != null) {
                                callback.onTransactionFetched(materialPurchase)
                            }
                        }else{
                            continue
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    interface OnMaterialPurchaseTransactionFetched{
        fun onTransactionFetched(materialPurchase: TransactionMaterialPurchase)
    }

    fun fetchSalesInvoiceTransaction(transactionId:String, transactionType:String, callback: FirebaseOperationsForProjectInternalTransactionsTab.OnSalesInvoiceTransactionFetched) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Projects")
        // iterate in all project/Transactions/MaterialPurchase and find the transaction matching transaction Id and return it with callback
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (projectSnapshot in snapshot.children) {
                    val transactionRef = projectSnapshot.child("Transactions").child("SalesInvoice")
                    for (transactionSnapshot in transactionRef.children) {
                        Log.i("transaction", transactionSnapshot.value.toString())
                        if (transactionSnapshot.key.equals(transactionId)) {
                            val salesInvoice =
                                transactionSnapshot.getValue(TransactionSalesInvoice::class.java)
                            if (salesInvoice != null) {
                                callback.onTransactionFetched(salesInvoice)
                            }
                        } else {
                            continue
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun fetchOtherExpenseTransaction(transactionId:String, callback: FirebaseOperationsForExternalPartyTab.OnOtherExpenseTransactionFetched) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Projects")
        // iterate in all project/Transactions/MaterialPurchase and find the transaction matching transaction Id and return it with callback
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (projectSnapshot in snapshot.children) {
                    val transactionRef = projectSnapshot.child("Transactions").child("OtherExpense")
                    for (transactionSnapshot in transactionRef.children) {
                        Log.i("transaction", transactionSnapshot.value.toString())
                        if (transactionSnapshot.key.equals(transactionId)) {
                            val otherExpense =
                                transactionSnapshot.getValue(TransactionOtherExpense::class.java)
                            if (otherExpense != null) {
                                callback.onTransactionFetched(otherExpense)
                            }
                        } else {
                            continue
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    interface OnOtherExpenseTransactionFetched{
        fun onTransactionFetched(otherExpense: TransactionOtherExpense)
    }

    // let's do same for PaymentIn , PaymentOut, IPaid and IReceived Transaction types:

    fun fetchPaymentInTransaction(transactionId:String, callback: OnPaymentInTransactionFetched) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Projects")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (projectSnapshot in snapshot.children) {
                    val transactionRef = projectSnapshot.child("Transactions").child("PaymentIn")
                    for (transactionSnapshot in transactionRef.children) {
                        Log.i("transaction", transactionSnapshot.value.toString())
                        if (transactionSnapshot.key.equals(transactionId)) {
                            val paymentIn =
                                transactionSnapshot.getValue(TransactionPaymentIn::class.java)
                            if (paymentIn != null) {
                                callback.onTransactionFetched(paymentIn)
                            } else {
                                continue
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    interface OnPaymentInTransactionFetched{
        fun onTransactionFetched(paymentIn: TransactionPaymentIn)
    }

    fun fetchPaymentOutTransaction(transactionId:String, callback: OnPaymentOutTransactionFetched) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Projects")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (projectSnapshot in snapshot.children) {
                    val transactionRef = projectSnapshot.child("Transactions").child("PaymentOut")
                    for (transactionSnapshot in transactionRef.children) {
                        Log.i("transaction", transactionSnapshot.value.toString())
                        if (transactionSnapshot.key.equals(transactionId)) {
                            val paymentOut =
                                transactionSnapshot.getValue(TransactionPaymentOut::class.java)
                            if (paymentOut != null) {
                                callback.onTransactionFetched(paymentOut)
                            } else {
                                continue
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    interface OnPaymentOutTransactionFetched{
        fun onTransactionFetched(paymentOut: TransactionPaymentOut)
    }

    fun fetchIPaidTransaction(transactionId:String, callback: FirebaseOperationsForProjectInternalTransactionsTab.OnIPaidTransactionFetched) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Projects")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (projectSnapshot in snapshot.children) {
                    val transactionRef = projectSnapshot.child("Transactions").child("IPaid")
                    for (transactionSnapshot in transactionRef.children) {
                        Log.i("transaction", transactionSnapshot.value.toString())
                        if (transactionSnapshot.key.equals(transactionId)) {
                            val iPaid =
                                transactionSnapshot.getValue(TransactionIPaid::class.java)
                            if (iPaid != null) {
                                callback.onTransactionFetched(iPaid)
                            } else {
                                continue
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun fetchIReceivedTransaction(transactionId:String, callback: FirebaseOperationsForProjectInternalTransactionsTab.OnIReceivedTransactionFetched) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Projects")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (projectSnapshot in snapshot.children) {
                    val transactionRef = projectSnapshot.child("Transactions").child("IReceived")
                    for (transactionSnapshot in transactionRef.children) {
                        Log.i("transaction", transactionSnapshot.value.toString())
                        if (transactionSnapshot.key.equals(transactionId)) {
                            val iReceived =
                                transactionSnapshot.getValue(TransactionIReceived::class.java)
                            if (iReceived != null) {
                                callback.onTransactionFetched(iReceived)
                            } else {
                                continue
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
