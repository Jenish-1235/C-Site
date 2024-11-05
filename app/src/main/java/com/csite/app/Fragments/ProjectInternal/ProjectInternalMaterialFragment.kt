package com.csite.app.Fragments.ProjectInternal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.csite.app.Activites.ProjectFeatures.MaterialTab.NewMaterialReceivedActivity
import com.csite.app.Activites.ProjectFeatures.MaterialTab.NewMaterialRequestActivity
import com.csite.app.R


class ProjectInternalMaterialFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_project_internal_material, container, false)

        val bundle = getArguments()
        var projectId = bundle?.getString("projectId")
        val memberAccess = bundle?.getString("memberAccess")

        val materialRequestButton = view.findViewById<TextView>(R.id.materialRequestButton)
        materialRequestButton.setOnClickListener {
            val newMaterialRequestIntent = Intent(activity, NewMaterialRequestActivity::class.java)
            newMaterialRequestIntent.putExtra("projectId",projectId)
            startActivity(newMaterialRequestIntent)
        }

        val materialReceivedButton = view.findViewById<TextView>(R.id.materialReceivedButton)
        materialReceivedButton.setOnClickListener {
            val newMaterialReceivedIntent = Intent(activity, NewMaterialReceivedActivity::class.java)
            newMaterialReceivedIntent.putExtra("projectId",projectId)
            startActivity(newMaterialReceivedIntent)
        }

        return view
    }
}