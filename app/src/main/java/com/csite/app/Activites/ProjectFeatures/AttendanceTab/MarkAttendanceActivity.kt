package com.csite.app.Activites.ProjectFeatures.AttendanceTab

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalAttendanceTab
import com.csite.app.Objects.Workforce
import com.csite.app.R
import com.csite.app.databinding.ActivityMarkAttendanceBinding
import java.util.zip.Inflater

class MarkAttendanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b  = ActivityMarkAttendanceBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent = intent
        val contractorName = intent.getStringExtra("contractorName")
        val currentDate = intent.getStringExtra("date")
        val projectId = intent.getStringExtra("projectId")

        val workforceId = intent.getStringExtra("workforceId")
        val salaryPerDay = intent.getStringExtra("salaryPerDay")
        val workforceType = intent.getStringExtra("workforceType")
        val workforceCategory = intent.getStringExtra("workforceCategory")

        val workforceNumberOfPresent   =   intent.getStringExtra("workforceNumberOfPresent")
        val workforceNumberOfAbsent   =   intent.getStringExtra("workforceNumberOfAbsent")
        val workforceIsOverTime   =   intent.getBooleanExtra("workforceIsOverTime", false)
        val workforceIsLate   =   intent.getBooleanExtra("workforceIsLate", false)
        val workforceHasAllowance   =   intent.getBooleanExtra("workforceHasAllowance", false)
        val workforceHasDeduction   =   intent.getBooleanExtra("workforceHasDeduction", false)
        val workforceOvertimeAmount   =   intent.getStringExtra("overtimeAmount")
        val workforceLateAmount   =   intent.getStringExtra("lateAmount")
        val workforceAllowanceAmount   =    intent.getStringExtra("allowanceAmount")
        val workforceDeductionAmount   =   intent.getStringExtra("deductionAmount")
        val workforceTotalWorkers   =   intent.getStringExtra("totalWorkers")

        b.presentCountInput.setText(workforceNumberOfPresent)
        b.absentCountInput.setText(workforceNumberOfAbsent)
        b.overtimePayInput.setText(workforceOvertimeAmount)
        b.lateFineInput.setText(workforceLateAmount)
        b.allowancePayInput.setText(workforceAllowanceAmount)
        b.hasDeductionInput.setText(workforceDeductionAmount)
        b.notesInput.setText(workforceTotalWorkers)
        b.isLateCheck.isChecked = workforceIsLate
        b.isOvertimeCheck.isChecked = workforceIsOverTime
        b.hasAllowanceCheck.isChecked = workforceHasAllowance
        b.hasDeductionCheck.isChecked = workforceHasDeduction


        b.markAttendanceButton.setOnClickListener {

            val numberOfPresent = b.presentCountInput.text.toString()
            val numberOfAbsent = b.absentCountInput.text.toString()
            var overtimePay = "0"
            var lateFine = "0"
            val updatedWorkforce = Workforce()
            if (b.isOvertimeCheck.isChecked) {
                overtimePay = b.overtimePayInput.text.toString()
                updatedWorkforce.workforceIsOverTime = true
            }
            if (b.isLateCheck.isChecked) {
                lateFine = b.lateFineInput.text.toString()
                updatedWorkforce.workforceIsLate = true
            }
            var allowancePay = "0"
            if (b.hasAllowanceCheck.isChecked) {
                allowancePay = b.allowancePayInput.text.toString()
                updatedWorkforce.workforceHasAllowance = true
            }
            var deductionPay = "0"
            if (b.hasDeductionCheck.isChecked) {
                deductionPay = b.hasDeductionInput.text.toString()
                updatedWorkforce.workforceHasDeduction = true
            }
            val notes = b.notesInput.text.toString()
            updatedWorkforce.workforcePresentWorkers = numberOfPresent
            updatedWorkforce.workforceAbsentWorkers = numberOfAbsent
            updatedWorkforce.workforceOverTimePay = overtimePay
            updatedWorkforce.workforceLateFine = lateFine
            updatedWorkforce.workforceAllowance = allowancePay
            updatedWorkforce.workforceDeduction = deductionPay
            updatedWorkforce.workforceNotes = notes
            updatedWorkforce.workforceNumberOfWorkers = workforceTotalWorkers!!
            if (salaryPerDay != null) {
                updatedWorkforce.workforceSalaryPerDay = salaryPerDay
            }
            if (workforceType != null) {
                updatedWorkforce.workforceType = workforceType
            }
            if (workforceCategory != null) {
                updatedWorkforce.workforceCategory = workforceCategory
            }
            if (workforceId != null) {
                updatedWorkforce.workforceId = workforceId
            }

            if (workforceTotalWorkers != null) {
                if (workforceTotalWorkers < numberOfPresent + numberOfAbsent) {
                    Toast.makeText(this,"Total workers cannot be less than present + absent. Total workers are $workforceTotalWorkers", Toast.LENGTH_SHORT).show()
                }else {
                    val firebaseOperationsForProjectInternalAttendanceTab = FirebaseOperationsForProjectInternalAttendanceTab()
                    if (projectId != null) {
                        if (contractorName != null) {
                            if (workforceId != null) {
                                if (currentDate != null) {
                                    firebaseOperationsForProjectInternalAttendanceTab.markAttendance(projectId,contractorName,workforceId,currentDate,updatedWorkforce)
                                    finish()
                                }else {
                                    Toast.makeText(this, "Date is null", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(this, "Workforce Id is null", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this, "Contractor Name is null", Toast.LENGTH_SHORT).show()
                        }
                    }else {
                        Toast.makeText(this, "Project Id is null", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    fun backButton(view:View){
        finish()
    }
}