package com.csite.app.FirebaseOperations

import com.csite.app.Objects.Contractor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.HashMap

class FirebaseOperationsForProjectInternalAttendanceTab {

    val projectReference = FirebaseDatabase.getInstance().getReference("Projects")

    fun saveSelectedContractorsToProjectAttendance(path: String, contractorList: HashMap<String,Contractor>){
        projectReference.child(path).setValue(contractorList)
    }

    fun fetchContractorListFromAttendance(projectId: String, currentDate: String, callback: FirebaseOperationsForProjectInternalAttendanceTab.getAttendanceContractorList){
        projectReference.child(projectId).child("ProjectAttendance").child(currentDate).addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val contractorList = HashMap<String, Contractor>()
                    for(contractor in snapshot.children){
                        contractorList[contractor.key.toString()] = contractor.getValue(Contractor::class.java)!!
                    }
                    callback.onAttendanceContractorListReceived(contractorList)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
        )
    }

    interface getAttendanceContractorList{
        fun onAttendanceContractorListReceived(contractorList: HashMap<String, Contractor>)
    }
}