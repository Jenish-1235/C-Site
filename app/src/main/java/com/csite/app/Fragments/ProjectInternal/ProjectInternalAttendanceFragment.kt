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
                firebaseOperationsForProjectInternalAttendance.getCalculatedValues(projectId, selectedDateTextView.text.toString(), object : FirebaseOperationsForProjectInternalAttendance.OnCalculatedValuesFetched{
                    override fun onCalculatedValuesFetched(calculatedHashMap: HashMap<String, String>) {
                        val presentCountView = view.findViewById<TextView>(R.id.presentCountView)
                        presentCountView.text = calculatedHashMap.get("totalPresent")
                        val absentCountView = view.findViewById<TextView>(R.id.absentCountView)
                        absentCountView.text = calculatedHashMap.get("totalAbsent")
                        val salaryView = view.findViewById<TextView>(R.id.totalSalaryView)
                        salaryView.text = calculatedHashMap.get("totalSalary")
                    }
                })
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
                    firebaseOperationsForProjectInternalAttendance.getCalculatedValues(projectId, selectedDateTextView.text.toString(), object : FirebaseOperationsForProjectInternalAttendance.OnCalculatedValuesFetched{
                        override fun onCalculatedValuesFetched(calculatedHashMap: HashMap<String, String>) {
                            val presentCountView = view.findViewById<TextView>(R.id.presentCountView)
                            presentCountView.text = calculatedHashMap.get("totalPresent")
                            val absentCountView = view.findViewById<TextView>(R.id.absentCountView)
                            absentCountView.text = calculatedHashMap.get("totalAbsent")
                            val salaryView = view.findViewById<TextView>(R.id.totalSalaryView)
                            salaryView.text = calculatedHashMap.get("totalSalary")
                        }
                    })
                }
                if (projectId != null) {

                    updateUI(view,projectId)
                }
            }else{
                if (projectId != null) {
                    firebaseOperationsForProjectInternalAttendance.getCalculatedValues(projectId, selectedDateTextView.text.toString(), object : FirebaseOperationsForProjectInternalAttendance.OnCalculatedValuesFetched{
                        override fun onCalculatedValuesFetched(calculatedHashMap: HashMap<String, String>) {
                            val presentCountView = view.findViewById<TextView>(R.id.presentCountView)
                            presentCountView.text = calculatedHashMap.get("totalPresent")
                            val absentCountView = view.findViewById<TextView>(R.id.absentCountView)
                            absentCountView.text = calculatedHashMap.get("totalAbsent")
                            val salaryView = view.findViewById<TextView>(R.id.totalSalaryView)
                            salaryView.text = calculatedHashMap.get("totalSalary")
                        }
                    })
                }
                if (projectId != null) {
                    updateUI(view, projectId)
                }
            }
        }

        if (projectId != null) {
            firebaseOperationsForProjectInternalAttendance.getCalculatedValues(projectId, selectedDateTextView.text.toString(), object : FirebaseOperationsForProjectInternalAttendance.OnCalculatedValuesFetched{
                override fun onCalculatedValuesFetched(calculatedHashMap: HashMap<String, String>) {
                    if (calculatedHashMap.get("totalPresent") != null) {
                        val presentCountView = view.findViewById<TextView>(R.id.presentCountView)
                        presentCountView.text = calculatedHashMap.get("totalPresent")
                        val absentCountView = view.findViewById<TextView>(R.id.absentCountView)
                        absentCountView.text = calculatedHashMap.get("totalAbsent")
                        val salaryView = view.findViewById<TextView>(R.id.totalSalaryView)
                        salaryView.text = "\u20b9 " +calculatedHashMap.get("totalSalary")
                    }else{
                        val presentCountView = view.findViewById<TextView>(R.id.presentCountView)
                        presentCountView.text = "0"
                        val absentCountView = view.findViewById<TextView>(R.id.absentCountView)
                        absentCountView.text = "0"
                        val salaryView = view.findViewById<TextView>(R.id.totalSalaryView)
                        salaryView.text = "\u20b9 0.0"
                    }
                }
            })
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
                                            val saveAttendanceButton = view.findViewById<Button>(R.id.saveAttendanceButton)
                                            saveAttendanceButton.setOnClickListener{
                                                var count = 0
                                                var salary = 0.0
//                                                Toast.makeText(requireActivity(), adapter.getAttendanceList().size.toString(), Toast.LENGTH_SHORT).show()
                                                for(worker in adapter.getAttendanceList()){
                                                    if (worker.value.wIsPresent.equals("true")){
                                                        count++
                                                        salary += worker.value.wSalaryPerDay.toDouble()
                                                    }else{
                                                        salary += 0
                                                    }
                                                }
                                                val presentCountView = view.findViewById<TextView>(R.id.presentCountView)
                                                presentCountView.text = count.toString()
                                                val absentCountView = view.findViewById<TextView>(R.id.absentCountView)
                                                absentCountView.text = (adapter.getAttendanceList().size - count).toString()
                                                val salaryView = view.findViewById<TextView>(R.id.totalSalaryView)
                                                salaryView.text = salary.toString()

                                                firebaseOperationsForProjectInternalAttendance.updateAttendance(projectId,selectedDateTextView.text.toString(),adapter.getAttendanceList())

                                                var calculatedHashMap : HashMap<String, String> = HashMap()
                                                calculatedHashMap.put("totalPresent",count.toString())
                                                calculatedHashMap.put("totalAbsent",(adapter.getAttendanceList().size - count).toString())
                                                calculatedHashMap.put("totalSalary",salary.toString())

                                                var calculatedHashMapWithDate = HashMap<String,HashMap<String,String>>()
                                                calculatedHashMapWithDate.put(selectedDateTextView.text.toString(),calculatedHashMap)
                                                var calculationHashMapWithChild = HashMap<String,HashMap<String,HashMap<String,String>>>()
                                                calculationHashMapWithChild.put("AttendanceCalculation",calculatedHashMapWithDate)
                                                firebaseOperationsForProjectInternalAttendance.checkCalculationExists(projectId) { calculatedExist ->
                                                    if (calculatedExist) {
                                                        firebaseOperationsForProjectInternalAttendance.saveOrUpdateCalculation(
                                                            projectId,
                                                            calculatedHashMapWithDate,0
                                                        )
                                                    } else {
                                                        firebaseOperationsForProjectInternalAttendance.saveOrUpdateCalculation(
                                                            projectId,
                                                            calculatedHashMapWithDate,1
                                                        )
                                                    }
                                                }

                                            }
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