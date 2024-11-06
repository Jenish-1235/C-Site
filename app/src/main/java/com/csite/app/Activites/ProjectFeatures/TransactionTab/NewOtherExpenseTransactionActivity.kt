package com.csite.app.Activites.ProjectFeatures.TransactionTab

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.DialogFragments.PartySelectionLibraryDialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactions
import com.csite.app.Objects.Party
import com.csite.app.Objects.TransactionOtherExpense
import com.csite.app.R
import com.csite.app.databinding.ActivityNewOtherExpenseTransactionBinding
import com.csite.app.databinding.ActivityNewPaymentInTransactionBinding

class NewOtherExpenseTransactionActivity : AppCompatActivity(), PartySelectionLibraryDialogFragment.OnPartySelectedListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewOtherExpenseTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val projectId = intent.getStringExtra("projectId")

        binding.addQuantityButton.setOnClickListener {
            if (binding.addQuantityButton.text == "+ Add Quantity") {
                binding.addQuantityButton.text = "- Remove Quantity"
                binding.addQuantityButton.setTextColor(getColorStateList(R.color.red))
                binding.addQuantityLinearLayout.visibility = View.VISIBLE
            } else {
                binding.addQuantityButton.text = "+ Add Quantity"
                binding.addQuantityLinearLayout.visibility = View.GONE
                binding.addQuantityButton.setTextColor(getColorStateList(R.color.black))
            }
        }

        binding.additionalChargesButton.setOnClickListener {
            if (binding.additionalChargesButton.text == "+ Additional Charges") {
                binding.additionalChargesButton.text = "- Remove Charges"
                binding.additionalChargesButton.setTextColor(getColorStateList(R.color.red))
                binding.additionalChargesInputLayout.visibility = View.VISIBLE
            }else {
                binding.additionalChargesButton.text = "+ Additional Charges"
                binding.additionalChargesInputLayout.visibility = View.GONE
                binding.additionalChargesButton.setTextColor(getColorStateList(R.color.black))
            }
        }

        binding.addDiscountButton.setOnClickListener {
            if (binding.addDiscountButton.text == "+ Add Discount") {
                binding.addDiscountButton.text = "- Remove Discount"
                binding.addDiscountButton.setTextColor(getColorStateList(R.color.red))
                binding.addDiscountInputLayout.visibility = View.VISIBLE
            }else{
                binding.addDiscountButton.text = "+ Add Discount"
                binding.addDiscountInputLayout.visibility = View.GONE
                binding.addDiscountButton.setTextColor(getColorStateList(R.color.black))
            }
        }

        binding.addCategoryButton.setOnClickListener {
            if (binding.addCategoryButton.text == "+ Add Category") {
                binding.addCategoryButton.text = "- Remove Category"
                binding.addCategoryButton.setTextColor(getColorStateList(R.color.red))
                binding.addCategoryInputLayout.visibility = View.VISIBLE
            }else{
                binding.addCategoryButton.text = "+ Add Category"
                binding.addCategoryInputLayout.visibility = View.GONE
                binding.addCategoryButton.setTextColor(getColorStateList(R.color.black))
            }
        }

        binding.addNotesButton.setOnClickListener {
            if (binding.addNotesButton.text == "+ Add Notes") {
                binding.addNotesButton.text = "- Remove Notes"
                binding.addNotesButton.setTextColor(getColorStateList(R.color.red))
                binding.addNotesInputLayout.visibility = View.VISIBLE
            }else{
                binding.addNotesButton.text = "+ Add Notes"
                binding.addNotesInputLayout.visibility = View.GONE
                binding.addNotesButton.setTextColor(getColorStateList(R.color.black))
            }
        }


        binding.otherExpenseTransactionDateInput.setOnClickListener{
            showDatePickerDialog(binding)
        }

        val materialUnitInputList: List<String> = mutableListOf(
            "nos",
            "numbers",
            "kg",
            "bags",
            "cft",
            "tonne",
            "brass",
            "litre",
            "sq. ft",
            "km",
            "metre",
            "ft",
            "cum",
            "quintal",
            "mm",
            "sq. metre",
            "kilolitre",
            "inch",
            "gram",
            "cm",
            "lb",
            "trips",
            "unit",
            "hours",
            "days",
            "bundle",
            "drum",
            "gallons",
            "pac",
            "pair",
            "pcs",
            "per day",
            "roll",
            "set",
            "sheet"
        )
        binding.otherExpenseTransactionUnitInput.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                materialUnitInputList
            )
        )

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

        binding.otherExpenseTransactionCategoryInput.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                materialCategoryList
            )
        )

        binding.otherExpenseTransactionToInput.setOnClickListener{
                val partySelectionLibraryDialogFragment = PartySelectionLibraryDialogFragment()
                partySelectionLibraryDialogFragment.show(supportFragmentManager, "partySelectionLibraryDialogFragment")
        }

        var additionalCharge = 0.0
        var quantity = 0.0
        var unitPrice = 0.0
        var discount = 0.0
        binding.otherExpenseTransactionUnitPriceInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.otherExpenseTransactionAdditionalChargesInput.getText().toString() != ""){
                    additionalCharge = binding.otherExpenseTransactionAdditionalChargesInput.getText().toString().toDouble()
                }else{
                    additionalCharge = 0.0
                }

                if(binding.otherExpenseTransactionQuantityInput.getText().toString() != ""){
                    quantity = binding.otherExpenseTransactionQuantityInput.getText().toString().toDouble()
                }else{
                    quantity = 0.0
                }

                if(binding.otherExpenseTransactionUnitPriceInput.getText().toString() != ""){
                    unitPrice = binding.otherExpenseTransactionUnitPriceInput.getText().toString().toDouble()
                }else{
                    unitPrice = 0.0
                }
                if(binding.otherExpenseTransactionDiscountInput.getText().toString() != ""){
                    discount = binding.otherExpenseTransactionDiscountInput.getText().toString().toDouble()
                }else{
                    discount = 0.0
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.otherExpenseTransactionAdditionalChargesInput.getText().toString() != ""){
                    additionalCharge = binding.otherExpenseTransactionAdditionalChargesInput.getText().toString().toDouble()
                }else{
                    additionalCharge = 0.0
                }

                if(binding.otherExpenseTransactionQuantityInput.getText().toString() != ""){
                    quantity = binding.otherExpenseTransactionQuantityInput.getText().toString().toDouble()
                }else{
                    quantity = 0.0
                }

                if(binding.otherExpenseTransactionUnitPriceInput.getText().toString() != ""){
                    unitPrice = binding.otherExpenseTransactionUnitPriceInput.getText().toString().toDouble()
                }else{
                    unitPrice = 0.0
                }
                if(binding.otherExpenseTransactionDiscountInput.getText().toString() != ""){
                    discount = binding.otherExpenseTransactionDiscountInput.getText().toString().toDouble()
                }else{
                    discount = 0.0
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                var totalAmount = (quantity * unitPrice) + additionalCharge - discount
                binding.otherExpenseTransactionTotalAmountView.setText(totalAmount.toString())
            }

        })

        binding.otherExpenseTransactionQuantityInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.otherExpenseTransactionAdditionalChargesInput.getText().toString() != ""){
                    additionalCharge = binding.otherExpenseTransactionAdditionalChargesInput.getText().toString().toDouble()
                }else{
                    additionalCharge = 0.0
                }

                if(binding.otherExpenseTransactionQuantityInput.getText().toString() != ""){
                    quantity = binding.otherExpenseTransactionQuantityInput.getText().toString().toDouble()
                }else{
                    quantity = 0.0
                }

                if(binding.otherExpenseTransactionUnitPriceInput.getText().toString() != ""){
                    unitPrice = binding.otherExpenseTransactionUnitPriceInput.getText().toString().toDouble()
                }else{
                    unitPrice = 0.0
                }
                if(binding.otherExpenseTransactionDiscountInput.getText().toString() != ""){
                    discount = binding.otherExpenseTransactionDiscountInput.getText().toString().toDouble()
                }else{
                    discount = 0.0
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.otherExpenseTransactionAdditionalChargesInput.getText().toString() != ""){
                    additionalCharge = binding.otherExpenseTransactionAdditionalChargesInput.getText().toString().toDouble()
                }else{
                    additionalCharge = 0.0
                }

                if(binding.otherExpenseTransactionQuantityInput.getText().toString() != ""){
                    quantity = binding.otherExpenseTransactionQuantityInput.getText().toString().toDouble()
                }else{
                    quantity = 0.0
                }

                if(binding.otherExpenseTransactionUnitPriceInput.getText().toString() != ""){
                    unitPrice = binding.otherExpenseTransactionUnitPriceInput.getText().toString().toDouble()
                }else{
                    unitPrice = 0.0
                }
                if(binding.otherExpenseTransactionDiscountInput.getText().toString() != ""){
                    discount = binding.otherExpenseTransactionDiscountInput.getText().toString().toDouble()
                }else{
                    discount = 0.0
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                var totalAmount = (quantity * unitPrice) + additionalCharge - discount
                binding.otherExpenseTransactionTotalAmountView.setText(totalAmount.toString())
            }


        })

        binding.otherExpenseTransactionAdditionalChargesInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.otherExpenseTransactionAdditionalChargesInput.getText().toString() != ""){
                    additionalCharge = binding.otherExpenseTransactionAdditionalChargesInput.getText().toString().toDouble()
                }else{
                    additionalCharge = 0.0
                }

                if(binding.otherExpenseTransactionQuantityInput.getText().toString() != ""){
                    quantity = binding.otherExpenseTransactionQuantityInput.getText().toString().toDouble()
                }else{
                    quantity = 0.0
                }

                if(binding.otherExpenseTransactionUnitPriceInput.getText().toString() != ""){
                    unitPrice = binding.otherExpenseTransactionUnitPriceInput.getText().toString().toDouble()
                }else{
                    unitPrice = 0.0
                }
                if(binding.otherExpenseTransactionDiscountInput.getText().toString() != ""){
                    discount = binding.otherExpenseTransactionDiscountInput.getText().toString().toDouble()
                }else{
                    discount = 0.0
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.otherExpenseTransactionAdditionalChargesInput.getText().toString() != ""){
                    additionalCharge = binding.otherExpenseTransactionAdditionalChargesInput.getText().toString().toDouble()
                }else{
                    additionalCharge = 0.0
                }

                if(binding.otherExpenseTransactionQuantityInput.getText().toString() != ""){
                    quantity = binding.otherExpenseTransactionQuantityInput.getText().toString().toDouble()
                }else{
                    quantity = 0.0
                }

                if(binding.otherExpenseTransactionUnitPriceInput.getText().toString() != ""){
                    unitPrice = binding.otherExpenseTransactionUnitPriceInput.getText().toString().toDouble()
                }else{
                    unitPrice = 0.0
                }
                if(binding.otherExpenseTransactionDiscountInput.getText().toString() != ""){
                    discount = binding.otherExpenseTransactionDiscountInput.getText().toString().toDouble()
                }else{
                    discount = 0.0
                }

            }


            override fun afterTextChanged(p0: Editable?) {
                var totalAmount = (quantity * unitPrice) + additionalCharge - discount
                binding.otherExpenseTransactionTotalAmountView.setText(totalAmount.toString())
            }


        })

        binding.otherExpenseTransactionDiscountInput.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.otherExpenseTransactionAdditionalChargesInput.getText().toString() != ""){
                    additionalCharge = binding.otherExpenseTransactionAdditionalChargesInput.getText().toString().toDouble()
                }else{
                    additionalCharge = 0.0
                }

                if(binding.otherExpenseTransactionQuantityInput.getText().toString() != ""){
                    quantity = binding.otherExpenseTransactionQuantityInput.getText().toString().toDouble()
                }else{
                    quantity = 0.0
                }

                if(binding.otherExpenseTransactionUnitPriceInput.getText().toString() != ""){
                    unitPrice = binding.otherExpenseTransactionUnitPriceInput.getText().toString().toDouble()
                }else{
                    unitPrice = 0.0
                }
                if(binding.otherExpenseTransactionDiscountInput.getText().toString() != ""){
                    discount = binding.otherExpenseTransactionDiscountInput.getText().toString().toDouble()
                }else{
                    discount = 0.0
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.otherExpenseTransactionAdditionalChargesInput.getText().toString() != ""){
                    additionalCharge = binding.otherExpenseTransactionAdditionalChargesInput.getText().toString().toDouble()
                }else{
                    additionalCharge = 0.0
                }

                if(binding.otherExpenseTransactionQuantityInput.getText().toString() != ""){
                    quantity = binding.otherExpenseTransactionQuantityInput.getText().toString().toDouble()
                }else{
                    quantity = 0.0
                }

                if(binding.otherExpenseTransactionUnitPriceInput.getText().toString() != ""){
                    unitPrice = binding.otherExpenseTransactionUnitPriceInput.getText().toString().toDouble()
                }else{
                    unitPrice = 0.0
                }
                if(binding.otherExpenseTransactionDiscountInput.getText().toString() != ""){
                    discount = binding.otherExpenseTransactionDiscountInput.getText().toString().toDouble()
                }else{
                    discount = 0.0
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                var totalAmount = (quantity * unitPrice) + additionalCharge - discount
                binding.otherExpenseTransactionTotalAmountView.setText(totalAmount.toString())
            }

        })
        binding.saveOtherExpenseTransactionButton.setOnClickListener {
            var otherExpenseTransactionDate =
                binding.otherExpenseTransactionDateInput.text.toString()
            var otherExpenseTransactionTo = binding.otherExpenseTransactionToInput.text.toString()
            var otherExpenseTransactionQuantity =
                binding.otherExpenseTransactionQuantityInput.text.toString()
            var otherExpenseTransactionUnit =
                binding.otherExpenseTransactionUnitInput.text.toString()
            var otherExpenseTransactionUnitPrice =
                binding.otherExpenseTransactionUnitPriceInput.text.toString()
            var otherExpenseTransactionAdditionalCharges: String =
                binding.otherExpenseTransactionAdditionalChargesInput.text.toString()
            var otherExpenseTransactionDiscount: String =
                binding.otherExpenseTransactionDiscountInput.text.toString()
            var otherExpenseTransactionTotalAmount =
                binding.otherExpenseTransactionTotalAmountView.text.toString()
            var otherExpenseTransactionCategory =
                binding.otherExpenseTransactionCategoryInput.text.toString()
            var otherExpenseTransactionNotes: String =
                binding.otherExpenseTransactionNotesInput.text.toString()

            if (otherExpenseTransactionDate != "" && otherExpenseTransactionTo != "" && otherExpenseTransactionQuantity != "" && otherExpenseTransactionUnit != "" && otherExpenseTransactionUnitPrice != "" && otherExpenseTransactionTotalAmount != "" && otherExpenseTransactionCategory.isNotEmpty()) {
                if (otherExpenseTransactionAdditionalCharges == ""){ otherExpenseTransactionAdditionalCharges = ""}
                if (otherExpenseTransactionDiscount == "") {otherExpenseTransactionDiscount = ""}
                if (otherExpenseTransactionNotes == "") {otherExpenseTransactionNotes = ""}

                val transactionOtherExpense = TransactionOtherExpense(
                    otherExpenseTransactionDate,
                    otherExpenseTransactionTo,
                    otherExpenseTransactionQuantity,
                    otherExpenseTransactionUnit,
                    otherExpenseTransactionUnitPrice,
                    otherExpenseTransactionAdditionalCharges,
                    otherExpenseTransactionDiscount,
                    otherExpenseTransactionTotalAmount,
                    otherExpenseTransactionCategory,
                    otherExpenseTransactionNotes
                )
                // save Transaction and update party
                val firebaseOperationsForProjectInternalTransactions = FirebaseOperationsForProjectInternalTransactions()
                try{
                    if (projectId!= null)
                        firebaseOperationsForProjectInternalTransactions.saveOtherExpenseTransaction(projectId, transactionOtherExpense)
                    finish()
                }catch (e:Exception){
                    Toast.makeText(this, e.message , Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Please Fill Required details.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun backButton(view: View){
        finish()
    }

    fun cameraButton(view: View){

    }

    fun galleryButton(view: View){

    }

    lateinit var selectedParty: Party
    override fun onPartySelected(party: Party?) {
        if (party != null) {
            selectedParty = party
        }
        var otherExpenseTransactionToInput:EditText = findViewById(R.id.otherExpenseTransactionToInput)
        otherExpenseTransactionToInput.setText(selectedParty?.partyName.toString())
        Toast.makeText(this, selectedParty.partyId, Toast.LENGTH_SHORT).show()
    }

    fun showDatePickerDialog(binding: ActivityNewOtherExpenseTransactionBinding){
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog: DatePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.otherExpenseTransactionDateInput.setText("$dayOfMonth/${month + 1}/$year")
        }, year, month, day)

        datePickerDialog.show()

    }

}