package com.csite.app.FirebaseOperations

import com.csite.app.Objects.ProjectWorker
import com.google.firebase.database.FirebaseDatabase

class FirebaseOperationsForProjectInternalAttendance {
    object FirebaseOperationsForProjectInternalAttendance{}

    // 1. Save workerlist to firebase
    val projectReference = FirebaseDatabase.getInstance().getReference("Projects")
    fun addWorkersToProject(projectId:String , workersList: HashMap<String, ProjectWorker>) {
        projectReference.child(projectId).child("ProjectWorkers").setValue(workersList)
    }
}