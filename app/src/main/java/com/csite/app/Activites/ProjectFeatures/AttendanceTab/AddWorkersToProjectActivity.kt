package com.csite.app.Activites.ProjectFeatures.AttendanceTab

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.DialogFragments.AddNewWorkforceDialogFragment
import com.csite.app.DialogFragments.PartySelectionLibraryDialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalAttendance
import com.csite.app.Objects.Party
import com.csite.app.Objects.ProjectWorker
import com.csite.app.Objects.Workforce
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.WorkerSelectionListAdapter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class AddWorkersToProjectActivity : AppCompatActivity(), PartySelectionLibraryDialogFragment.OnPartySelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workers_to_project)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val projectId = intent.getStringExtra("projectId")

        val workerPartyInput: EditText = findViewById(R.id.workerPartyInput)
        workerPartyInput.setOnClickListener{
            val partySelectionLibraryDialogFragment = PartySelectionLibraryDialogFragment()
            partySelectionLibraryDialogFragment.show(supportFragmentManager, "partySelectionLibraryDialogFragment")
        }

        val newWorkerButton = findViewById<ExtendedFloatingActionButton>(R.id.newWorkerButton)
        newWorkerButton.setOnClickListener{
            val addNewWorkforceDialogFragment = AddNewWorkforceDialogFragment()
            addNewWorkforceDialogFragment.show(supportFragmentManager, "AddNewWorkforceDialogFragment")
        }

        val workersRecyclerView = findViewById<RecyclerView>(R.id.workerSelectionList)
        val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        lateinit var receivedWorkforceList: ArrayList<Workforce>
        lateinit var workerSelectionListAdapter :WorkerSelectionListAdapter

        val firebaseOperationsForProjectInternalAttendance = FirebaseOperationsForProjectInternalAttendance()
        if (projectId != null) {
            firebaseOperationsForProjectInternalAttendance.fetchProjectWorkers(
                projectId,
                object : FirebaseOperationsForProjectInternalAttendance.OnProjectWorkersFetched {
                    override fun onProjectWorkersFetched(workersList: ArrayList<ProjectWorker>) {
                        var projectWorkerList = workersList
                        firebaseOperationsForLibrary.fetchWorkforceFromWorkforceLibrary(object :
                            FirebaseOperationsForLibrary.onWorkforceListReceived {
                            override fun onWorkforceListReceived(workforceList: ArrayList<Workforce>) {
                                receivedWorkforceList = workforceList
                                var workerList = ArrayList<ProjectWorker>()
                                for(workforce in workforceList){
                                    var found = false
                                    for(projectWorker in projectWorkerList){
                                        if(workforce.workforceId == projectWorker.wId){
                                            workerList.add(projectWorker)
                                            found = true
                                            break
                                        }
                                    }
                                    if(!found) {
                                        workerList.add(
                                            ProjectWorker(
                                                "",
                                                workforce.workforceType,
                                                workforce.workforceCategory,
                                                "",
                                                "",
                                                false,
                                                false,
                                                "",
                                                "",
                                                false,
                                                false,
                                                "",
                                                "",
                                                "",
                                                workforce.workforceSalaryPerShift,
                                                workforce.workforceId,
                                                false
                                            )
                                        )
                                    }

                                    workerSelectionListAdapter = WorkerSelectionListAdapter(workerList)
                                    workersRecyclerView.adapter = workerSelectionListAdapter

                                }
                                workerSelectionListAdapter = WorkerSelectionListAdapter(workerList)
                                workersRecyclerView.adapter = workerSelectionListAdapter
                            }
                        })
                    }
                })
        }

        workersRecyclerView.layoutManager = LinearLayoutManager(this)

        val saveWorkersButton = findViewById<View>(R.id.saveWorkersButton)
        saveWorkersButton.setOnClickListener{
            val workerPartyInput = findViewById<EditText>(R.id.workerPartyInput)
            val workerParty = workerPartyInput.text.toString()
            var selectedWorkers = workerSelectionListAdapter.getSelectedWorkers()
            for(worker in selectedWorkers){
                worker.value.wParty = workerParty
            }
            if (selectedWorkers.size > 0 && workerParty.isNotEmpty()) {
                val projectId = intent.getStringExtra("projectId")
                val firebaseOperationsForProjectInternalAttendance = FirebaseOperationsForProjectInternalAttendance()
                if (projectId != null) {
                    firebaseOperationsForProjectInternalAttendance.addWorkersToProject(projectId, selectedWorkers)
                }else{
                    workerPartyInput.error = "Project Id not found"
                }
                finish()
            }else{
                workerPartyInput.error = "Please enter a party and select workers"
                Toast.makeText(this, "Please enter a party and select workers", Toast.LENGTH_SHORT).show()
                workerPartyInput.requestFocus()
            }

        }


    }

    fun backButton(view: View){
        finish()
    }

    override fun onPartySelected(party: Party?) {
        val workerPartyInput = findViewById<EditText>(R.id.workerPartyInput)
        workerPartyInput.setText(party?.partyName)
    }
}