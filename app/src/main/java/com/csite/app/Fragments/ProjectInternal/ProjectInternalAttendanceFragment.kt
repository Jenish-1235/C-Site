package com.csite.app.Fragments.ProjectInternal

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.csite.app.Activites.ProjectFeatures.AttendanceTab.AddWorkersToProjectActivity
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalAttendance
import com.csite.app.Objects.ProjectWorker
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.AttendanceListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.HashMap


class ProjectInternalAttendanceFragment : Fragment() {
    val pref = FirebaseDatabase.getInstance().getReference("Projects")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view =  inflater.inflate(R.layout.fragment_project_internal_attendance, container, false)

        val bundle = getArguments()
        val projectId = bundle?.getString("projectId")
        val memberAccess = bundle?.getString("memberAccess")

        val attendanceRecyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.projectWorkersRecyclerView)
        val emptyWorkerList = ArrayList<ProjectWorker>()
        val adapter = AttendanceListAdapter(emptyWorkerList)
        attendanceRecyclerView.adapter = adapter
        attendanceRecyclerView.layoutManager = LinearLayoutManager(requireActivity())


        val addDayButton = view.findViewById<Button>(R.id.addDayButton)
        addDayButton.text = "Add Day"
        addDayButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.yellow))

        val currentDate = getCurrentDate()
        val selectedDateTextView = view.findViewById<TextView>(R.id.selectedDateView)
        selectedDateTextView.text = currentDate


        val addWorkerButton: FloatingActionButton = view.findViewById(R.id.addWorkersButton)
        addWorkerButton.setOnClickListener{
            val addWorkerToProjectIntent = Intent(activity, AddWorkersToProjectActivity::class.java)
            addWorkerToProjectIntent.putExtra("projectId",projectId)
            addWorkerToProjectIntent.putExtra("memberAccess",memberAccess)
            startActivity(addWorkerToProjectIntent)
        }

        val firebaseOperationsForProjectInternalAttendance = FirebaseOperationsForProjectInternalAttendance()

        val yesterdayDateButton:ImageView = view.findViewById(R.id.yesterdayDateButton)
        yesterdayDateButton.setOnClickListener{
            var selectedDate = selectedDateTextView.text.toString()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDate.parse(selectedDate, formatter)
            val yesterday = date.minusDays(1)
            selectedDateTextView.text = yesterday.format(formatter)
            if (projectId != null) {
                updateUI(view,projectId)
            }

        }

        val tomorrowDateButton:ImageView = view.findViewById(R.id.tomorrowDateButton)
        tomorrowDateButton.setOnClickListener {
            var selectedDate = selectedDateTextView.text.toString()
            if (selectedDate != getCurrentDate()){
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                val date = LocalDate.parse(selectedDate, formatter)
                val tomorrow = date.plusDays(1)
                selectedDateTextView.text = tomorrow.format(formatter)
                if (projectId != null) {
                    updateUI(view,projectId)
                }
            }else{
                if (projectId != null) {
                    updateUI(view, projectId)
                }
            }
        }

        if (projectId != null) {
            updateUI(view, projectId)
        }

        addDayButton.setOnClickListener{
            if (projectId != null) {
                firebaseOperationsForProjectInternalAttendance.checkWorkerExist(projectId){exist->
                    if (exist){
                        addWorkerButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.green))
                        var projectWorkerList = ArrayList<ProjectWorker>()
                        firebaseOperationsForProjectInternalAttendance.fetchProjectWorkers(projectId,
                            object : FirebaseOperationsForProjectInternalAttendance.OnProjectWorkersFetched {
                                override fun onProjectWorkersFetched(workersList: ArrayList<ProjectWorker>) {
                                    projectWorkerList = workersList
                                    firebaseOperationsForProjectInternalAttendance.checkAttendanceExists(projectId){attendanceExist ->
                                        if (attendanceExist){
                                            val attendanceHashMap = HashMap<String, ProjectWorker>()
                                            for (worker in projectWorkerList){
                                                attendanceHashMap.put(worker.wId,worker)
                                            }
                                            firebaseOperationsForProjectInternalAttendance.updateAttendance(projectId,selectedDateTextView.text.toString(),attendanceHashMap)
                                            updateUI(view,projectId)
                                        }else{
                                            val attendanceHashMap = HashMap<String, ProjectWorker>()
                                            for (worker in projectWorkerList){
                                                attendanceHashMap.put(worker.wId,worker)
                                            }
                                            firebaseOperationsForProjectInternalAttendance.saveAttendance(projectId,selectedDateTextView.text.toString(),attendanceHashMap)
                                        }
                                    }
                                }
                            })
                    }else{
                        Toast.makeText(requireActivity(), "No Workers Found", Toast.LENGTH_SHORT).show()
                        val addWorkerToProjectIntent = Intent(activity, AddWorkersToProjectActivity::class.java)
                        addWorkerToProjectIntent.putExtra("projectId",projectId)
                        addWorkerToProjectIntent.putExtra("memberAccess",memberAccess)
                        startActivity(addWorkerToProjectIntent)
                    }
                }
            }
        }


        val saveAttendanceButton:Button = view.findViewById(R.id.saveAttendanceButton)
        saveAttendanceButton.setOnClickListener{
            if (projectId != null) {

            }
        }
        return view
    }


    fun getCurrentDate():String{
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return today.format(formatter)
    }

    fun updateUI(view: View,projectId:String){
        val firebaseOperationsForProjectInternalAttendance = FirebaseOperationsForProjectInternalAttendance()
        val addWorkerButton: FloatingActionButton = view.findViewById(R.id.addWorkersButton)
        val addDayButton = view.findViewById<Button>(R.id.addDayButton)
        val selectedDateTextView = view.findViewById<TextView>(R.id.selectedDateView)
        val attendanceRecyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.projectWorkersRecyclerView)
        attendanceRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val emptyWorkerList = ArrayList<ProjectWorker>()
        val adapter = AttendanceListAdapter(emptyWorkerList)
        attendanceRecyclerView.adapter = adapter

        if (projectId != null) {
            firebaseOperationsForProjectInternalAttendance.checkWorkerExist(projectId){workerExist ->
                if (workerExist){

                    addWorkerButton.setBackgroundColor(getResources().getColor(R.color.green))
                    addWorkerButton.setImageDrawable(getResources().getDrawable(R.drawable.icon_edit))

                    firebaseOperationsForProjectInternalAttendance.checkAttendanceExists(projectId){attendanceExist ->
                        if (attendanceExist){
                            firebaseOperationsForProjectInternalAttendance.checkDateExists(projectId,selectedDateTextView.text.toString()){dateExist ->
                                if (dateExist){
                                    addDayButton.text = "Update Workers"
                                    firebaseOperationsForProjectInternalAttendance.getWorkersForExistingAttendance(projectId, selectedDateTextView.text.toString(), object : FirebaseOperationsForProjectInternalAttendance.OnAttendanceWorkerFetched {
                                        override fun onAttendanceWorkerFetched(workersList: ArrayList<ProjectWorker>) {
                                            val adapter = AttendanceListAdapter(workersList)
                                            attendanceRecyclerView.adapter = adapter
                                            adapter.notifyDataSetChanged()
                                        }
                                    })
                                }else{
                                    addDayButton.text = "Add Day"
                                    addDayButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.yellow))
                                }
                            }
                        }else{
                            addDayButton.text = "Add Day"
                            addDayButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.yellow))
                        }
                    }

                }else{

                    Toast.makeText(requireActivity(), "No Workers Found", Toast.LENGTH_SHORT).show()
                    addDayButton.text = "Add Day"

                    addDayButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.yellow))
                    addWorkerButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.yellow))
                    addWorkerButton.setImageDrawable(getResources().getDrawable(R.drawable.party_icon_black))
                }
            }
        }

    }


}