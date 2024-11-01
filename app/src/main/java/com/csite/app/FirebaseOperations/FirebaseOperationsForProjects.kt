package com.csite.app.FirebaseOperations

import android.util.Log
import com.csite.app.Objects.Project
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class FirebaseOperationsForProjects {
    object FirebaseOperationsForProjects{    }

    val metaReference = FirebaseDatabase.getInstance().getReference("Meta")

    fun saveProjectToFirebase(projectReference: DatabaseReference, project: Project) {

        metaReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val refNextProjectID: DataSnapshot = snapshot.child("nextProjectId")
                    val valNextProjectId: String = refNextProjectID.getValue(String::class.java) ?: ""

                    metaReference.child("nextProjectId").setValue((Integer.parseInt(valNextProjectId) + 1).toString())
                    project.projectId = valNextProjectId
                    projectReference.child(valNextProjectId).setValue(project)

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseOperations", "Error fetching nextProjectId: ${error.message}")
            }
        })



    }

}