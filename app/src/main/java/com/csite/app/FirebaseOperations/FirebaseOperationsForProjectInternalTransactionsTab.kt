package com.csite.app.FirebaseOperations

import com.cmpte.app.Objects.TransactionMaterialPurchase
import com.csite.app.Objects.CommonTransaction
import com.csite.app.Objects.MaterialSelection
import com.csite.app.Objects.TransactionIPaid
import com.csite.app.Objects.TransactionIReceived
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

class FirebaseOperationsForProjectInternalTransactionsTab {

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

                    var projectBalance = "0"
                    var projectTotalIn = "0"
                    var projectTotalOut = "0"
                    var projectTotalExpense = "0"
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
                                projectTotalIn = (projectTotalIn.toDouble() + transaction.transactionAmount.toDouble()).toString()
                            }
                            "Material Purchase" ->{
                                projectBalance = (projectBalance.toDouble() - transaction.transactionAmount.toDouble()).toString()
                                projectTotalOut = (projectTotalOut.toDouble() + transaction.transactionAmount.toDouble()).toString()
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
        var iPaidTransaction = mutableListOf<TransactionIPaid>()
        var iReceivedTransaction = mutableListOf<TransactionIReceived>()
        projectReference.child(projectId).child("Transactions").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                paymentInTransaction.clear()
                paymentOutTransaction.clear()
                otherExpenseTransaction.clear()
                salesInvoiceTransaction.clear()
                materialPurchaseTransaction.clear()
                iPaidTransaction.clear()
                iReceivedTransaction.clear()
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

                            "IPaid" ->{
                                val iPaid = transaction.getValue(TransactionIPaid::class.java)
                                iPaidTransaction.add(iPaid!!)
                            }
                            "IReceived" ->{
                                val iReceived = transaction.getValue(TransactionIReceived::class.java)
                                iReceivedTransaction.add(iReceived!!)
                            }

                        }
                    }
                    callback.onAllTransactionsFetched(paymentInTransaction,paymentOutTransaction,otherExpenseTransaction,salesInvoiceTransaction,materialPurchaseTransaction, iPaidTransaction, iReceivedTransaction)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
    interface allTransactionFetch{
        fun onAllTransactionsFetched(transactions: MutableList<TransactionPaymentIn>, paymentOutTransaction: MutableList<TransactionPaymentOut>,otherExpenseTransaction: MutableList<TransactionOtherExpense>, salesInvoiceTransaction: MutableList<TransactionSalesInvoice>, materialPurchaseTransaction: MutableList<TransactionMaterialPurchase>, ipaidTransaction: MutableList<TransactionIPaid>, ireceivedTransaction: MutableList<TransactionIReceived>)
    }

    fun fetchPaymentInTransaction(projectId: String, transactionId:String, callback: OnPaymentInTransactionFetched){
        projectReference.child(projectId).child("Transactions").child("PaymentIn").child(transactionId).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val paymentIn = snapshot.getValue(TransactionPaymentIn::class.java)
                if (paymentIn != null) {
                    callback.onTransactionFetched(paymentIn)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    interface OnPaymentInTransactionFetched{
        fun onTransactionFetched(paymentIn: TransactionPaymentIn)
    }

    fun fetchPaymentOutTransaction(projectId: String, transactionId:String, callback:OnPaymentOutTransactionFetched){
        projectReference.child(projectId).child("Transactions").child("PaymentOut").child(transactionId).addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val paymentOut = snapshot.getValue(TransactionPaymentOut::class.java)
                if (paymentOut != null) {
                    callback.onTransactionFetched(paymentOut)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    interface OnPaymentOutTransactionFetched{
        fun onTransactionFetched(paymentOut: TransactionPaymentOut)
    }

    fun fetchSalesInvoiceTransaction(projectId: String, transactionId:String, callback:OnSalesInvoiceTransactionFetched){
        projectReference.child(projectId).child("Transactions").child("SalesInvoice").child(transactionId).addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val salesInvoice = snapshot.getValue(TransactionSalesInvoice::class.java)
                if (salesInvoice != null) {
                    callback.onTransactionFetched(salesInvoice)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    interface OnSalesInvoiceTransactionFetched{
        fun onTransactionFetched(salesInvoice: TransactionSalesInvoice)
    }

    fun fetchMaterialPurchaseTransaction(projectId: String, transactionId:String, callback:OnMaterialPurchaseTransactionFetched){
        projectReference.child(projectId).child("Transactions").child("MaterialPurchase").child(transactionId).addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val materialPurchase = snapshot.getValue(TransactionMaterialPurchase::class.java)
                if (materialPurchase != null) {
                    callback.onTransactionFetched(materialPurchase)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    interface OnMaterialPurchaseTransactionFetched{
        fun onTransactionFetched(materialPurchase: TransactionMaterialPurchase)
    }

    fun fetchOtherExpenseTransaction(projectId: String, transactionId:String, callback:OnOtherExpenseTransactionFetched){
        projectReference.child(projectId).child("Transactions").child("OtherExpense").child(transactionId).addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val otherExpense = snapshot.getValue(TransactionOtherExpense::class.java)
                if (otherExpense != null) {
                    callback.onTransactionFetched(otherExpense)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    interface OnOtherExpenseTransactionFetched{
        fun onTransactionFetched(otherExpense: TransactionOtherExpense)
    }

    fun saveIPaidTransaction(projectId: String, ipaidTransaction: TransactionIPaid){
        val transactionId = "tIPAID" + randomSixDigitIdGenerator()
        ipaidTransaction.transactionId = transactionId
        projectReference.child(projectId).child("Transactions/IPaid").child(transactionId).setValue(ipaidTransaction)
    }

    fun saveIReceivedTransaction(projectId: String, ireceivedTransaction:TransactionIReceived){
        val transactionId = "tIRECEIVED" + randomSixDigitIdGenerator()
        ireceivedTransaction.transactionId = transactionId
        projectReference.child(projectId).child("Transactions/IReceived").child(transactionId).setValue(ireceivedTransaction)
    }

    fun fetchIPaidTransaction(projectId: String, transactionId:String, callback: OnIPaidTransactionFetched){
        projectReference.child(projectId).child("Transactions").child("IPaid").child(transactionId).addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ipaid = snapshot.getValue(TransactionIPaid::class.java)
                if (ipaid != null) {
                    callback.onTransactionFetched(ipaid)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    interface OnIPaidTransactionFetched {
        fun onTransactionFetched(ipaid: TransactionIPaid)
    }

    fun fetchIReceivedTransaction(projectId: String, transactionId:String, callback: OnIReceivedTransactionFetched){
        projectReference.child(projectId).child("Transactions").child("IReceived").child(transactionId).addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ireceived = snapshot.getValue(TransactionIReceived::class.java)
                if (ireceived != null) {
                    callback.onTransactionFetched(ireceived)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    interface OnIReceivedTransactionFetched{
        fun onTransactionFetched(ireceived: TransactionIReceived)
    }
}