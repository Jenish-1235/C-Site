package com.csite.app.FirebaseOperations

import com.csite.app.Objects.MaterialRequestOrReceived
import com.csite.app.Objects.MaterialSelection
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class FirebaseOperationsForProjectInternalMaterialTab {
    object FirebaseOperationsForProjectInternalMaterialTab{}

    val projectReference = FirebaseDatabase.getInstance().getReference("Projects")

    // GET TODAY'S DATE

    fun getDateTimeWithTime(): String {
        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return formatter.format(date)
    }

    // 1. Save All Material Requests to firebase
    fun saveMaterialRequests(projectId: String , materialList: HashMap<String, MaterialSelection>) {
        val date = getDateTimeWithTime()
        projectReference.child(projectId).child("MaterialRequests").child(date).setValue(materialList)
    }

    fun saveMaterialReceived(projectId: String , materialList: HashMap<String, MaterialSelection>) {
        val date = getDateTimeWithTime()
        projectReference.child(projectId).child("MaterialReceived").child(date).setValue(materialList)
    }

    fun fetchMaterialRequests(projectId: String, callback: OnMaterialRequestReceived) {
        var materialRequestList = ArrayList<MaterialRequestOrReceived>()
        projectReference.child(projectId).child("MaterialRequests").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var newMaterialRequest = MaterialRequestOrReceived()
                        newMaterialRequest.type = "Requested"
                    for(materialRequest in snapshot.children) {
                        newMaterialRequest.dateTimeStamp = materialRequest.key.toString()
                        for (material in materialRequest.children) {
                            val materialSelection = material.getValue(MaterialSelection::class.java)
                            if (materialSelection != null) {
                                newMaterialRequest.materialId = materialSelection.materialId
                                newMaterialRequest.materialQuantity = materialSelection.materialQuantity
                                newMaterialRequest.materialName = materialSelection.materialName
                                newMaterialRequest.materialUnit = materialSelection.materialUnit
                                newMaterialRequest.materialCategory = materialSelection.materialCategory
                                materialRequestList.add(newMaterialRequest)
                            }

                        }
                    }

                    callback.onMaterialRequestReceived(materialRequestList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    interface OnMaterialRequestReceived{
        fun onMaterialRequestReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>)
    }



}