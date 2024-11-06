package com.csite.app.FirebaseOperations

import com.cmpte.app.Objects.TransactionMaterialPurchase
import com.csite.app.Objects.CommonTransaction
import com.csite.app.Objects.MaterialRequestOrReceived
import com.csite.app.Objects.MaterialSelection
import com.csite.app.Objects.TransactionOtherExpense
import com.csite.app.Objects.TransactionPaymentIn
import com.csite.app.Objects.TransactionPaymentOut
import com.csite.app.Objects.TransactionSalesInvoice
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.HashMap

class FirebaseOperationsForProjectInternalTransactions {

    val projectReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Projects")

    fun randomSixDigitIdGenerator(): String {
        val random = java.util.Random()
        val id = random.nextInt(900000) + 100000
        return id.toString()
    }

    // 1. Payment In Transactions
    fun savePaymentInTransaction(projectId: String, paymentInTransaction: TransactionPaymentIn) {
        val transactionId = "tPAYIN" + randomSixDigitIdGenerator()
        paymentInTransaction.transactionId = transactionId
        projectReference.child(projectId).child("Transactions/PaymentIn").child(transactionId)
            .setValue(paymentInTransaction)
    }

    // 2. Payment Out Transactions
    fun savePaymentOutTransaction(projectId: String, paymentOutTransaction: TransactionPaymentOut) {
        val transactionId = "tPAYOUT" + randomSixDigitIdGenerator()
        paymentOutTransaction.transactionId = transactionId
        projectReference.child(projectId).child("Transactions/PaymentOut").child(transactionId)
            .setValue(paymentOutTransaction)
    }

    // 3. Other Expense Transactions
    fun saveOtherExpenseTransaction(
        projectId: String,
        otherExpenseTransaction: TransactionOtherExpense
    ) {
        val transactionId = "tEXPENSE" + randomSixDigitIdGenerator()
        otherExpenseTransaction.transactionId = transactionId
        projectReference.child(projectId).child("Transactions/OtherExpense").child(transactionId)
            .setValue(otherExpenseTransaction)
    }


    // 4. Sales Invoice Transactions
    fun saveSalesInvoiceTransaction(
        projectId: String,
        salesInvoiceTransaction: TransactionSalesInvoice
    ) {
        val transactionId = "tSALES" + randomSixDigitIdGenerator()
        salesInvoiceTransaction.transactionId = transactionId
        projectReference.child(projectId).child("Transactions/SalesInvoice").child(transactionId)
            .setValue(salesInvoiceTransaction)
    }


    // 5. Material Purchase Transaction
    fun saveMaterialPurchaseTransaction(
        projectId: String,
        materialPurchaseTransaction: TransactionMaterialPurchase
    ) {
        val transactionId = "tMP" + randomSixDigitIdGenerator()
        materialPurchaseTransaction.transactionId = transactionId
        projectReference.child(projectId).child("Transactions/MaterialPurchase")
            .child(transactionId).setValue(materialPurchaseTransaction)

        val materialPurchaseHashMap  = materialPurchaseTransaction.mpItems
        var listOfMaterialReceived = HashMap<String, MaterialSelection>()
        for(material in materialPurchaseHashMap){
            val materialRequestOrReceived = MaterialSelection()
            materialRequestOrReceived.materialId = material.value.materialId
            materialRequestOrReceived.materialQuantity = material.value.materialQuantity
            materialRequestOrReceived.materialUnit = material.value.materialUnit
            materialRequestOrReceived.materialCategory = material.value.materialCategory
            materialRequestOrReceived.materialName = material.value.materialName
            materialRequestOrReceived.materialSelected = true
            materialRequestOrReceived.materialGST = material.value.materialGST
            listOfMaterialReceived.put(materialRequestOrReceived.materialId, materialRequestOrReceived)
        }

        val firebaseOperationsForProjectInternalMaterialTab = FirebaseOperationsForProjectInternalMaterialTab()
        firebaseOperationsForProjectInternalMaterialTab.saveMaterialReceived(projectId, listOfMaterialReceived)

    }

