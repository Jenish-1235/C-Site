package com.csite.app.RecyclerViewListAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Objects.Member
import com.csite.app.R

class ProjectMembersListAdapter(context: Context, members: List<Member>): RecyclerView.Adapter<ProjectMembersListAdapter.MemberViewHolder>() {
    private val mContext = context
    private val mMembers = members

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memberName = itemView.findViewById<TextView>(R.id.memberNameView)
        val memberRole = itemView.findViewById<TextView>(R.id.accessView)
        val memberNumber = itemView.findViewById<TextView>(R.id.phoneNumberView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectMembersListAdapter.MemberViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_project_members, parent, false)
        return MemberViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ProjectMembersListAdapter.MemberViewHolder,
        position: Int
    ) {
        val member = mMembers[position]
        holder.memberName.text = member.name
        holder.memberRole.text = member.memberAccess
        holder.memberNumber.text = member.mobileNumber

        holder.itemView.setOnClickListener {
         // give option to change the role of member
        }
    }

    override fun getItemCount(): Int {
        return mMembers.size
    }


}