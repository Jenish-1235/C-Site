package com.csite.app.Activites.ProjectFeatures.TransactionTab

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactionsTab
import com.csite.app.Objects.Party
import com.csite.app.Objects.TransactionIPaid
import com.csite.app.R
import com.csite.app.databinding.ActivityNewIpaidTransactionBinding
import com.csite.app.databinding.ActivityNewMaterialPurchaseTransactionBinding

class NewIPaidTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityNewIpaidTransactionBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val projectId = intent.getStringExtra("projectId")

        b.iPaidDateInput.setOnClickListener{
            showDatePickerDialog(b)
        }

        val materialCategoryList: List<String> = mutableListOf(
            "Bills",
            "Ceiling and Roofing",
            "Civil",
            "Construction Equipments",
            "Doors and Windows",
            "Earth Work",
            "Electrical",
            "Form-work and Scaffolding",
            "HVAC",
            "Metal",
            "Others",
            "Painting",
            "Plumbing",
            "Safety Equipments",
            "Sand",
            "Steel Structure and Metal Work",
            "Transportation",
            "Wood Work"
        )

        b.iPaidCategoryInput.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                materialCategoryList
            )
        )

        val mobileNumberPreference = getSharedPreferences("mobileNumber", MODE_PRIVATE)
        val mobileNumber = mobileNumberPreference.getString("mobileNumber", "")

        b.saveIPaidButton.setOnClickListener{
            val transactionDate = b.iPaidDateInput.text
            val amount = b.iPaidAmountInput.text
            val description = b.iPaidNotesInput.text
            val costCode = b.iPaidCostCodeInput.text
            val category = b.iPaidCategoryInput.text

            if (transactionDate!!.isNotEmpty() && amount!!.isNotEmpty() && description!!.isNotEmpty() && costCode!!.isNotEmpty() && category.isNotEmpty()){
                val iPaidTransaction = TransactionIPaid()
                iPaidTransaction.transactionDate = transactionDate.toString()
                iPaidTransaction.transactionAmount = amount.toString()
                iPaidTransaction.transactionDescription = description.toString()
                iPaidTransaction.transactionCostCode = costCode.toString()
                iPaidTransaction.transactionCategory = category.toString()
                iPaidTransaction.transactionParty = ""
                iPaidTransaction.transactionType = "I Paid"

                val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
                firebaseOperationsForLibrary.fetchPartyFromPartyLibrary(object : FirebaseOperationsForLibrary.onPartyListReceived {
                    override fun onPartyListReceived(partyList: ArrayList<Party>) {
                        for (party in partyList){
                            if (party.partyMobileNumber == mobileNumber) {
                                iPaidTransaction.transactionParty = party.partyName
                                val firebaseOperationsForProjectInternalTransactionsTab = FirebaseOperationsForProjectInternalTransactionsTab()
                                firebaseOperationsForProjectInternalTransactionsTab.saveIPaidTransaction(projectId!!, iPaidTransaction)

                                if (party.partyOpeningBalanceDetails.equals("Fresh")){
                                    party.partyAmountToPayOrReceive = (party.partyAmountToPayOrReceive!!.toDouble() + amount.toString().toDouble()).toString()
                                    if (party.partyAmountToPayOrReceive!!.toDouble() > 0){
                                        party.partyOpeningBalanceDetails = "Will Receive"
                                    }
                                }else if (party.partyOpeningBalanceDetails.equals("Will Pay")){
                                    party.partyAmountToPayOrReceive = (party.partyAmountToPayOrReceive!!.toDouble() + amount.toString().toDouble()).toString()
                                    if (party.partyAmountToPayOrReceive!!.toDouble() < 0){
                                        party.partyOpeningBalanceDetails = "Will Pay"
                                    }else if (party.partyAmountToPayOrReceive!!.toDouble() == 0.0){
                                        party.partyOpeningBalanceDetails = "Fresh"
                                    }else if (party.partyAmountToPayOrReceive!!.toDouble() > 0){
                                        party.partyOpeningBalanceDetails = "Will Recieve"
                                    }

                                }else if (party.partyOpeningBalanceDetails.equals("Will Receive")){
                                    party.partyAmountToPayOrReceive = (party.partyAmountToPayOrReceive!!.toDouble() + amount.toString().toDouble()).toString()
                                    if (party.partyAmountToPayOrReceive!!.toDouble() < 0){
                                        party.partyOpeningBalanceDetails = "Will Pay"
                                    }else if (party.partyAmountToPayOrReceive!!.toDouble() == 0.0){
                                        party.partyOpeningBalanceDetails = "Fresh"
                                    }else if (party.partyAmountToPayOrReceive!!.toDouble() > 0){
                                        party.partyOpeningBalanceDetails = "Will Receive"
                                    }
                                }
                                val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
                                firebaseOperationsForLibrary.updateParty(party.partyId, party)

                                finish()
                            }
                        }
                    }

                })

            }
        }

    }

    fun showDatePickerDialog(binding: ActivityNewIpaidTransactionBinding){
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog: DatePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.iPaidDateInput.setText("$dayOfMonth/${month + 1}/$year")
        }, year, month, day)

        datePickerDialog.show()
    }

    fun backButton(view: View){
        finish()
    }
    fun galleryButton(view: View){

    }
    fun cameraButton(view: View){

    }
}