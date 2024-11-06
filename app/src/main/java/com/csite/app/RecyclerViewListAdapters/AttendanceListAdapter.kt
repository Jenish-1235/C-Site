package com.csite.app.RecyclerViewListAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.ProjectWorker
import com.csite.app.R
import java.util.ArrayList

class AttendanceListAdapter(workerList: ArrayList<ProjectWorker>): RecyclerView.Adapter<AttendanceListAdapter.AttendanceViewHolder>(){
    var workerList = workerList
    var updatedAttendanceList: HashMap<String, ProjectWorker> = HashMap()


    class AttendanceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val workerName = itemView.findViewById<TextView>(R.id.workerNameView)
        val workerPartyView = itemView.findViewById<TextView>(R.id.workerPartyView)
        val workerCategoryView = itemView.findViewById<TextView>(R.id.workerCategoryView)
        val workerAbsentView = itemView.findViewById<TextView>(R.id.workerAbsentButton)
        val workerMoreOptionsView = itemView.findViewById<ImageView>(R.id.workerMoreOptionButton)
        val workerSalaryView = itemView.findViewById<TextView>(R.id.workerSalaryPerDay)
        val workerPresentView = itemView.findViewById<TextView>(R.id.workerPresentButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_attendance_view, parent, false)
        return AttendanceViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return workerList.size
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val worker = workerList[position]
        holder.workerName.text = worker.wName
        holder.workerPartyView.text = worker.wParty
        holder.workerCategoryView.text = worker.wCategory
        holder.workerSalaryView.text = "\u20b9 " + worker.wSalaryPerDay.toString()

        for (worker in workerList) {
            updatedAttendanceList[worker.wId] = worker
        }
        for (worker in workerList) {
            updatedAttendanceList.put(worker.wId, worker)
        }

        if (worker.wIsPresent.equals("true")){
            holder.workerPresentView.backgroundTintList =
                holder.itemView.context.resources.getColorStateList(R.color.green)
            holder.workerAbsentView.backgroundTintList =
                holder.itemView.context.resources.getColorStateList(R.color.skin)
            updatedAttendanceList[worker.wId]!!.wIsPresent = "true"
        }else if (worker.wIsPresent.equals("false")){
            holder.workerPresentView.backgroundTintList =
                holder.itemView.context.resources.getColorStateList(R.color.skin)
            holder.workerAbsentView.backgroundTintList =
                holder.itemView.context.resources.getColorStateList(R.color.red)
            updatedAttendanceList.get(worker.wId)!!.wIsPresent = "false"
        }


        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, worker.wName, Toast.LENGTH_SHORT).show()
        }

        holder.workerMoreOptionsView.setOnClickListener {}

        holder.workerAbsentView.setOnClickListener {
            holder.workerPresentView.backgroundTintList =
                holder.itemView.context.resources.getColorStateList(R.color.skin)
            holder.workerAbsentView.backgroundTintList =
                holder.itemView.context.resources.getColorStateList(R.color.red)
            updatedAttendanceList[worker.wId]!!.wIsPresent = "false"

        }
        holder.workerPresentView.setOnClickListener {
            holder.workerPresentView.backgroundTintList =
                holder.itemView.context.resources.getColorStateList(R.color.green)
            holder.workerAbsentView.backgroundTintList =
                holder.itemView.context.resources.getColorStateList(R.color.skin)
            updatedAttendanceList[worker.wId]!!.wIsPresent = "true"
        }
    }
    fun getAttendanceList(): HashMap<String, ProjectWorker> {
        return updatedAttendanceList
    }

}