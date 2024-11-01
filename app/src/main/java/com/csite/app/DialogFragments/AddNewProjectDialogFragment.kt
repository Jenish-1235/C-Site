package com.csite.app.DialogFragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.CalendarContract.CalendarAlerts
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.compose.material3.DatePickerDialog
import androidx.fragment.app.DialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjects
import com.csite.app.Objects.Member
import com.csite.app.Objects.Project
import com.csite.app.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.values


class AddNewProjectDialogFragment : DialogFragment() {

    lateinit var projectNameInput: EditText;
    lateinit var projectAddressInput: EditText;
    lateinit var projectCityInput: EditText;
    lateinit var projectStartDateInput: EditText;
    lateinit var projectEndDateInput: EditText;
    lateinit var projectValueInput: EditText;

    lateinit var createProject: Button;

    val metaReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Meta")
    val projectsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Projects")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.dialog_fragment_add_new_project, container, false)

        projectNameInput = view.findViewById(R.id.projectNameInput)
        projectAddressInput = view.findViewById(R.id.projectAddressInput)
        projectCityInput = view.findViewById(R.id.projectCityInput)
        projectStartDateInput = view.findViewById(R.id.projectStartDateInput)
        projectEndDateInput = view.findViewById(R.id.projectEndDateInput)
        projectValueInput = view.findViewById(R.id.projectValueInput)

        projectStartDateInput.setOnClickListener {
            showDatePickerDialog(1)
        }

        projectEndDateInput.setOnClickListener {
            showDatePickerDialog(2)
        }

        createProject = view.findViewById(R.id.createProjectButton)
        createProject.setOnClickListener {

            if (projectNameInput.text.toString().isEmpty() || projectAddressInput.text.toString().isEmpty() || projectCityInput.text.toString().isEmpty() || projectStartDateInput.text.toString().isEmpty() || projectEndDateInput.text.toString().isEmpty()){
                    projectNameInput.error = "Please fill in all fields"
            }else{


                val project : Project = Project()
                project.projectName = projectNameInput.text.toString()
                project.projectAddress = projectAddressInput.text.toString()
                project.projectCity = projectCityInput.text.toString()
                project.projectStartDate = projectStartDateInput.text.toString()
                project.projectEndDate = projectEndDateInput.text.toString()
                project.projectValue = projectValueInput.text.toString()
                project.projectStatus = "Active"
                if (projectValueInput.text.toString().isEmpty()){
                    project.projectValue = "0"
                }

                val memberMobileNumberSharedPreferences:SharedPreferences = requireContext().getSharedPreferences("mobileNumber", MODE_PRIVATE)
                val memberMobileNumber = memberMobileNumberSharedPreferences.getString("mobileNumber", "")
                val memberNameSharedPreferences:SharedPreferences = requireContext().getSharedPreferences("memberName", MODE_PRIVATE)
                val memberName = memberNameSharedPreferences.getString("memberName", "")
                val memberAccessSharedPreferences:SharedPreferences = requireContext().getSharedPreferences("memberAccess", MODE_PRIVATE)
                val memberAccess = memberAccessSharedPreferences.getString("memberAccess", "")
                if (memberMobileNumber != null && memberName != null && memberAccess != null) {

                    val memberHashMap: HashMap<String, Member> = HashMap()
                    val member: Member = Member(memberName, memberMobileNumber, memberAccess)
                    memberHashMap.put(memberMobileNumber, member)
                    project.projectMembers = memberHashMap
                }


                val firebaseOperationsForProjects = FirebaseOperationsForProjects()
                firebaseOperationsForProjects.saveProjectToFirebase(projectsReference, project)
                dismiss()
            }
        }


        val dialog = getDialog()
        if (dialog != null) {
            dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
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

    private fun showDatePickerDialog(inputId: Int) {
        val calendar: Calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),
            { view, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                // Do something with the selected date
                if (inputId == 1) {
                    projectStartDateInput.setText(selectedDate)
                }else{
                    projectEndDateInput.setText(selectedDate)
                }
            }, year, month, day)

        datePickerDialog.show()

    }

}