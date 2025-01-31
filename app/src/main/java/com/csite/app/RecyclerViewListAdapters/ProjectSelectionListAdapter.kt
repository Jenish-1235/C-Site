package com.csite.app.RecyclerViewListAdapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Activites.ProjectFeatures.ProjectInternalMainActivity
import com.csite.app.Activites.ProjectFeatures.ProjectSettingsActivity
import com.csite.app.Objects.Party
import com.csite.app.Objects.Project
import com.csite.app.R

class ProjectSelectionListAdapter(projectList: List<Project>): RecyclerView.Adapter<ProjectSelectionListAdapter.ProjectSelectionViewHolder>() {


    var projectList: List<Project>? = projectList

    class ProjectSelectionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val projectNameTextView: TextView = itemView.findViewById(R.id.projectNameTextView)
        val projectLocationTextView: TextView = itemView.findViewById(R.id.projectLocationTextView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectSelectionListAdapter.ProjectSelectionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_project_selection, parent, false)
        return ProjectSelectionViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ProjectSelectionListAdapter.ProjectSelectionViewHolder,
        position: Int
    ) {
        val project: Project = projectList!![position]
        holder.projectNameTextView.text = project.projectName
        holder.projectLocationTextView.text = project.projectCity

        holder.itemView.setOnClickListener {
            listener?.OnItemClick(project)
        }

    }

    override fun getItemCount(): Int {
        return projectList!!.size
    }

    interface OnItemClickListener {
        fun OnItemClick(project: Project?)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

}