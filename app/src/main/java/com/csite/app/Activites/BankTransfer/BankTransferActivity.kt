package com.csite.app.Activites.BankTransfer

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
import com.csite.app.DialogFragments.PartySelectionForBankTransferDialogFragment
import com.csite.app.DialogFragments.Project_Selection_Dialog_Fragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForBankTransfers
import com.csite.app.Objects.BankTransfer
import com.csite.app.Objects.Party
import com.csite.app.Objects.Project
import com.csite.app.R
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class BankTransferActivity: AppCompatActivity(), PartySelectionForBankTransferDialogFragment.OnPartySelectedListener, Project_Selection_Dialog_Fragment.OnProjectSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_transfer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        val dateInput: EditText = findViewById(R.id.dateInput)
        val partyNameInput: EditText = findViewById(R.id.partyNameInput)
        val accountNumberInput: EditText = findViewById(R.id.accountNumberInput)
        val amountInput: EditText = findViewById(R.id.amountInput)
        val headInput: Spinner = findViewById(R.id.headInput)
        val doneByInput: Spinner = findViewById(R.id.doneByInput)
        val siteInput: EditText = findViewById(R.id.siteInput)

        // Set click listener for date input
        dateInput.setOnClickListener {
            showDatePickerDialog()
        }

        val partySelectionForBankTransferDialogFragment = PartySelectionForBankTransferDialogFragment()
        partyNameInput.setOnClickListener{
            partySelectionForBankTransferDialogFragment.show(supportFragmentManager, "partySelectionForBankTransferDialogFragment")
        }
        val projectSelectionDialogFragment = Project_Selection_Dialog_Fragment()
        siteInput.setOnClickListener{
            projectSelectionDialogFragment.show(supportFragmentManager, "projectSelectionDialogFragment")
        }

        // Set up Head and Done By Spinners
        val headList: Array<String> = arrayOf(
            "Select Head",
            "Labour Payment",
            "Supplier with GST Number",
            "Salary",
            "Expense",
            "URD",
            "Bill Only"
        )
        val doneByList: Array<String> = arrayOf(
            "Select Done By",
            "ANIL SIR",
            "MANOJ SIR",
            "ASHOK SIR",
            "ISHAN",
            "VICKY",
            "DARSHAN",
            "NAND"
        )
        val headAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, headList)
        val doneByAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, doneByList)
        headAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        doneByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        headInput.adapter = headAdapter
        doneByInput.adapter = doneByAdapter

        var selectedHead: String = ""
        headInput.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedHead = headInput.selectedItem.toString()
                var selectedTextView: TextView = view as TextView
                selectedTextView.setTextColor(resources.getColor(R.color.black))

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        var selectedDoneBy: String = ""
        doneByInput.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedDoneBy = doneByInput.selectedItem.toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        // Set click listener for submit button
        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            val date = dateInput.text.toString()
            val partyName = partyNameInput.text.toString()
            val accountNumber = accountNumberInput.text.toString()
            val amount = amountInput.text.toString()
            val head = selectedHead
            val doneBy = selectedDoneBy
            val site = siteInput.text.toString()

            if (date.isEmpty() || partyName.isEmpty() || accountNumber.isEmpty() || amount.isEmpty() || head == "Select Head" || doneBy == "Select Done By" || site.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()

            } else {
                val newBankTransfer: BankTransfer = BankTransfer(date, partyName, accountNumber, amount, head, doneBy, site)
                val firebaseOperationsForBankTransfers: FirebaseOperationsForBankTransfers = FirebaseOperationsForBankTransfers()
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

        // Set click listener for export button
        var exportButton: Button = findViewById(R.id.exportButton)
        exportButton.setOnClickListener {
            val firebaseOperationsForBankTransfers: FirebaseOperationsForBankTransfers =
                FirebaseOperationsForBankTransfers()
            firebaseOperationsForBankTransfers.getBankTransfersFromFirebase(object :
                FirebaseOperationsForBankTransfers.onBankTransfersFetched {
                override fun onBankTransfersFetched(bankTransfers: ArrayList<BankTransfer>) {
                    if (exportToExcel(bankTransfers)){
                        firebaseOperationsForBankTransfers.deleteBankTransferFromFirebase()
                    }
                }
            })
        }
    }

    // Set OnClick Function for Back Button
    fun backButton(view:View){
        finish()
    }

    // Set OnClick Function for Date Input
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

    // This function exports the fetched bank transfer list to xlsx file
    fun exportToExcel(bankTransfers: ArrayList<BankTransfer>):Boolean{
        // Create a new workbook
        val workbook: XSSFWorkbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Bank Transfers")
        val headerRow = sheet.createRow(0)
        // Set the header row
        headerRow.createCell(0).setCellValue("Date")
        headerRow.createCell(1).setCellValue("Party Name")
        headerRow.createCell(2).setCellValue("Account Number")
        headerRow.createCell(3).setCellValue("Amount")
        headerRow.createCell(4).setCellValue("Head")
        headerRow.createCell(5).setCellValue("Done By")
        headerRow.createCell(6).setCellValue("Site")

        // Set the data rows
        for((index, bankTransfer) in bankTransfers.withIndex()){
            val row = sheet.createRow(index + 1)
            Log.d("BankTransferActivity", "Exporting bank transfer: ${bankTransfer.date}")
            row.createCell(0).setCellValue(bankTransfer.date)
            row.createCell(1).setCellValue(bankTransfer.partyName)
            row.createCell(2).setCellValue(bankTransfer.accountNumber)
            row.createCell(3).setCellValue(bankTransfer.amount)
            row.createCell(4).setCellValue(bankTransfer.head)
            row.createCell(5).setCellValue(bankTransfer.doneBy)
            row.createCell(6).setCellValue(bankTransfer.site)
        }

        // Save the workbook
        var file: File = File(Environment.getExternalStorageDirectory().absolutePath.toString() + "/Download/BankTransfers.xlsx")
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: Exception) {
                Toast.makeText(this, "Error Creating File due to ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
        }else{
            try {
                file.delete()
                file.createNewFile()
            }catch (e:Exception){
                Toast.makeText(this, "Error Creating File due to ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
        }

        // Export the workbook
        try {
            val fileOutputStream: FileOutputStream = FileOutputStream(file)
            workbook.write(fileOutputStream)
            fileOutputStream.close()
            Toast.makeText(this, "Bank Transfers Exported Successfully", Toast.LENGTH_SHORT).show()
            return true
        }catch (e:Exception){
            Toast.makeText(this, "Error Writing File due to ${e.message}", Toast.LENGTH_SHORT).show()
            return false
        }

    }

    override fun onPartySelected(party: Party?) {
        val partyNameInput = findViewById<EditText>(R.id.partyNameInput)
        partyNameInput.setText(party?.partyName)
    }

    override fun onProjectSelected(project: Project?) {
        val siteInput = findViewById<EditText>(R.id.siteInput)
        siteInput.setText(project?.projectName)
    }

}