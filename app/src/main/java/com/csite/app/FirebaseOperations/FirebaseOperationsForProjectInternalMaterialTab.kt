package com.csite.app.FirebaseOperations

import android.util.Log
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
        var materialRequestList = ArrayList<MaterialSelection>()
        var materialRequestList2 = ArrayList<MaterialRequestOrReceived>()
        var dateTimeStamp = ""
        projectReference.child(projectId).child("MaterialRequests").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                materialRequestList.clear()
                for(materialRequest in snapshot.children) {
                    dateTimeStamp = ""
                    for (material in materialRequest.children) {
                        val materialSelection = material.getValue(MaterialSelection::class.java)
                        var newMaterialRequest = MaterialRequestOrReceived()
                        newMaterialRequest.type = "Request"
                        newMaterialRequest.dateTimeStamp = materialRequest.key.toString()
                        if (materialSelection != null) {
                            materialRequestList.add(materialSelection)
                            newMaterialRequest.materialId = materialSelection.materialId
                            newMaterialRequest.materialQuantity = materialSelection.materialQuantity
                            newMaterialRequest.materialName = materialSelection.materialName
                            newMaterialRequest.materialUnit = materialSelection.materialUnit
                            newMaterialRequest.materialCategory = materialSelection.materialCategory
                            materialRequestList2.add(newMaterialRequest)
                        }
                    }
                    Log.d("TAG", "onDataChange: ${materialRequestList2[0].materialId} + ${materialRequestList2[1].materialId}")
                    callback.onMaterialRequestReceived(materialRequestList2)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    interface OnMaterialRequestReceived{
        fun onMaterialRequestReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>)
    }

    fun fetchMaterialReceived(projectId: String, callback: OnMaterialReceivedReceived) {
        var materialRequestList = ArrayList<MaterialSelection>()
        var materialRequestList2 = ArrayList<MaterialRequestOrReceived>()
        var dateTimeStamp = ""
        projectReference.child(projectId).child("MaterialReceived").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                materialRequestList.clear()
                for(materialRequest in snapshot.children) {
                    dateTimeStamp = ""
                    for (material in materialRequest.children) {
                        val materialSelection = material.getValue(MaterialSelection::class.java)
                        var newMaterialRequest = MaterialRequestOrReceived()
                        newMaterialRequest.type = "Received"
                        newMaterialRequest.dateTimeStamp = materialRequest.key.toString()
                        if (materialSelection != null) {
                            materialRequestList.add(materialSelection)
                            newMaterialRequest.materialId = materialSelection.materialId
                            newMaterialRequest.materialQuantity = materialSelection.materialQuantity
                            newMaterialRequest.materialName = materialSelection.materialName
                            newMaterialRequest.materialUnit = materialSelection.materialUnit
                            newMaterialRequest.materialCategory = materialSelection.materialCategory
                            materialRequestList2.add(newMaterialRequest)
                        }
                    }
                    callback.onMaterialReceivedReceived(materialRequestList2)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    interface OnMaterialReceivedReceived{
        fun onMaterialReceivedReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>)
    }


}