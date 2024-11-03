package com.csite.app.DialogFragments

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.Objects.Material
import com.csite.app.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNewMaterialDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.dialog_fragment_add_new_material, container, false)
        val dialog = getDialog()
        if (dialog != null) {
            dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        }


        val materialNameInput: EditText = view.findViewById(R.id.materialNameInput)
        val materialUnitInput: AutoCompleteTextView = view.findViewById(R.id.materialUnitInput)
        val materialGSTInput: AutoCompleteTextView = view.findViewById(R.id.materialGSTInput)
        val materialCategoryInput: AutoCompleteTextView = view.findViewById(R.id.materialCategoryInput)

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
        materialUnitInput.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                materialUnitInputList
            )
        )

        val materialGSTList: List<String> = mutableListOf(
            "18%",
            "12%",
            "5%",
            "0%",
            "28%",
            "0.1%",
            "0.25%",
            "1.5%",
            "3%",
            "6%",
            "7.5%",
            "14%"
        )
        materialGSTInput.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                materialGSTList
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
        materialCategoryInput.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                materialCategoryList
            )
        )


        val saveNewMaterialButton: Button = view.findViewById(R.id.saveNewMaterialButton)
        saveNewMaterialButton.setOnClickListener{

            val materialName = materialNameInput.text.toString()
            val materialUnit = materialUnitInput.text.toString()
            val materialGST = materialGSTInput.text.toString()
            val materialCategory  =materialCategoryInput.text.toString()
            if(materialName.isEmpty() || materialUnit.isEmpty() || materialCategory.isEmpty() || materialCategory.isEmpty() || materialUnit.equals("Unit") || materialGST.equals("GST")){
                Toast.makeText(context, "Please fill All details", Toast.LENGTH_SHORT).show()
            }else{
                val newMaterial = Material(materialName, materialCategory, materialGST, materialUnit)
                val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
                firebaseOperationsForLibrary.addMaterialsToMaterialLibrary( newMaterial)
                Toast.makeText(context, "New Material Added To Library", Toast.LENGTH_SHORT).show()
                dismiss()
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