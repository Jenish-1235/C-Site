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

    // Firebase database reference
    val metaReference = FirebaseDatabase.getInstance().getReference("Meta")

    // 1. Save project to Firebase
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

    // 2.0 Get project list from Firebase
    fun getProjectListFromFirebase(projectReference: DatabaseReference, listStatus: String, mobileNumber: String, callback: getProjectListFromFirebaseCallback): List<Project> {
        val projectList = mutableListOf<Project>()
        val projectListValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                projectList.clear()
                for (projectSnapshot in snapshot.children) {
                    val project = projectSnapshot.getValue(Project::class.java)
                    if (project != null && project.projectStatus.equals(listStatus)){
                        if (project.projectMembers.contains(mobileNumber)){
                            projectList.add(project)
                        }
                    }
                }
                callback.onProjectListFetched(projectList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseOperations", "Error fetching project list: ${error.message}")
            }
        }
        projectReference.addValueEventListener(projectListValueEventListener)
        return projectList
    }

    // 2.1Callback interface for getting project list from Firebase
    interface getProjectListFromFirebaseCallback {
        fun onProjectListFetched(projectList: List<Project>)
    }

}