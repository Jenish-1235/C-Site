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

    // 1.1 Fetch workerlist from firebase
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
    // 3. Check if workers exist in project.
    fun checkWorkerExist(projectId: String, callback:(Boolean) -> Unit) {
        projectReference.child(projectId).child("ProjectWorkers").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val workersExist = snapshot.exists()
                callback(workersExist)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }
    // 4. Check if attendance exists
    fun checkAttendanceExists(projectId: String, callback: (Boolean) -> Unit) {
        projectReference.child(projectId).child("ProjectAttendance").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val attendanceExists = snapshot.exists()
                callback(attendanceExists)
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }

    fun saveAttendance(projectId: String, date: String, attendance: HashMap<String, ProjectWorker>) {
        val hashMapWithDate = HashMap<String, HashMap<String, ProjectWorker>>()
        hashMapWithDate[date] = attendance
        val finalHashMap = HashMap<String, Any>()
        finalHashMap["ProjectAttendance"] = hashMapWithDate
        projectReference.child(projectId).updateChildren(finalHashMap as Map<String, Any>)
    }

    fun updateAttendance(projectId: String, date: String, attendance: HashMap<String, ProjectWorker>) {
        val hashMapWithDate = HashMap<String, HashMap<String, ProjectWorker>>()
        hashMapWithDate[date] = attendance
        projectReference.child(projectId).child("ProjectAttendance").updateChildren(hashMapWithDate as Map<String, Any>)
    }

    fun checkDateExists(projectId: String, date: String, callback: (Boolean) -> Unit) {
        projectReference.child(projectId).child("ProjectAttendance").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dateExists = snapshot.hasChild(date)
                callback(dateExists)
                }
            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }

    fun getWorkersForExistingAttendance(projectId: String, date: String,callback: OnAttendanceWorkerFetched){
        val workersList = ArrayList<ProjectWorker>()
        projectReference.child(projectId).child("ProjectAttendance").child(date).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                workersList.clear()
                for (workerSnapshot in snapshot.children) {
                    val worker = workerSnapshot.getValue(ProjectWorker::class.java)
                    if (worker != null) {
                        workersList.add(worker)
                    }
                }
                callback.onAttendanceWorkerFetched(workersList)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    interface OnAttendanceWorkerFetched{
        fun onAttendanceWorkerFetched(workersList: ArrayList<ProjectWorker>)
    }

    fun checkCalculationExists(projectId: String, callback: (Boolean) -> Unit) {
        projectReference.child(projectId).child("ProjectCalculation").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val calculationExists = snapshot.exists()
                callback(calculationExists)
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }

    fun saveOrUpdateCalculation(projectId: String, calculation: HashMap<String, HashMap<String, String>>, i: Int) {
        if (i == 0){
            projectReference.child(projectId).child("AttendanceCalculation").setValue(calculation)
        }else{
            projectReference.child(projectId).child("AttendanceCalculation").updateChildren(calculation as Map<String, Any>)
        }
    }

    fun getCalculatedValues(projectId: String, date:String, callback:OnCalculatedValuesFetched){
        val calculatedValues = HashMap<String, String>()
        projectReference.child(projectId).child("AttendanceCalculation").child(date).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                calculatedValues.clear()
                for (calculatedValueSnapshot in snapshot.children) {
                    val value = calculatedValueSnapshot.getValue(String::class.java)
                    if (value != null) {
                        calculatedValues[calculatedValueSnapshot.key!!] = value
                    }
                }
                callback.onCalculatedValuesFetched(calculatedValues)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    interface OnCalculatedValuesFetched{
        fun onCalculatedValuesFetched(calculatedValues: HashMap<String, String>)
    }
}