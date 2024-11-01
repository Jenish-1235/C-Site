package com.csite.app.Activites.MainScreen

import android.app.Activity
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.FirebaseOperations.FirebaseOperationsForBankTransfers
import com.csite.app.Objects.BankTransfer
import com.csite.app.R

class BankTransferActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_transfer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dateInput: EditText = findViewById(R.id.dateInput)
        val partyNameInput: EditText = findViewById(R.id.partyNameInput)
        val accountNumberInput: EditText = findViewById(R.id.accountNumberInput)
        val amountInput: EditText = findViewById(R.id.amountInput)
        val headInput: Spinner = findViewById(R.id.headInput)
        val doneByInput: Spinner = findViewById(R.id.doneByInput)
        val siteInput: EditText = findViewById(R.id.siteInput)

        dateInput.setOnClickListener {
            showDatePickerDialog()
        }

        val headList: Array<String> = arrayOf("Select Head", "Labour Payment", "Supplier with GST Number", "Salary", "Expense", "URD","Bill Only")
        val doneByList: Array<String> = arrayOf("Select Done By", "ANIL SIR", "MANOJ SIR", "ASHOK SIR", "ISHAN", "VICKY", "DARSHAN", "NAND")
        val headAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, headList)
        val doneByAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, doneByList)
        headAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        doneByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        headInput.adapter = headAdapter
        doneByInput.adapter = doneByAdapter

        var selectedHead: String = ""
        headInput.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedHead = headInput.selectedItem.toString()
                var selectedTextView: TextView = view as TextView
                selectedTextView.setTextColor(resources.getColor(R.color.black))

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        var selectedDoneBy: String = ""
        doneByInput.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedDoneBy = doneByInput.selectedItem.toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        val submitButton:Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            val date = dateInput.text.toString()
            val partyName = partyNameInput.text.toString()
            val accountNumber = accountNumberInput.text.toString()
            val amount = amountInput.text.toString()
            val head = selectedHead
            val doneBy = selectedDoneBy
            val site = siteInput.text.toString()

            if (date.isEmpty() || partyName.isEmpty() || accountNumber.isEmpty() || amount.isEmpty() || head == "Select Head" || doneBy == "Select Done By" || site.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()

            }else{
                val newBankTransfer: BankTransfer = BankTransfer(date, partyName, accountNumber, amount, head, doneBy, site)
                val firebaseOperationsForBankTransfers : FirebaseOperationsForBankTransfers = FirebaseOperationsForBankTransfers()
                firebaseOperationsForBankTransfers.saveBankTransferToFirebase(newBankTransfer)
                Toast.makeText(this, "Bank Transfer Added Successfully", Toast.LENGTH_SHORT).show()

                dateInput.text.clear()
                partyNameInput.text.clear()
                accountNumberInput.text.clear()
                amountInput.text.clear()
                siteInput.text.clear()
                headInput.setSelection(0)
                doneByInput.setSelection(0)

            }
        }



}

    fun backButton(view:View){
        finish()
    }

    fun showDatePickerDialog(){
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog: DatePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val dateInput: EditText = findViewById(R.id.dateInput)
            dateInput.setText("$dayOfMonth/${month + 1}/$year")
        }, year, month, day)

        datePickerDialog.show()

    }

}