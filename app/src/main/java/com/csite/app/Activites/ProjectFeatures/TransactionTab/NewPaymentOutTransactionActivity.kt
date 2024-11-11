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
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactionsTab
import com.csite.app.Objects.Party
import com.csite.app.Objects.TransactionPaymentOut
import com.csite.app.R
import com.csite.app.databinding.ActivityNewPaymentOutTransactionBinding

class NewPaymentOutTransactionActivity : AppCompatActivity(), PartySelectionLibraryDialogFragment.OnPartySelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPaymentOutTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val projectId = intent.getStringExtra("projectId")

        binding.cashRadioButton.setOnClickListener {
            binding.bankRadioButton.isChecked = false
            binding.chequeRadioButton.isChecked = false
        }

        binding.bankRadioButton.setOnClickListener {
            binding.cashRadioButton.isChecked = false
            binding.chequeRadioButton.isChecked = false
        }

        binding.chequeRadioButton.setOnClickListener {
            binding.cashRadioButton.isChecked = false
            binding.bankRadioButton.isChecked = false
        }

        binding.paymentOutTransactionDateInput.setOnClickListener {
            showDatePickerDialog(binding)
        }

        binding.paymentOutTransactionPaymentToInput.setOnClickListener {
            val partySelectionLibraryDialogFragment = PartySelectionLibraryDialogFragment()
            partySelectionLibraryDialogFragment.show(
                supportFragmentManager,
                "partySelectionLibraryDialogFragment"
            )
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
        binding.paymentOutTransactionCategoryInput.setAdapter<ArrayAdapter<String>>(
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_dropdown_item_1line,
                materialCategoryList
            )
        )



        binding.savePaymentOutTransactionButton.setOnClickListener {
            val paymentOutTransactionDate = binding.paymentOutTransactionDateInput.text.toString()
            val paymentOutTransactionAmount = binding.paymentOutTransactionAmountInput.text.toString()
            val paymentOutTransactionDescription =
                binding.paymentOutTransactionDescriptionInput.text.toString()
            val paymentOutTransactionPaymentFrom =
                binding.paymentOutTransactionPaymentToInput.text.toString()
            val paymentOutTransactionCostCode =
                binding.paymentOutTransactionCostCodeInput.text.toString()
            val paymentOutTransactionCategory =
                binding.paymentOutTransactionCategoryInput.text.toString()
            var paymentOutTransactionPaymentMode = ""
            if (binding.cashRadioButton.isChecked) {
                paymentOutTransactionPaymentMode = "cash"
            } else if (binding.bankRadioButton.isChecked) {
                paymentOutTransactionPaymentMode = "bank"
            } else if (binding.chequeRadioButton.isChecked) {
                paymentOutTransactionPaymentMode = "cheque"
            }

            if (paymentOutTransactionDate.isEmpty() || paymentOutTransactionAmount.isEmpty() || paymentOutTransactionDescription.isEmpty() || paymentOutTransactionPaymentFrom.isEmpty() || paymentOutTransactionCostCode.isEmpty() || paymentOutTransactionCategory.isEmpty() || paymentOutTransactionPaymentMode.equals(
                    ""
                )
            ) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val firebaseOperationsForProjectInternalTransactionsTab =
                    FirebaseOperationsForProjectInternalTransactionsTab()
                if (projectId != null) {
                    firebaseOperationsForProjectInternalTransactionsTab.savePaymentOutTransaction(
                        projectId,
                        TransactionPaymentOut(
                            paymentOutTransactionDate,
                            paymentOutTransactionPaymentFrom,
                            paymentOutTransactionAmount,
                            paymentOutTransactionDescription,
                            paymentOutTransactionPaymentMode,
                            paymentOutTransactionCostCode,
                            paymentOutTransactionCategory
                        )
                    )

                    // TODO: re look into this calculations update party amount to pay or to receive

                    var partyOpeningBalanceDetails = selectedParty?.partyOpeningBalanceDetails
                    if (partyOpeningBalanceDetails != null) {
                        if (partyOpeningBalanceDetails.equals("Will Pay")) {
                            var partyAmountToPay = -1 * selectedParty?.partyAmountToPayOrReceive?.toDouble()!!
                            partyAmountToPay -= paymentOutTransactionAmount.toDouble()
                            selectedParty?.partyAmountToPayOrReceive = partyAmountToPay.toString()
                            selectedParty?.updateData(selectedParty?.partyId.toString(), selectedParty!!)
                        }else if (partyOpeningBalanceDetails.equals("Will Receive")){
                            var partyAmountToReceive = selectedParty?.partyAmountToPayOrReceive?.toDouble()!!
                            partyAmountToReceive -= paymentOutTransactionAmount.toDouble()
                            if (partyAmountToReceive < 0) {
                                selectedParty?.partyAmountToPayOrReceive = partyAmountToReceive.toString()
                                selectedParty?.partyOpeningBalanceDetails = "Will Pay"
                            }else{
                                selectedParty?.partyOpeningBalanceDetails = "Will Receive"
                                selectedParty?.partyAmountToPayOrReceive = partyAmountToReceive.toString()
                            }

                        }else if (partyOpeningBalanceDetails.equals("Fresh")){
                            selectedParty?.partyAmountToPayOrReceive = paymentOutTransactionAmount.toString()
                            selectedParty?.partyOpeningBalanceDetails = "Will Pay"
                            selectedParty?.partyCondition?.replaceFirst('0', '1', false)

                        }
                    }

                    val partyId = selectedParty?.partyId.toString()
                    selectedParty?.updateData(partyId, selectedParty!!)
                    finish()


                } else {
                    Toast.makeText(this, "Project ID is null", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }

    fun backButton(view: View) {
        finish()
    }

    fun galleryButton(view: View) {
    }

    fun cameraButton(view: View) {
    }

    fun showDatePickerDialog(binding: ActivityNewPaymentOutTransactionBinding) {
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog: DatePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.paymentOutTransactionDateInput.setText("$dayOfMonth/${month + 1}/$year")
            },
            year,
            month,
            day
        )

        datePickerDialog.show()

    }

    var selectedParty: Party? = null

    override fun onPartySelected(party: Party?) {
        selectedParty = party
        val paymentOutTransactionPaymentFromInput =
            findViewById<EditText>(R.id.paymentOutTransactionPaymentToInput)
        paymentOutTransactionPaymentFromInput.setText(party?.partyName)
    }
}