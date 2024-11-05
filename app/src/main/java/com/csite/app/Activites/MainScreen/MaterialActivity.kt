package com.csite.app.Activites.MainScreen

import android.os.Bundle
import android.view.View
import android.widget.NumberPicker.OnValueChangeListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalMaterialTab
import com.csite.app.Objects.MaterialRequestOrReceived
import com.csite.app.Objects.MaterialSelection
import com.csite.app.Objects.Project
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.MaterialTabListAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MaterialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val materialRequestsRecyclerView = findViewById<RecyclerView>(R.id.materialRequestsRecyclerView)

        val dbReference = FirebaseDatabase.getInstance().getReference("Projects")

        var requestList = ArrayList<MaterialRequestOrReceived>()
        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                requestList.clear()
                for (projectSnapshot in snapshot.children) {
                    if (projectSnapshot.hasChild("MaterialRequests")) {
                        for(materialRequestSnapshot in projectSnapshot.child("MaterialRequests").children){
                            if (materialRequestSnapshot != null) {
                                val materialRequestDateTimeStamp = materialRequestSnapshot.key.toString()
                                for(materialSelectionSnapshot in materialRequestSnapshot.children){
                                    val materialId = materialSelectionSnapshot.key.toString()
                                    val materialSelection = materialSelectionSnapshot.getValue(MaterialSelection::class.java)
                                    val materialRequestOrReceived = MaterialRequestOrReceived("Requested", materialRequestDateTimeStamp, materialSelection!!.materialName, materialSelection.materialQuantity, materialId, materialSelection.materialUnit, materialSelection.materialCategory)
                                    requestList.add(materialRequestOrReceived)
                                }
                            }
                        }
                    }
                }
                var adapter = MaterialTabListAdapter(requestList)
                materialRequestsRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
                materialRequestsRecyclerView.layoutManager = LinearLayoutManager(this@MaterialActivity)
            }


            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    fun backButton(view: View){
        finish()
    }
}