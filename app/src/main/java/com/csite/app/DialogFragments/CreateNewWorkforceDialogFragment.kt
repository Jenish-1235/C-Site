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
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.csite.app.R

class CreateNewWorkforceDialogFragment : DialogFragment() {

    interface OnWorkforceAddedListener{
        fun onWorkforceAddedListener(workforceType:String,workforceSalaryPerDay:String,workforceCategory:String, workforceNumberOfWorkers:String)
    }

    lateinit var onWorkforceAddedListener: OnWorkforceAddedListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.dialog_fragment_create_new_workforce, container, false)
        val dialog = getDialog()
        if (dialog != null) {
            dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        }


        val workforceTypeInput:EditText = view.findViewById(R.id.workforceTypeInput)
        val workforceSalaryPerDayInput:EditText = view.findViewById(R.id.workforceSalaryPerDayInput)
        val workforceCategoryInput:AutoCompleteTextView = view.findViewById(R.id.workforceCategoryInput)
        val workforceNumberOfWorkersInput: EditText = view.findViewById(R.id.workforceNumberOfWorkersInput)
        val categoryList = arrayOf("Bills",
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
            "Wood Work")

        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,categoryList)
        workforceCategoryInput.setAdapter(adapter)

        val addNewWorkforceButton: Button = view.findViewById(R.id.addNewWorkforceButton)

        addNewWorkforceButton.setOnClickListener{
            if (workforceTypeInput.text.toString().isNotEmpty() && workforceSalaryPerDayInput.text.toString().isNotEmpty() && workforceCategoryInput.text.toString().isNotEmpty() && workforceNumberOfWorkersInput.text.toString().isNotEmpty()){
                onWorkforceAddedListener = context as OnWorkforceAddedListener
                onWorkforceAddedListener.onWorkforceAddedListener(workforceTypeInput.text.toString(),workforceSalaryPerDayInput.text.toString(),workforceCategoryInput.text.toString(), workforceNumberOfWorkersInput.text.toString())
                dismiss()
            }else{
                Toast.makeText(context,"Please fill all fields",Toast.LENGTH_SHORT).show()
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