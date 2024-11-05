package com.csite.app.FirebaseOperations

import com.csite.app.Objects.ProjectWorker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseOperationsForProjectInternalAttendance {
    object FirebaseOperationsForProjectInternalAttendance{}

    // 1. Save workerlist to firebase
    val projectReference = FirebaseDatabase.getInstance().getReference("Projects")
    fun addWorkersToProject(projectId:String , workersList: HashMap<String, ProjectWorker>) {
        projectReference.child(projectId).child("ProjectWorkers").setValue(workersList)
    }

    fun fetchProjectWorkers(projectId:String, callback: OnProjectWorkersFetched) {
        val workersList = ArrayList<ProjectWorker>()
        projectReference.child(projectId).child("ProjectWorkers").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                workersList.clear()
                for (workerSnapshot in snapshot.children) {
                    val worker = workerSnapshot.getValue(ProjectWorker::class.java)
                    if (worker != null) {
                        workersList.add(worker)
                    }
                }
                callback.onProjectWorkersFetched(workersList)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    interface OnProjectWorkersFetched {
        fun onProjectWorkersFetched(workersList: ArrayList<ProjectWorker>)
    }

}