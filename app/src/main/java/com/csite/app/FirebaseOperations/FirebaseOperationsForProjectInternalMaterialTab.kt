package com.csite.app.FirebaseOperations

import com.csite.app.Objects.MaterialSelection
import com.google.firebase.database.FirebaseDatabase
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
}