package com.csite.app.RecyclerViewListAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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
            Toast.makeText(context, "Settings button clicked for " + project.projectName, Toast.LENGTH_SHORT).show()
        }

        holder.projectMoreInfoButton.setOnClickListener {
            Toast.makeText(context, "More info button clicked", Toast.LENGTH_SHORT).show()
        }

    }

    // Get number of items in list
    override fun getItemCount(): Int {
        return projectList!!.size
    }

    // Create interface for click listener
    private var listener: OnItemClickListener? = null
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    // Set click listener
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

}