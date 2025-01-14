package com.csite.app.Activites.ProjectFeatures.TransactionTab.TransactionDetails

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cmpte.app.Objects.TransactionMaterialPurchase
import com.csite.app.FirebaseOperations.FirebaseOperationsForExternalPartyTab
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactionsTab
import com.csite.app.Objects.MaterialSelection
import com.csite.app.Objects.TransactionSalesInvoice
import com.csite.app.R
import com.csite.app.databinding.ActivityMaterialPurchaseTransactionDetailsBinding

class MaterialPurchaseOrSalesInvoiceTransactionDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityMaterialPurchaseTransactionDetailsBinding.inflate(layoutInflater)
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

        val firebaseOperationsForProjectInternalTransactionsTab = FirebaseOperationsForProjectInternalTransactionsTab()

        if (projectId != "" && transactionId != null && transactionType != null) {
            if (projectId != null && transactionId != null && transactionType != null) {
                if (transactionType == "Material Purchase") {
                    firebaseOperationsForProjectInternalTransactionsTab.fetchMaterialPurchaseTransaction(
                        projectId,
                        transactionId,
                        object :
                            FirebaseOperationsForProjectInternalTransactionsTab.OnMaterialPurchaseTransactionFetched {
                            override fun onTransactionFetched(materialPurchase: TransactionMaterialPurchase) {
                                b.transactionName.text = materialPurchase.transactionType
                                b.txnDateView.text = "Date: " + materialPurchase.transactionDate
                                b.txnPartyNameView.text =
                                    "Party Name: " + materialPurchase.transactionParty
                                b.txnAdditionalChargesView.text =
                                    "Additional Charges: \u20b9" + materialPurchase.mpAdditionalCharge
                                b.txnDiscountView.text =
                                    "Discount: \u20b9" + materialPurchase.mpDiscount
                                b.txnCategoryView.text = "Category: " + materialPurchase.mpCategory
                                b.txnAmountView.text =
                                    "Amount: \u20b9" + materialPurchase.transactionAmount
                                b.txnDescriptionView.text =
                                    "Description: " + materialPurchase.transactionDescription
                                val materialList = ArrayList<String>()
                                for (material in materialPurchase.mpItems.values) {
                                    val materialData =
                                        material.materialName + "   " + material.materialQuantity + material.materialUnit + " * \u20b9" + material.materialUnitRate + " = \u20b9" + (material.materialQuantity.toDouble() * material.materialUnitRate.toDouble())
                                    materialList.add(materialData)
                                }
                                val adapter = ArrayAdapter(
                                    this@MaterialPurchaseOrSalesInvoiceTransactionDetailsActivity,
                                    android.R.layout.simple_list_item_1,
                                    materialList
                                )
                                b.materialListView.adapter = adapter

                            }

                        })
                } else if (transactionType == "Sales Invoice") {
                    firebaseOperationsForProjectInternalTransactionsTab.fetchSalesInvoiceTransaction(
                        projectId,
                        transactionId,
                        object :
                            FirebaseOperationsForProjectInternalTransactionsTab.OnSalesInvoiceTransactionFetched {
                            override fun onTransactionFetched(salesInvoice: TransactionSalesInvoice) {
                                b.transactionName.text = salesInvoice.transactionType
                                b.txnDateView.text = "Date: " + salesInvoice.transactionDate
                                b.txnPartyNameView.text =
                                    "Party Name: " + salesInvoice.transactionParty
                                b.txnAdditionalChargesView.text =
                                    "Additional Charges: \u20b9" + salesInvoice.siAdditionalCharge
                                b.txnDiscountView.text =
                                    "Discount: \u20b9" + salesInvoice.siDiscount
                                b.txnCategoryView.text = "Category: " + salesInvoice.siCategory
                                b.txnAmountView.text =
                                    "Amount: \u20b9" + salesInvoice.transactionAmount
                                b.txnDescriptionView.text =
                                    "Description: " + salesInvoice.transactionDescription
                                val materialList = ArrayList<String>()
                                for (material in salesInvoice.siItems.values) {
                                    val materialData =
                                        material.materialName + "   " + material.materialQuantity + material.materialUnit + " * \u20b9" + material.materialUnitRate + " = \u20b9" + (material.materialQuantity.toDouble() * material.materialUnitRate.toDouble())
                                    materialList.add(materialData)
                                }
                                val adapter = ArrayAdapter(
                                    this@MaterialPurchaseOrSalesInvoiceTransactionDetailsActivity,
                                    android.R.layout.simple_list_item_1,
                                    materialList
                                )
                                b.materialListView.adapter = adapter
                            }
                        })
                }
            }
        }else{
            if(transactionId != null && transactionType != null){
                val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                if (transactionType == "Material Purchase") {

                }else if (transactionType == "Sales Invoice") {

                }
            }
        }

    }

    fun backButton(view: View){
        finish()
    }
}