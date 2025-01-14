package com.csite.app.Activites.ProjectFeatures.TransactionTab.TransactionDetails

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.FirebaseOperations.FirebaseOperationsForExternalPartyTab
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactionsTab
import com.csite.app.Objects.TransactionIPaid
import com.csite.app.Objects.TransactionIReceived
import com.csite.app.R
import com.csite.app.databinding.ActivityIpaidOrIreceivedTransactionDetailsBinding

class IPaidOrIReceivedTransactionDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityIpaidOrIreceivedTransactionDetailsBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val projectId = intent.getStringExtra("projectId")
        val transactionId = intent.getStringExtra("transactionId")
        val transactionType = intent.getStringExtra("transactionType")

        if (projectId != "") {
            if (projectId != null) {
                if (transactionId != null) {
                    if (transactionType != null) {
                        if (transactionType == "I Paid") {
                            b.transactionName.text = transactionType
                            val firebaseOperationsForProjectInternalTransactionsTab =
                                FirebaseOperationsForProjectInternalTransactionsTab()
                            firebaseOperationsForProjectInternalTransactionsTab.fetchIPaidTransaction(
                                projectId,
                                transactionId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnIPaidTransactionFetched {
                                    override fun onTransactionFetched(ipaid: TransactionIPaid) {
                                        b.txnDateView.text = "Date: " + ipaid.transactionDate
                                        b.txnPartyNameView.text = "Party: " + ipaid.transactionParty
                                        b.txnAmountView.text =
                                            "Amount: \u20b9" + ipaid.transactionAmount
                                        b.txnDescriptionView.text =
                                            "Description: " + ipaid.transactionDescription
                                        b.txnCostCodeView.text =
                                            "Cost Code: " + ipaid.transactionCostCode
                                        b.txnCategoryView.text =
                                            "Category: " + ipaid.transactionCategory
                                    }

                                })
                        } else if (transactionType == "I Received") {
                            b.transactionName.text = transactionType
                            val firebaseOperationsForProjectInternalTransactionsTab =
                                FirebaseOperationsForProjectInternalTransactionsTab()
                            firebaseOperationsForProjectInternalTransactionsTab.fetchIReceivedTransaction(
                                projectId,
                                transactionId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnIReceivedTransactionFetched {
                                    override fun onTransactionFetched(ireceived: TransactionIReceived) {
                                        b.txnDateView.text = "Date: " + ireceived.transactionDate
                                        b.txnPartyNameView.text =
                                            "Party: " + ireceived.transactionParty
                                        b.txnAmountView.text =
                                            "Amount: \u20b9" + ireceived.transactionAmount
                                        b.txnDescriptionView.text =
                                            "Description: " + ireceived.transactionDescription
                                        b.txnCostCodeView.text =
                                            "Cost Code: " + ireceived.transactionCostCode
                                        b.txnCategoryView.text =
                                            "Category: " + ireceived.transactionCategory
                                    }

                                })
                        }
                    }
                }
            }
        }else{
            if (transactionType != null) {
                if (transactionType == "I Paid") {
                    val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                    firebaseOperationsForExternalPartyTab.fetchIPaidTransaction(
                        transactionId!!,
                        object : FirebaseOperationsForProjectInternalTransactionsTab.OnIPaidTransactionFetched {
                            override fun onTransactionFetched(ipaid: TransactionIPaid) {
                                b.txnDateView.text = "Date: " + ipaid.transactionDate
                                b.txnPartyNameView.text = "Party: " + ipaid.transactionParty
                                b.txnAmountView.text =
                                    "Amount: \u20b9" + ipaid.transactionAmount
                                b.txnDescriptionView.text =
                                    "Description: " + ipaid.transactionDescription
                                b.txnCostCodeView.text =
                                    "Cost Code: " + ipaid.transactionCostCode
                                b.txnCategoryView.text =
                                    "Category: " + ipaid.transactionCategory
                            }
                        })
                }else if (transactionType == "I Received") {
                    val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                    firebaseOperationsForExternalPartyTab.fetchIReceivedTransaction(
                        transactionId!!,
                        object : FirebaseOperationsForProjectInternalTransactionsTab.OnIReceivedTransactionFetched {
                            override fun onTransactionFetched(ireceived: TransactionIReceived) {
                                b.txnDateView.text = "Date: " + ireceived.transactionDate
                                b.txnPartyNameView.text =
                                    "Party: " + ireceived.transactionParty
                                b.txnAmountView.text =
                                    "Amount: \u20b9" + ireceived.transactionAmount
                                b.txnDescriptionView.text =
                                    "Description: " + ireceived.transactionDescription
                                b.txnCostCodeView.text =
                                    "Cost Code: " + ireceived.transactionCostCode
                                b.txnCategoryView.text =
                                    "Category: " + ireceived.transactionCategory
                            }
                        })
                }
            }
        }
    }

    fun backButton(view:View){
        finish()
    }
}