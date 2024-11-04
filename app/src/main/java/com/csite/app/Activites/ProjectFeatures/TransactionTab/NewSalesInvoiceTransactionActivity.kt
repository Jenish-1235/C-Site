package com.csite.app.Activites.ProjectFeatures.TransactionTab

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.DialogFragments.MaterialSelectionLibraryDialogFragment
import com.csite.app.DialogFragments.PartySelectionLibraryDialogFragment
import com.csite.app.Objects.MaterialSelection
import com.csite.app.Objects.Party
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.MaterialSelectionListAdapter
import com.csite.app.RecyclerViewListAdapters.SelectedMaterialsListAdapter
import com.csite.app.databinding.ActivityNewSalesInvoiceTransactionBinding

class NewSalesInvoiceTransactionActivity : AppCompatActivity() , PartySelectionLibraryDialogFragment.OnPartySelectedListener, MaterialSelectionLibraryDialogFragment.OnMaterialListReceived{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityNewSalesInvoiceTransactionBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        b.salesInvoiceTransactionDateInput.setOnClickListener {
            showDatePickerDialog(b)
        }

        b.salesInvoiceTransactionPaymentFrom.setOnClickListener {
            val partySelectionLibraryDialogFragment = PartySelectionLibraryDialogFragment()
            partySelectionLibraryDialogFragment.show(supportFragmentManager, "PartySelectionLibraryDialogFragment")
        }

        b.addMaterialButton.setOnClickListener {
            val materialSelectionLibraryDialogFragment = MaterialSelectionLibraryDialogFragment()
            materialSelectionLibraryDialogFragment.show(supportFragmentManager, "MaterialSelectionLibraryDialogFragment")
        }

        b.additionalChargesButton.setOnClickListener {
            if (b.additionalChargesButton.text == "+ Additional Charges") {
                b.additionalChargesButton.text = "- Remove Charges"
                b.additionalChargesButton.setTextColor(getColorStateList(R.color.red))
                b.additionalChargesInputLayout.visibility = View.VISIBLE
            }else {
                b.additionalChargesButton.text = "+ Additional Charges"
                b.additionalChargesInputLayout.visibility = View.GONE
                b.additionalChargesButton.setTextColor(getColorStateList(R.color.black))
            }
        }

        b.addDiscountButton.setOnClickListener {
            if (b.addDiscountButton.text == "+ Add Discount") {
                b.addDiscountButton.text = "- Remove Discount"
                b.addDiscountButton.setTextColor(getColorStateList(R.color.red))
                b.addDiscountInputLayout.visibility = View.VISIBLE
            }else{
                b.addDiscountButton.text = "+ Add Discount"
                b.addDiscountInputLayout.visibility = View.GONE
                b.addDiscountButton.setTextColor(getColorStateList(R.color.black))
            }
        }

        b.addCategoryButton.setOnClickListener {
            if (b.addCategoryButton.text == "+ Add Category") {
                b.addCategoryButton.text = "- Remove Category"
                b.addCategoryButton.setTextColor(getColorStateList(R.color.red))
                b.addCategoryInputLayout.visibility = View.VISIBLE
            }else{
                b.addCategoryButton.text = "+ Add Category"
                b.addCategoryInputLayout.visibility = View.GONE
                b.addCategoryButton.setTextColor(getColorStateList(R.color.black))
            }
        }

        b.addNotesButton.setOnClickListener {
            if (b.addNotesButton.text == "+ Add Notes") {
                b.addNotesButton.text = "- Remove Notes"
                b.addNotesButton.setTextColor(getColorStateList(R.color.red))
                b.addNotesInputLayout.visibility = View.VISIBLE
            }else{
                b.addNotesButton.text = "+ Add Notes"
                b.addNotesInputLayout.visibility = View.GONE
                b.addNotesButton.setTextColor(getColorStateList(R.color.black))
            }
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

        b.salesInvoiceTrasactionCategoryInput.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                materialCategoryList
            )
        )




    }

    fun backButton(view:View){
        finish()
    }

    fun galleryButton(view:View){

    }

    fun cameraButton(view:View){

    }

    fun showDatePickerDialog(binding: ActivityNewSalesInvoiceTransactionBinding){
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog: DatePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.salesInvoiceTransactionDateInput.setText("$dayOfMonth/${month + 1}/$year")
        }, year, month, day)

        datePickerDialog.show()
    }

    var selectedParty : Party? = null
    override fun onPartySelected(party: Party?) {
        val salesInvoiceTransactionPaymentFrom = findViewById<EditText>(R.id.salesInvoiceTransactionPaymentFrom)
        salesInvoiceTransactionPaymentFrom.setText(party?.partyName)
        selectedParty = party
    }

    var receivedMaterialList = ArrayList<MaterialSelection>()
    override fun sendMaterialList(selectedMaterialList: ArrayList<MaterialSelection>) {
        receivedMaterialList = selectedMaterialList
        val salesInvoiceTransactionRecyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.salesInvoiceSelectedMaterialsRecyclerView)
        val adapter = SelectedMaterialsListAdapter(selectedMaterialList)
        salesInvoiceTransactionRecyclerView.adapter = adapter
        salesInvoiceTransactionRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }


}