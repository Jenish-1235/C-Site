package com.csite.app.Activites.ProjectFeatures

import android.content.res.ColorStateList
import android.os.Binder
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.DialogFragments.AddNewProjectMemberDialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjects
import com.csite.app.Objects.Member
import com.csite.app.Objects.Project
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.ProjectMembersListAdapter
import com.csite.app.databinding.ActivityProjectSettingsBinding
import com.google.firebase.database.FirebaseDatabase

class ProjectSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b: ActivityProjectSettingsBinding = ActivityProjectSettingsBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val mobileNumberPreferences = getSharedPreferences("mobileNumber", MODE_PRIVATE)
        val mobileNumber = mobileNumberPreferences.getString("mobileNumber", "")

        val intent = intent
        var projectId = intent.getStringExtra("projectId")
        var projectName = intent.getStringExtra("projectName")
        var projectStatus = intent.getStringExtra("projectStatus")
        var projectStartDate = intent.getStringExtra("projectStartDate")
        var projectEndDate = intent.getStringExtra("projectEndDate")
        var projectLocation = intent.getStringExtra("projectLocation")
        var projectCity = intent.getStringExtra("projectCity")
        var projectValue = intent.getStringExtra("projectValue")

        b.projectNameView.text = "Name: " +  projectName
        b.projectValueView.text = "Value: \u20b9 " + projectValue
        b.startDateView.text = "Start Date: " + projectStartDate
        b.endDateView.text = "End Date: " + projectEndDate
        b.projectLocationView.text = "Location: " + projectLocation + ", " + projectCity


        if(projectStatus == "Active"){
            b.projectStatusSwitch.isChecked = true
            b.projectStatusSwitch.text = "Active"
            b.projectStatusSwitch.setThumbTintList(ColorStateList.valueOf(resources.getColor(R.color.green)))
            b.projectStatusSwitch.setTrackTintList(ColorStateList.valueOf(resources.getColor(R.color.golden)))
        }else{
            b.projectStatusSwitch.isChecked = false
            b.projectStatusSwitch.setThumbTintList(ColorStateList.valueOf(resources.getColor(R.color.red)))
            b.projectStatusSwitch.setTrackTintList(ColorStateList.valueOf(resources.getColor(R.color.golden)))
            b.projectStatusSwitch.text = "Completed"
        }

        b.projectStatusSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                b.projectStatusSwitch.text = "Active"
                val firebaseOperationsForProjects = FirebaseOperationsForProjects()
                if (projectId != null) {
                    firebaseOperationsForProjects.updateProjectStatus(projectId, "Active")
                }
                b.projectStatusSwitch.setThumbTintList(ColorStateList.valueOf(resources.getColor(R.color.green)))
                b.projectStatusSwitch.setTrackTintList(ColorStateList.valueOf(resources.getColor(R.color.golden)))
            }else{
                b.projectStatusSwitch.text = "Completed"
                val firebaseOperationsForProjects = FirebaseOperationsForProjects()
                if (projectId != null) {
                    firebaseOperationsForProjects.updateProjectStatus(projectId, "Completed")
                }
                b.projectStatusSwitch.setThumbTintList(ColorStateList.valueOf(resources.getColor(R.color.red)))
                b.projectStatusSwitch.setTrackTintList(ColorStateList.valueOf(resources.getColor(R.color.golden)))
            }
        }

        b.addMembersButton.setOnClickListener {
            val addNewProjectMemberDialogFragment = AddNewProjectMemberDialogFragment()
            addNewProjectMemberDialogFragment.show(supportFragmentManager, "add new member")
        }

        b.leaveProjectButton.setOnClickListener {
            val firebaseOperationsForProjects = FirebaseOperationsForProjects()
            if (projectId != null) {
                if (mobileNumber != null) {
                    firebaseOperationsForProjects.leaveProject(projectId, mobileNumber)
                }else{
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
        }
        b.leaveProject.setOnClickListener{
            val firebaseOperationsForProjects = FirebaseOperationsForProjects()
            if (projectId != null) {
                if (mobileNumber != null) {
                    firebaseOperationsForProjects.leaveProject(projectId, mobileNumber)
                }else{
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
        }

        val projectMembersList = mutableListOf<Member>()
        b.membersRecyclerView.adapter = ProjectMembersListAdapter(this, projectMembersList)
        b.membersRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val firebaseOperationsForProjects = FirebaseOperationsForProjects()
        if (projectId != null) {
            firebaseOperationsForProjects.getProjectMembers(
                projectId,
                callback = fun(members: List<Member>) {
                    projectMembersList.clear()
                    projectMembersList.addAll(members)
                    b.membersRecyclerView.adapter?.notifyDataSetChanged()
                }
            )
        }

    }


    fun backButton(view: View){
        finish()
    }

}