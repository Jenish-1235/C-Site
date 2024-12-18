package com.csite.app.Activites.ProjectFeatures.AttendanceTab

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.csite.app.Activites.Library.AddNewContractorActivity
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalAttendanceTab
import com.csite.app.Objects.Contractor
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.ContractorSelectionListAdapter
import com.csite.app.databinding.ActivityContractorSelectionBinding

class ContractorSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityContractorSelectionBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent = intent
        val projectId = intent.getStringExtra("projectId")
        val currentDate = intent.getStringExtra("currentDate")

        b.addNewContractorButton.setOnClickListener{
            val addNewContractorActivity = Intent(this, AddNewContractorActivity::class.java)
            startActivity(addNewContractorActivity)
        }

        val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        firebaseOperationsForLibrary.fetchContractorListFromLibrary(object : FirebaseOperationsForLibrary.OnContractorListReceived{
            override fun onContractorListReceived(contractorList: ArrayList<Contractor>) {
                b.contractorSelectionRecyclerView.adapter = ContractorSelectionListAdapter(contractorList)
                b.contractorSelectionRecyclerView.adapter?.notifyDataSetChanged()
                b.contractorSelectionRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
            }
        })

        b.selectContractorsButton.setOnClickListener{
            val selectedContractors = (b.contractorSelectionRecyclerView.adapter as ContractorSelectionListAdapter).sendSelectedContractorIdList()
            val firebaseOperationsForProjectInternalAttendanceTab = FirebaseOperationsForProjectInternalAttendanceTab()
            val path = "$projectId/ProjectAttendance/$currentDate"
            if (selectedContractors.size != 0) {
                firebaseOperationsForProjectInternalAttendanceTab.saveSelectedContractorsToProjectAttendance(
                    path,
                    selectedContractors
                )
            }else{
                Toast.makeText(this, "No Contractor Selected", Toast.LENGTH_SHORT).show()
            }
            finish()
        }


    }

    fun backButton(view: View){
        finish()
    }
}