package com.csite.app.Fragments.ProjectInternal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Activites.ProjectFeatures.AttendanceTab.ContractorSelectionActivity
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalAttendanceTab
import com.csite.app.Objects.Contractor
import com.csite.app.Objects.Workforce
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.AttendanceTabListAdapter
import com.csite.app.databinding.FragmentProjectInternalAttendanceBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.collections.HashMap

class ProjectInternalAttendanceFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_project_internal_attendance, container, false)
        val b = FragmentProjectInternalAttendanceBinding.bind(view)

        val bundle = arguments
        val projectName = bundle?.getString("projectName")
        val projectId = bundle?.getString("projectId")


        b.yesterdayDateButton.setOnClickListener{
            val currentSelectedDate = b.currentSelectedDateView.text
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDate.parse(currentSelectedDate, formatter)
            val yesterday = date.minusDays(1)
            b.currentSelectedDateView.text = formatter.format(yesterday).toString()
            updateUI(view, projectId.toString(), b.currentSelectedDateView.text.toString())

        }

        b.currentSelectedDateView.text = getDateTimeWithTime()

        b.tomorrowDateButton.setOnClickListener{
            val currentSelectedDate = b.currentSelectedDateView.text
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDate.parse(currentSelectedDate, formatter)
            val tomorrow = date.plusDays(1)
            b.currentSelectedDateView.text =
                formatter.format(tomorrow).toString()
            updateUI(view, projectId.toString(), b.currentSelectedDateView.text.toString())
        }

        b.addContractorButton.setOnClickListener{
            val contractorSelectionIntent = Intent(context, ContractorSelectionActivity::class.java)
            contractorSelectionIntent.putExtra("projectId", projectId)
            contractorSelectionIntent.putExtra("currentDate", b.currentSelectedDateView.text.toString())
            startActivity(contractorSelectionIntent)
        }

        updateUI(view, projectId.toString(), b.currentSelectedDateView.text.toString())
        return view
    }

    fun getDateTimeWithTime(): String {
        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        return formatter.format(date)
    }

    fun updateUI(view:View, projectId: String, currentDate:String){
        val firebaseOperationsForProjectInternalAttendanceTab = FirebaseOperationsForProjectInternalAttendanceTab()
        firebaseOperationsForProjectInternalAttendanceTab.fetchContractorListFromAttendance(projectId, currentDate, object :FirebaseOperationsForProjectInternalAttendanceTab.getAttendanceContractorList{
            override fun onAttendanceContractorListReceived(contractorList: HashMap<String, Contractor>) {

                val workforceHashMap = HashMap<String, ArrayList<Workforce>>()
                var size = 0
                for (contractor in contractorList){
                    val workforceList = ArrayList<Workforce>()
                    for (workforce in contractor.value.contractorWorkforce.values){
                        workforceList.add(workforce)
                        size++
                    }
                    workforceHashMap[contractor.value.contractorName] = workforceList
                }

                val attendanceTabRecyclerView = view.findViewById<RecyclerView>(R.id.attendanceListRecyclerView)
                val attendanceTabAdapter = AttendanceTabListAdapter(workforceHashMap, currentDate, size)
                attendanceTabRecyclerView.adapter = attendanceTabAdapter
                attendanceTabRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                attendanceTabAdapter.notifyDataSetChanged()

            }

        })
    }
}