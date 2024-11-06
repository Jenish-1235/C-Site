package com.csite.app.FirebaseOperations

import android.util.Log
import androidx.compose.animation.core.snap
import com.csite.app.Objects.Member
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
    val projectReference = FirebaseDatabase.getInstance().getReference("Projects")

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

    fun updateProjectStatus(projectId: String, newStatus: String) {
        projectReference.child(projectId).child("projectStatus").setValue(newStatus)
    }

    fun getProjectMembers(projectId: String, callback: (List<Member>) -> Unit) {
        projectReference.child(projectId).child("projectMembers").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val members = mutableListOf<Member>()
                for (memberSnapshot in snapshot.children) {
                    val member = memberSnapshot.getValue(Member::class.java)
                    if (member != null) {
                        members.add(member)
                        Log.d("FirebaseOperations", "Member: $member")
                    }
                }
                callback(members)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun updateProjectMembers(projectId: String, newMembers: Member) {
        projectReference.child(projectId).child("projectMembers").setValue(newMembers)
    }
    fun leaveProject(projectId: String, mobileNumber: String) {
        projectReference.child(projectId).child("projectMembers").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.childrenCount.toInt() == 1){
                    projectReference.child(projectId).removeValue()
                }else{
                    val members = mutableListOf<Member>()
                    for (memberSnapshot in snapshot.children) {
                        val member = memberSnapshot.getValue(Member::class.java)
                        if (member != null && member.mobileNumber != mobileNumber) {
                            members.add(member)
                            Log.d("FirebaseOperations", "Member: $member")
                        }
                    }
                    projectReference.child(projectId).child("projectMembers").setValue(members)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}