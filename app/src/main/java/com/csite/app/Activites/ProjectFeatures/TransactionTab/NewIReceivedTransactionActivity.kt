package com.csite.app.Activites.ProjectFeatures.TransactionTab

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactionsTab
import com.csite.app.Objects.Party
import com.csite.app.Objects.TransactionIPaid
import com.csite.app.Objects.TransactionIReceived
import com.csite.app.R
import com.csite.app.databinding.ActivityNewIpaidTransactionBinding
import com.csite.app.databinding.ActivityNewIreceivedTransactionBinding

class NewIReceivedTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityNewIreceivedTransactionBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val projectId = intent.getStringExtra("projectId")

        b.iReceivedDateInput.setOnClickListener {
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

        b.iReceivedCatergoryInput.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                materialCategoryList
            )
        )

        val mobileNumberPreference = getSharedPreferences("mobileNumber", MODE_PRIVATE)
        val mobileNumber = mobileNumberPreference.getString("mobileNumber", "")

        b.saveButton.setOnClickListener {
            val transactionDate = b.iReceivedDateInput.text
            val amount = b.iReceivedAmountInput.text
            val description = b.iReceivedNotesInput.text
            val costCode = b.iReceivedCostCodeInput.text
            val category = b.iReceivedCatergoryInput.text

            if (transactionDate!!.isNotEmpty() && amount!!.isNotEmpty() && description!!.isNotEmpty() && costCode!!.isNotEmpty() && category.isNotEmpty()) {
                val iReceivedTransaction = TransactionIReceived()
                iReceivedTransaction.transactionDate = transactionDate.toString()
                iReceivedTransaction.transactionAmount = amount.toString()
                iReceivedTransaction.transactionDescription = description.toString()
                iReceivedTransaction.transactionCostCode = costCode.toString()
                iReceivedTransaction.transactionCategory = category.toString()
                iReceivedTransaction.transactionParty = ""
                iReceivedTransaction.transactionType = "I Received"

                val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
                firebaseOperationsForLibrary.fetchPartyFromPartyLibrary(object :
                    FirebaseOperationsForLibrary.onPartyListReceived {
                    override fun onPartyListReceived(partyList: ArrayList<Party>) {
                        for (party in partyList) {
                            if (party.partyMobileNumber == mobileNumber) {
                                iReceivedTransaction.transactionParty = party.partyName
                                val firebaseOperationsForProjectInternalTransactionsTab =
                                    FirebaseOperationsForProjectInternalTransactionsTab()
                                firebaseOperationsForProjectInternalTransactionsTab.saveIReceivedTransaction(
                                    projectId!!,
                                    iReceivedTransaction
                                )

                                if (party.partyOpeningBalanceDetails.equals("Fresh")){
                                    party.partyAmountToPayOrReceive = (party.partyAmountToPayOrReceive!!.toDouble() - amount.toString().toDouble()).toString()
                                    if (party.partyAmountToPayOrReceive!!.toDouble() < 0){
                                        party.partyOpeningBalanceDetails = "Will Pay"
                                    }
                                }else if (party.partyOpeningBalanceDetails.equals("Will Pay")){
                                    party.partyAmountToPayOrReceive = (party.partyAmountToPayOrReceive!!.toDouble() - amount.toString().toDouble()).toString()
                                    if (party.partyAmountToPayOrReceive!!.toDouble() < 0){
                                        party.partyOpeningBalanceDetails = "Will Pay"
                                    }else if (party.partyAmountToPayOrReceive!!.toDouble() == 0.0){
                                        party.partyOpeningBalanceDetails = "Fresh"
                                    }else if (party.partyAmountToPayOrReceive!!.toDouble() > 0){
                                        party.partyOpeningBalanceDetails = "Will Recieve"
                                    }

                                }else if (party.partyOpeningBalanceDetails.equals("Will Receive")){
                                    party.partyAmountToPayOrReceive = (party.partyAmountToPayOrReceive!!.toDouble() - amount.toString().toDouble()).toString()
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

    fun showDatePickerDialog(binding: ActivityNewIreceivedTransactionBinding) {
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog: DatePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.iReceivedDateInput.setText("$dayOfMonth/${month + 1}/$year")
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}