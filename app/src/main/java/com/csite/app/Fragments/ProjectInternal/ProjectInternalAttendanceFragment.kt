package com.csite.app.Fragments.ProjectInternal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csite.app.Activites.ProjectFeatures.ContractorSelectionActivity
import com.csite.app.R
import com.csite.app.databinding.FragmentProjectInternalAttendanceBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

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
        }

        b.currentSelectedDateView.text = getDateTimeWithTime()

        b.tomorrowDateButton.setOnClickListener{
            val currentSelectedDate = b.currentSelectedDateView.text
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDate.parse(currentSelectedDate, formatter)
            val tomorrow = date.plusDays(1)
            b.currentSelectedDateView.text =
                formatter.format(tomorrow).toString()
        }

        b.addContractorButton.setOnClickListener{
            val contractorSelectionIntent = Intent(context, ContractorSelectionActivity::class.java)
            contractorSelectionIntent.putExtra("projectId", projectId)
            contractorSelectionIntent.putExtra("currentDate", b.currentSelectedDateView.text.toString())
            startActivity(contractorSelectionIntent)
        }

        return view
    }

    fun getDateTimeWithTime(): String {
        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        return formatter.format(date)
    }
}