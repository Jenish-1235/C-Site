package com.csite.app.Activites.Library

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.DialogFragments.CreateNewWorkforceDialogFragment
import com.csite.app.DialogFragments.PartySelectionLibraryDialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.Objects.Contractor
import com.csite.app.Objects.Party
import com.csite.app.Objects.Workforce
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.WorkforceListAdapter
import com.google.common.collect.Queues
import java.util.Queue
import java.util.Stack

class AddNewContractorActivity : AppCompatActivity() , CreateNewWorkforceDialogFragment.OnWorkforceAddedListener {

    var workforceList = Stack<Workforce>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_contractor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val newWorkforceButton:Button = findViewById(R.id.addNewWorkforceButton)
        newWorkforceButton.setOnClickListener {
            val newWorkforceDialogFragment = CreateNewWorkforceDialogFragment()
            newWorkforceDialogFragment.show(supportFragmentManager, "CreateNewWorkforceDialogFragment")
        }

        val contractorName:EditText = findViewById(R.id.contractorNameInput)
        val contractorPhoneNumber = findViewById<EditText>(R.id.contractorNumberInput)

        val saveContractorButton:Button = findViewById(R.id.saveContractorButton)
        saveContractorButton.setOnClickListener{
            if (contractorName.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter a name for the contractor", Toast.LENGTH_SHORT).show()
            }else if(contractorPhoneNumber.text.toString().isEmpty() || contractorPhoneNumber.text.toString().length != 10){
                Toast.makeText(this, "Please enter phone number for the contractor", Toast.LENGTH_SHORT).show()
            }
            else if(workforceList.isEmpty()){
                Toast.makeText(this, "Please add a workforce to the contractor", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Contractor saved", Toast.LENGTH_SHORT).show()
                var workforceHashMap = HashMap<String, Workforce>()
                for (workforce in workforceList){
                    workforceHashMap[workforce.workforceId] = workforce
                }
                val newContractor = Contractor(contractorName.text.toString(), sixDigitIdGeneratorForContractor(), contractorPhoneNumber.text.toString(),workforceHashMap)
                val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
                firebaseOperationsForLibrary.addNewContractorToLibrary(newContractor)
            }
            finish()
        }
    }

    fun backButton(view: View){
        finish()
    }

    override fun onWorkforceAddedListener(
        workforceType: String,
        workforceSalaryPerDay: String,
        workforceCategory: String,
        workforceNumberOfWorkers: String
    ) {
        Toast.makeText(this, workforceNumberOfWorkers, Toast.LENGTH_SHORT).show()
        createNewWorkforce(workforceType,workforceSalaryPerDay, workforceCategory, workforceNumberOfWorkers)
    }

    fun createNewWorkforce(workforceType: String, workforceSalaryPerDay: String, workforceCategory:String, workforceNumberOfWorkers:String){
        val newWorkforce = Workforce(workforceType, workforceSalaryPerDay, workforceCategory, workforceNumberOfWorkers, sixDigitIdGeneratorForWorkforce())
        Toast.makeText(this, newWorkforce.workforceNumberOfWorkers, Toast.LENGTH_SHORT).show()
        workforceList.push(newWorkforce)
        val workforceRecyclerView = findViewById<RecyclerView>(R.id.workforceRecyclerView)
        workforceRecyclerView.adapter = WorkforceListAdapter(workforceList)
        workforceRecyclerView.adapter?.notifyDataSetChanged()
        workforceRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun sixDigitIdGeneratorForWorkforce():String{
        val random = (100000..999999).random()
        return "wf$random"
    }

    fun sixDigitIdGeneratorForContractor():String{
        val random = (100000..999999).random()
        return "ct$random"
    }
}