    // 6. Fetch All Transaction
    fun fetchAllTransactions(projectId: String, callback: OnTransactionsFetched, callback2: OnCalculated) {
        var transactions = mutableListOf<CommonTransaction>()
        var projectBalance = "0"
        var projectTotalIn = "0"
        var projectTotalOut = "0"
        var projectTotalExpense = "0"

        projectReference.child(projectId).child("Transactions")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    transactions.clear()
                    for(transactionSnapshot in snapshot.children){
                        for (transaction in transactionSnapshot.children){
                            val commonTransaction = transaction.getValue(CommonTransaction::class.java)
                            transactions.add(commonTransaction!!)
                        }
                    }
                    transactions.sortBy { it.transactionDate }
                    for(transaction in transactions){
                        when(transaction.transactionType){
                            "Payment In" -> {
                                projectBalance = (projectBalance.toDouble() + transaction.transactionAmount.toDouble()).toString()
                                projectTotalIn = (projectTotalIn.toDouble() + transaction.transactionAmount.toDouble()).toString()
                            }
                            "Payment Out" -> {
                                projectBalance = (projectBalance.toDouble() - transaction.transactionAmount.toDouble()).toString()
                                projectTotalOut = (projectTotalOut.toDouble() + transaction.transactionAmount.toDouble()).toString()
                            }
                            "Other Expense" -> {
                                projectBalance = (projectBalance.toDouble() - transaction.transactionAmount.toDouble()).toString()
                                projectTotalExpense = (projectTotalExpense.toDouble() + transaction.transactionAmount.toDouble()).toString()
                            }
                            "Sales Invoice" -> {
                                projectBalance = (projectBalance.toDouble() + transaction.transactionAmount.toDouble()).toString()
                            }

                            }
                    }

                    callback.onTransactionsFetched(transactions)
                    callback2.onCalculated(arrayListOf(projectBalance,projectTotalIn,projectTotalOut,projectTotalExpense))
                }

                override fun onCancelled(error: DatabaseError) {
                }


            })
    }

    interface OnTransactionsFetched{
        fun onTransactionsFetched(transactions: MutableList<CommonTransaction>)
    }

    interface OnCalculated{
        fun onCalculated(calculations:ArrayList<String>)
    }


    fun fetchTransactionsByType(projectId: String, callback: allTransactionFetch) {
        var paymentInTransaction = mutableListOf<TransactionPaymentIn>()
        var paymentOutTransaction = mutableListOf<TransactionPaymentOut>()
        var otherExpenseTransaction = mutableListOf<TransactionOtherExpense>()
        var salesInvoiceTransaction = mutableListOf<TransactionSalesInvoice>()
        var materialPurchaseTransaction = mutableListOf<TransactionMaterialPurchase>()
        projectReference.child(projectId).child("Transactions").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                paymentInTransaction.clear()
                paymentOutTransaction.clear()
                otherExpenseTransaction.clear()
                salesInvoiceTransaction.clear()
                materialPurchaseTransaction.clear()
                for (transactionSnapshot in snapshot.children) {
                    for (transaction in transactionSnapshot.children) {
                        when (transactionSnapshot.key) {
                            "PaymentIn" -> {
                                val paymentIn =
                                    transaction.getValue(TransactionPaymentIn::class.java)
                                paymentInTransaction.add(paymentIn!!)
                            }

                            "PaymentOut" -> {
                                val paymentOut =
                                    transaction.getValue(TransactionPaymentOut::class.java)
                                paymentOutTransaction.add(paymentOut!!)
                            }

                            "OtherExpense" -> {
                                val otherExpense =
                                    transaction.getValue(TransactionOtherExpense::class.java)
                                otherExpenseTransaction.add(otherExpense!!)
                            }

                            "SalesInvoice" -> {
                                val salesInvoice =
                                    transaction.getValue(TransactionSalesInvoice::class.java)
                                salesInvoiceTransaction.add(salesInvoice!!)
                            }

                            "MaterialPurchase" -> {
                                val materialPurchase =
                                    transaction.getValue(TransactionMaterialPurchase::class.java)
                                materialPurchaseTransaction.add(materialPurchase!!)
                            }
                        }
                    }
                    callback.onAllTransactionsFetched(paymentInTransaction,paymentOutTransaction,otherExpenseTransaction,salesInvoiceTransaction,materialPurchaseTransaction)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
    interface allTransactionFetch{
        fun onAllTransactionsFetched(transactions: MutableList<TransactionPaymentIn>, paymentOutTransaction: MutableList<TransactionPaymentOut>,otherExpenseTransaction: MutableList<TransactionOtherExpense>, salesInvoiceTransaction: MutableList<TransactionSalesInvoice>, materialPurchaseTransaction: MutableList<TransactionMaterialPurchase>)

    }
}