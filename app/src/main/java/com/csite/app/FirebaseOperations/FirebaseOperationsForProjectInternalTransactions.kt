package com.csite.app.FirebaseOperations

import com.csite.app.Objects.TransactionOtherExpense
import com.csite.app.Objects.TransactionPaymentIn
import com.csite.app.Objects.TransactionPaymentOut
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Transaction

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


    // 2. Payment Out Transactions
    fun savePaymentOutTransaction(projectId: String , paymentOutTransaction: TransactionPaymentOut){
        val transactionId = "tPAYOUT" + randomSixDigitIdGenerator()
        paymentOutTransaction.paymentOutTransactionId = transactionId
        projectReference.child(projectId).child("Transactions/PaymentOut").child(transactionId).setValue(paymentOutTransaction)
    }

    // Other Expense Transactions
    fun saveOtherExpenseTransaction(projectId: String , otherExpenseTransaction: TransactionOtherExpense){
        val transactionId = "tEXPENSE" + randomSixDigitIdGenerator()
        otherExpenseTransaction.otherExpenseTransactionId = transactionId
        projectReference.child(projectId).child("Transactions/OtherExpense").child(transactionId).setValue(otherExpenseTransaction)
    }

}