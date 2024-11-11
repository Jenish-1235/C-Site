package com.csite.app.Activites.ProjectFeatures.TransactionTab.TransactionDetails

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactionsTab
import com.csite.app.Objects.TransactionOtherExpense
import com.csite.app.R
import com.csite.app.databinding.ActivityOtherExpenseTransactionDetailsBinding

class OtherExpenseTransactionDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityOtherExpenseTransactionDetailsBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val projectId = intent.getStringExtra("projectId")
        val transactionId = intent.getStringExtra("transactionId")

        val firebaseOperationsForProjectInternalTransactionsTab = FirebaseOperationsForProjectInternalTransactionsTab()
        if (projectId != null) {
            if (transactionId != null) {
                firebaseOperationsForProjectInternalTransactionsTab.fetchOtherExpenseTransaction(projectId, transactionId, object :FirebaseOperationsForProjectInternalTransactionsTab.OnOtherExpenseTransactionFetched{
                    override fun onTransactionFetched(otherExpense: TransactionOtherExpense) {
                        b.transactionName.text = otherExpense.transactionType
                        b.txnAmountView.text = "Amount: \u20b9 " + otherExpense.transactionAmount
                        b.txnDateView.text = "Date: " + otherExpense.transactionDate
                        b.txnPartyNameView.text = "Party: " + otherExpense.transactionParty
                        b.txnDescriptionView.text = "Description: " + otherExpense.transactionDescription
                        b.txnUnitPriceView.text = "Unit Price: \u20b9" + otherExpense.otherExpenseTransactionUnitPrice
                        b.txnQuantityView.text = "Quantity: " + otherExpense.otherExpenseTransactionQuantity + " " + otherExpense.otherExpenseTransactionUnit
                        b.txnAdditionalChargesView.text = "Additional Charges: \u20b9" + otherExpense.otherExpenseTransactionAdditionalCharges
                        b.txnDiscountView.text = "Discount: \u20b9" + otherExpense.otherExpenseTransactionDiscount
                        b.txnCategoryView.text = "Category: " + otherExpense.otherExpenseTransactionCategory
                    }

                })
            }
        }

    }
}