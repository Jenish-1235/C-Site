package com.csite.app.RecyclerViewListAdapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.csite.app.Activites.ProjectFeatures.ProjectInternalMainActivity
import com.csite.app.Activites.ProjectFeatures.ProjectSettingsActivity
import com.csite.app.Objects.Project
import com.csite.app.R

class ProjectListAdapter(context: Context, projectList: List<Project>): RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder>(){

    // Declare variables
    var context: Context = context
    var projectList: List<Project>? = projectList


    // Create ViewHolder
    class ProjectViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val projectNameTextView: TextView = itemView.findViewById(R.id.projectNameTextView)
        val projectLocationTextView: TextView = itemView.findViewById(R.id.projectLocationTextView)
        val projectSettingsButton: Button = itemView.findViewById(R.id.projectSettingsButton)
        val projectMoreInfoButton: Button = itemView.findViewById(R.id.projectMoreInfoButton)


    }

    // Inflate layout for list item
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ProjectListAdapter.ProjectViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.list_item_project, parent, false)
        return ProjectViewHolder(itemView)
    }

    // Bind data to list item
    override fun onBindViewHolder(holder: ProjectListAdapter.ProjectViewHolder, position: Int) {

        val project: Project = projectList!![position]
        holder.projectNameTextView.text = project.projectName
        holder.projectLocationTextView.text = project.projectCity

        holder.projectSettingsButton.setOnClickListener {
//            Toast.makeText(context, "Settings button clicked for " + project.projectName, Toast.LENGTH_SHORT).show()
            val projectSettingsIntent = Intent(context, ProjectSettingsActivity::class.java)
            projectSettingsIntent.putExtra("projectName", project.projectName)
            projectSettingsIntent.putExtra("projectId", project.projectId)
            context.startActivity(projectSettingsIntent)
        }

        holder.projectMoreInfoButton.setOnClickListener {
//            Toast.makeText(context, "More info button clicked", Toast.LENGTH_SHORT).show()
            val projectInternalMainActivityIntent = Intent(context, ProjectInternalMainActivity::class.java)
            projectInternalMainActivityIntent.putExtra("projectName", project.projectName)
            projectInternalMainActivityIntent.putExtra("projectId", project.projectId)
            context.startActivity(projectInternalMainActivityIntent)

        }

        holder.itemView.setOnClickListener{
//            Toast.makeText(context, "Clicked " + project.projectName, Toast.LENGTH_SHORT).show()
            val projectInternalMainActivityIntent = Intent(context, ProjectInternalMainActivity::class.java)
            projectInternalMainActivityIntent.putExtra("projectName", project.projectName)
            projectInternalMainActivityIntent.putExtra("projectId", project.projectId)
            context.startActivity(projectInternalMainActivityIntent)
        }

    }

    // Get number of items in list
    override fun getItemCount(): Int {
        return projectList!!.size
    }


}