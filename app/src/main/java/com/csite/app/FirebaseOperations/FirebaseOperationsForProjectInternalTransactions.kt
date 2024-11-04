package com.csite.app.FirebaseOperations

import com.cmpte.app.Objects.TransactionMaterialPurchase
import com.csite.app.Objects.TransactionOtherExpense
import com.csite.app.Objects.TransactionPaymentIn
import com.csite.app.Objects.TransactionPaymentOut
import com.csite.app.Objects.TransactionSalesInvoice
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseOperationsForProjectInternalTransactions {

    val projectReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Projects")

    fun randomSixDigitIdGenerator(): String {
        val random = java.util.Random()
        val id = random.nextInt(900000) + 100000
        return id.toString()
    }

    // 1. Payment In Transactions
    fun savePaymentInTransaction(projectId: String , paymentInTransaction: TransactionPaymentIn) {
        val transactionId = "tPAYIN" + randomSixDigitIdGenerator()
        paymentInTransaction.paymentInTransactionId = transactionId
        projectReference.child(projectId).child("Transactions/PaymentIn").child(transactionId).setValue(paymentInTransaction)
    }

    // 1.1 Fetch Payment In Transactions
    fun fetchPaymentInTransactions(projectId: String, callback: OnPaymentInTransactionsReceived) {
        projectReference.child(projectId).child("Transactions/PaymentIn").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val paymentInTransactions = mutableListOf<TransactionPaymentIn>()
                for (transactionSnapshot in dataSnapshot.children) {
                    val paymentInTransaction =
                        transactionSnapshot.getValue(TransactionPaymentIn::class.java)
                    paymentInTransaction?.let {
                        paymentInTransactions.add(it)
                    }
                }
                callback.onPaymentInTransactionsReceived(paymentInTransactions)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    // 1.2 On PaymentInTransactionsReceived
    interface OnPaymentInTransactionsReceived {
        fun onPaymentInTransactionsReceived(paymentInTransactions: MutableList<TransactionPaymentIn>)
    }


    // 2. Payment Out Transactions
    fun savePaymentOutTransaction(projectId: String , paymentOutTransaction: TransactionPaymentOut){
        val transactionId = "tPAYOUT" + randomSixDigitIdGenerator()
        paymentOutTransaction.paymentOutTransactionId = transactionId
        projectReference.child(projectId).child("Transactions/PaymentOut").child(transactionId).setValue(paymentOutTransaction)
    }

    // 2.1
    fun fetchPaymentOutTransactions(projectId: String, callback: OnPaymentOutTransactionsReceived) {
        projectReference.child(projectId).child("Transactions/PaymentOut").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val paymentOutTransactions = mutableListOf<TransactionPaymentOut>()
                for (transactionSnapshot in dataSnapshot.children) {
                    val paymentOutTransaction =
                        transactionSnapshot.getValue(TransactionPaymentOut::class.java)
                    paymentOutTransactions.add(paymentOutTransaction!!)
                }
                callback.onPaymentOutTransactionsReceived(paymentOutTransactions)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    // 2.2 On PaymentOutTransactionsReceived
    interface OnPaymentOutTransactionsReceived {
        fun onPaymentOutTransactionsReceived(paymentOutTransactions: MutableList<TransactionPaymentOut>)
    }

    // 3. Other Expense Transactions
    fun saveOtherExpenseTransaction(projectId: String , otherExpenseTransaction: TransactionOtherExpense){
        val transactionId = "tEXPENSE" + randomSixDigitIdGenerator()
        otherExpenseTransaction.otherExpenseTransactionId = transactionId
        projectReference.child(projectId).child("Transactions/OtherExpense").child(transactionId).setValue(otherExpenseTransaction)
    }

    // 3.1 Fetch Other Expense Transactions
    fun fetchOtherExpenseTransactions(projectId: String, callback: OnOtherExpenseTransactionsReceived) {
        projectReference.child(projectId).child("Transactions/OtherExpense").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val otherExpenseTransactions = mutableListOf<TransactionOtherExpense>()
                for (transactionSnapshot in dataSnapshot.children) {
                    val otherExpenseTransaction =
                        transactionSnapshot.getValue(TransactionOtherExpense::class.java)
                    otherExpenseTransactions.add(otherExpenseTransaction!!)
                    }
                callback.onOtherExpenseTransactionsReceived(otherExpenseTransactions)
            }
            override fun onCancelled(error: DatabaseError) {
            }
            })
    }

    // 3.2 On OtherExpenseTransactionsReceived
    interface OnOtherExpenseTransactionsReceived{
        fun onOtherExpenseTransactionsReceived(otherExpenseTransactions: MutableList<TransactionOtherExpense>)
    }

    // 4. Sales Invoice Transactions
    fun saveSalesInvoiceTransaction(projectId: String , salesInvoiceTransaction: TransactionSalesInvoice){
        val transactionId = "tSALES" + randomSixDigitIdGenerator()
        salesInvoiceTransaction.siId = transactionId
        projectReference.child(projectId).child("Transactions/SalesInvoice").child(transactionId).setValue(salesInvoiceTransaction)
    }

    // 4.1 Fetch Sales Invoice Transactions
    fun fetchSalesInvoiceTransactions(projectId: String, callback: OnSalesInvoiceTransactionsReceived) {
        projectReference.child(projectId).child("Transactions/SalesInvoice").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val salesInvoiceTransactions = mutableListOf<TransactionSalesInvoice>()
                for (transactionSnapshot in dataSnapshot.children) {
                    val salesInvoiceTransaction =
                        transactionSnapshot.getValue(TransactionSalesInvoice::class.java)
                    salesInvoiceTransactions.add(salesInvoiceTransaction!!)
                    }
                callback.onSalesInvoiceTransactionsReceived(salesInvoiceTransactions)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        }

    // 4.2 On SalesInvoiceTransactionsReceived
    interface OnSalesInvoiceTransactionsReceived{
        fun onSalesInvoiceTransactionsReceived(salesInvoiceTransactions: MutableList<TransactionSalesInvoice>)
    }

    // 5. Material Purchase Transaction
    fun saveMaterialPurchaseTransaction(projectId: String , materialPurchaseTransaction: TransactionMaterialPurchase){
        val transactionId = "tMP" + randomSixDigitIdGenerator()
        materialPurchaseTransaction.mpId = transactionId
        projectReference.child(projectId).child("Transactions/MaterialPurchase").child(transactionId).setValue(materialPurchaseTransaction)
    }

    // 5.1 Fetch Material Purchase Transactions
    fun fetchMaterialPurchaseTransactions(projectId: String, callback: OnMaterialPurchaseTransactionsReceived) {
        projectReference.child(projectId).child("Transactions/MaterialPurchase").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val materialPurchaseTransactions = mutableListOf<TransactionMaterialPurchase>()
                for (transactionSnapshot in dataSnapshot.children) {
                    val materialPurchaseTransaction =
                        transactionSnapshot.getValue(TransactionMaterialPurchase::class.java)
                    materialPurchaseTransactions.add(materialPurchaseTransaction!!)
                    }
                callback.onMaterialPurchaseTransactionsReceived(materialPurchaseTransactions)
            }
            override fun onCancelled(error: DatabaseError) {

            }
    })
        }

    // 5.2 On MaterialPurchaseTransactionsReceived
    interface OnMaterialPurchaseTransactionsReceived {
        fun onMaterialPurchaseTransactionsReceived(materialPurchaseTransactions: MutableList<TransactionMaterialPurchase>)
    }


}