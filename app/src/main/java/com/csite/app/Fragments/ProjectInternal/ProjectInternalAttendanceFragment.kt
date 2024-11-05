package com.csite.app.Fragments.ProjectInternal

import android.content.Intent
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.csite.app.Activites.ProjectFeatures.AttendanceTab.AddWorkersToProjectActivity
import com.csite.app.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date


class ProjectInternalAttendanceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_project_internal_attendance, container, false)

        val bundle = getArguments()
        var projectId = bundle?.getString("projectId")
        val memberAccess = bundle?.getString("memberAccess")

        val currentDate = getCurrentDate()
        val selectedDateTextView = view.findViewById<TextView>(R.id.selectedDateView)
        selectedDateTextView.text = currentDate

        val yesterdayDateButton:ImageView = view.findViewById(R.id.yesterdayDateButton)
        yesterdayDateButton.setOnClickListener{
            var selectedDate = selectedDateTextView.text.toString()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDate.parse(selectedDate, formatter)
            val yesterday = date.minusDays(1)
            selectedDateTextView.text = yesterday.format(formatter)
        }

        val tomorrowDateButton:ImageView = view.findViewById(R.id.tomorrowDateButton)
        tomorrowDateButton.setOnClickListener {
            var selectedDate = selectedDateTextView.text.toString()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDate.parse(selectedDate, formatter)
            val tomorrow = date.plusDays(1)
            selectedDateTextView.text = tomorrow.format(formatter)
        }


        val addWorkerButton: Button = view.findViewById(R.id.addWorkersButton)
        addWorkerButton.setOnClickListener{
            val addWorkerToProjectIntent = Intent(activity, AddWorkersToProjectActivity::class.java)
            addWorkerToProjectIntent.putExtra("projectId",projectId)
            addWorkerToProjectIntent.putExtra("memberAccess",memberAccess)
            startActivity(addWorkerToProjectIntent)
        }



        return view
    }


    fun getCurrentDate():String{
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return today.format(formatter)
    }


}