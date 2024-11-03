package com.csite.app.DialogFragments

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.Objects.Workforce
import com.csite.app.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNewWorkforceDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.dialog_fragment_add_new_workforce, container, false)
        val dialog = getDialog()
        if (dialog != null) {
            dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        val workforceTypeInput = view.findViewById<EditText>(R.id.workforceTypeInput)
        val workforceSalaryPerShiftInput = view.findViewById<EditText>(R.id.workforceSalaryPerShiftInput)
        val workforceCategoryInput:AutoCompleteTextView = view.findViewById(R.id.workforceCategoryInput)

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
        workforceCategoryInput.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                materialCategoryList
            )
        )

        val saveNewWorkforceButton = view.findViewById<View>(R.id.saveNewWorkforceButton)
        saveNewWorkforceButton.setOnClickListener {
            val workforceType = workforceTypeInput.text.toString()
            val workforceSalaryPerShift = workforceSalaryPerShiftInput.text.toString()
            val workforceCategory = workforceCategoryInput.text.toString()
            if (workforceType.isNotEmpty() && workforceSalaryPerShift.isNotEmpty() && workforceCategory.isNotEmpty()) {
                val firebaseOperationsForLibrary: FirebaseOperationsForLibrary = FirebaseOperationsForLibrary()
                firebaseOperationsForLibrary.addWorkforceToWorkforceLibrary(Workforce(workforceType, workforceSalaryPerShift, workforceCategory))
                dismiss()
                Toast.makeText(requireContext(), "Workforce added successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        return view
    }

    // sets positioning of dialog fragment to bottom of screen.
    override fun onStart() {
        super.onStart()

        val dialog: Dialog? = getDialog()

        if (dialog != null){
            val window = dialog.getWindow()
            if (window != null){
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                window.setGravity(Gravity.BOTTOM)

                val params = window.getAttributes()
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
                params.horizontalMargin = 0f;
                params.verticalMargin = 0f;

                window.setWindowAnimations(R.style.DialogAnimation)
            }
        }
    }

}