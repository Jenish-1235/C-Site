package com.csite.app.Activites.ProjectFeatures.TransactionTab

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.DialogFragments.PartySelectionLibraryDialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactions
import com.csite.app.Objects.Party
import com.csite.app.Objects.TransactionPaymentIn
import com.csite.app.R
import com.csite.app.databinding.ActivityNewPaymentInTransactionBinding

class NewPaymentInTransactionActivity : AppCompatActivity() , PartySelectionLibraryDialogFragment.OnPartySelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPaymentInTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val projectId = intent.getStringExtra("projectId")

        binding.cashRadioButton.setOnClickListener{
            binding.bankRadioButton.isChecked = false
            binding.chequeRadioButton.isChecked = false
        }

        binding.bankRadioButton.setOnClickListener{
            binding.cashRadioButton.isChecked = false
            binding.chequeRadioButton.isChecked = false
        }

        binding.chequeRadioButton.setOnClickListener{
            binding.cashRadioButton.isChecked = false
            binding.bankRadioButton.isChecked = false
        }

        binding.paymentInTransactionDateInput.setOnClickListener{
            showDatePickerDialog(binding)
        }

        binding.paymentInTransactionPaymentFromInput.setOnClickListener{
            val partySelectionLibraryDialogFragment = PartySelectionLibraryDialogFragment()
            partySelectionLibraryDialogFragment.show(supportFragmentManager, "partySelectionLibraryDialogFragment")
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
        binding.paymentInTransactionCategoryInput.setAdapter<ArrayAdapter<String>>(
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_dropdown_item_1line,
                materialCategoryList
            )
        )



        binding.savePaymentInTransactionButton.setOnClickListener{
            val paymentInTransactionDate = binding.paymentInTransactionDateInput.text.toString()
            val paymentInTransactionAmount = binding.paymentInTransactionAmountInput.text.toString()
            val paymentInTransactionDescription = binding.paymentInTransactionDescriptionInput.text.toString()
            val paymentInTransactionPaymentFrom = binding.paymentInTransactionPaymentFromInput.text.toString()
            val paymentInTransactionCostCode = binding.paymentInTransactionCostCodeInput.text.toString()
            val paymentInTransactionCategory = binding.paymentInTransactionCategoryInput.text.toString()
            var paymentInTransactionPaymentMode = ""
            if (binding.cashRadioButton.isChecked){
                paymentInTransactionPaymentMode = "cash"
            } else if (binding.bankRadioButton.isChecked){
                paymentInTransactionPaymentMode = "bank"
            }else if (binding.chequeRadioButton.isChecked){
                paymentInTransactionPaymentMode = "cheque"
            }

            if (paymentInTransactionDate.isEmpty() || paymentInTransactionAmount.isEmpty() || paymentInTransactionDescription.isEmpty() || paymentInTransactionPaymentFrom.isEmpty() || paymentInTransactionCostCode.isEmpty() || paymentInTransactionCategory.isEmpty() || paymentInTransactionPaymentMode.equals("")){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val firebaseOperationsForProjectInternalTransactions = FirebaseOperationsForProjectInternalTransactions()
                if (projectId != null) {
                    firebaseOperationsForProjectInternalTransactions.savePaymentInTransaction(
                        projectId,
                        TransactionPaymentIn(
                            paymentInTransactionDate,
                            paymentInTransactionPaymentFrom,
                            paymentInTransactionAmount,
                            paymentInTransactionDescription,
                            paymentInTransactionPaymentMode,
                            paymentInTransactionCostCode,
                            paymentInTransactionCategory
                        )
                    )

                    var  partyOpeningBalanceDetails = selectedParty?.partyOpeningBalanceDetails
                    if (partyOpeningBalanceDetails != null){
                        var partyAmountToPayOrReceive = selectedParty?.partyAmountToPayOrReceive?.toDouble()
                        if (partyOpeningBalanceDetails.equals("Will Receive") && partyAmountToPayOrReceive != null) {
                                partyAmountToPayOrReceive += paymentInTransactionAmount.toDouble()
                                selectedParty?.partyOpeningBalanceDetails = "Will Receive"
                            selectedParty?.partyAmountToPayOrReceive = partyAmountToPayOrReceive.toString()
                        } else if (partyOpeningBalanceDetails.equals("Will Pay")){
                            partyAmountToPayOrReceive = -1 * partyAmountToPayOrReceive!!
                            partyAmountToPayOrReceive += paymentInTransactionAmount.toDouble()
                            if (partyAmountToPayOrReceive < 0){
                                selectedParty?.partyOpeningBalanceDetails = "Will Pay"
                                selectedParty?.partyAmountToPayOrReceive = partyAmountToPayOrReceive.toString()
                            }else{
                                selectedParty?.partyOpeningBalanceDetails = "Will Receive"
                                selectedParty?.partyAmountToPayOrReceive = partyAmountToPayOrReceive.toString()
                            }
                        } else if (partyOpeningBalanceDetails.equals("Fresh")){
                            partyAmountToPayOrReceive = paymentInTransactionAmount.toDouble()
                            selectedParty?.partyAmountToPayOrReceive = partyAmountToPayOrReceive.toString()
                            selectedParty?.partyOpeningBalanceDetails = "Will Receive"
                            selectedParty?.partyCondition?.replaceFirst('0', '1', false)
                        }
                    }

                    val partyId = selectedParty?.partyId.toString()
                    selectedParty?.updateData(partyId, selectedParty!!)
                    finish()



                }else{
                    Toast.makeText(this, "Project ID is null", Toast.LENGTH_SHORT).show()
                }
            }



        }
    }

    fun backButton(view: View) {
        finish()
    }

    fun galleryButton(view: View){
    }

    fun cameraButton(view: View){
    }

    fun showDatePickerDialog(binding: ActivityNewPaymentInTransactionBinding){
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog: DatePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.paymentInTransactionDateInput.setText("$dayOfMonth/${month + 1}/$year")
        }, year, month, day)

        datePickerDialog.show()

    }

    var selectedParty: Party? = null
    override fun onPartySelected(party: Party?) {
        val paymentInTransactionPaymentFromInput = findViewById<EditText>(R.id.paymentInTransactionPaymentFromInput)
        paymentInTransactionPaymentFromInput.setText(party?.partyName)
        selectedParty = party
    }

}