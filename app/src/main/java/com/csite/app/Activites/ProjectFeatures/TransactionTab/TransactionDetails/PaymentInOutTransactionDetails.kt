package com.csite.app.Activites.ProjectFeatures.TransactionTab.TransactionDetails

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactionsTab
import com.csite.app.Objects.TransactionPaymentIn
import com.csite.app.Objects.TransactionPaymentOut
import com.csite.app.R
import com.csite.app.databinding.ActivityPaymentInOutTransactionDetailsBinding

class PaymentInOutTransactionDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityPaymentInOutTransactionDetailsBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val transactionType = intent.getStringExtra("transactionType")
        val transactionId = intent.getStringExtra("transactionId")
        val projectId = intent.getStringExtra("projectId")
        val firebaseOperationsForProjectInternalTransactionsTab = FirebaseOperationsForProjectInternalTransactionsTab()
        if (transactionType == "Payment In"){
            if (transactionId != null) {
                if (projectId != null) {
                    firebaseOperationsForProjectInternalTransactionsTab.fetchPaymentInTransaction(projectId, transactionId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnPaymentInTransactionFetched{
                        override fun onTransactionFetched(paymentIn: TransactionPaymentIn) {

                            b.transactionName.text = "Payment In"
                            b.txnDateView.text = "Date: " + paymentIn.transactionDate
                            b.txnPartyNameView.text = "Party Name: " + paymentIn.transactionParty
                            b.txnAmountView.text = "Amount: \u20b9" + paymentIn.transactionAmount
                            b.txnCostCodeView.text = "Cost Code: " + paymentIn.paymentInTrasactionCostCode
                            b.txnDescriptionView.text = "Description: " + paymentIn.transactionDescription
                            b.txnCategoryView.text = "Category: " + paymentIn.paymentInTransactionCategory
                            b.txnModeView.text = "Mode: " + paymentIn.paymentInTransactionPaymentMode

                        }

                    })
                }
            }
        }else if (transactionType == "Payment Out"){
            if (transactionId != null) {
                if (projectId != null) {
                    firebaseOperationsForProjectInternalTransactionsTab.fetchPaymentOutTransaction(projectId, transactionId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnPaymentOutTransactionFetched{
                      override fun onTransactionFetched(paymentOut: TransactionPaymentOut) {
                          b.transactionName.text = "Payment In"
                          b.txnDateView.text = "Date: " + paymentOut.transactionDate
                          b.txnPartyNameView.text = "Party Name: " + paymentOut.transactionParty
                          b.txnAmountView.text = "Amount: \u20b9" + paymentOut.transactionAmount
                          b.txnCostCodeView.text = "Cost Code: " + paymentOut.paymentOutTrasactionCostCode
                          b.txnDescriptionView.text = "Description: " + paymentOut.transactionDescription
                          b.txnCategoryView.text = "Category: " + paymentOut.paymentOutTransactionCategory
                          b.txnModeView.text = "Mode: " + paymentOut.paymentOutTransactionPaymentMode
                        }
                    })
                }
            }
        }

    }

    fun backButton(view: View){
        finish()
    }
}