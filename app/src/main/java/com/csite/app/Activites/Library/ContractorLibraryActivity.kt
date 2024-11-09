package com.csite.app.Activites.Library

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.Objects.Contractor
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.ContractorLibraryListAdapter

class ContractorLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contractor_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addNewContractorButton: Button = findViewById(R.id.addNewContractorButton)

        addNewContractorButton.setOnClickListener{
            val addNewContractorActivityIntent = Intent(this, AddNewContractorActivity::class.java)
            startActivity(addNewContractorActivityIntent)
        }

        val contractorRecyclerView:RecyclerView = findViewById(R.id.contractorLibraryRecyclerView)
        val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        firebaseOperationsForLibrary.fetchContractorListFromLibrary(object: FirebaseOperationsForLibrary.OnContractorListReceived{
            override fun onContractorListReceived(contractorList: ArrayList<Contractor>) {
                val contractorListAdapter = ContractorLibraryListAdapter(contractorList)
                contractorRecyclerView.adapter = contractorListAdapter
                contractorRecyclerView.layoutManager = LinearLayoutManager(this@ContractorLibraryActivity)
            }

        })

    }
}