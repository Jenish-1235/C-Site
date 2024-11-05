package com.csite.app.Fragments.ProjectInternal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.csite.app.Activites.ProjectFeatures.MaterialTab.NewMaterialReceivedActivity
import com.csite.app.Activites.ProjectFeatures.MaterialTab.NewMaterialRequestActivity
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalMaterialTab
import com.csite.app.Objects.MaterialRequestOrReceived
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.MaterialTabListAdapter
import com.google.android.material.tabs.TabLayout


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


        val firebaseOperationsForProjectInternalMaterialTab = FirebaseOperationsForProjectInternalMaterialTab()
        val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.materialTabRecyclerView)
        val tabLayout = view.findViewById<TabLayout>(R.id.materialTabLLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0->{
                        if (projectId != null) {
                            firebaseOperationsForProjectInternalMaterialTab.fetchMaterialRequests(projectId,
                                object : FirebaseOperationsForProjectInternalMaterialTab.OnMaterialRequestReceived {
                                    override fun onMaterialRequestReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                                        var adapter = MaterialTabListAdapter(materialRequestList)
                                        recyclerView.adapter = adapter
                                        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                                        adapter.notifyDataSetChanged()
                                    }

                                })
                        }else{
                            Toast.makeText(activity,"Project Id is null",Toast.LENGTH_SHORT).show()
                        }
                    }
                    1-> {
                        if (projectId != null) {
                            firebaseOperationsForProjectInternalMaterialTab.fetchMaterialReceived(projectId,
                                object : FirebaseOperationsForProjectInternalMaterialTab.OnMaterialReceivedReceived {
                                    override fun onMaterialReceivedReceived(materialReceivedList: ArrayList<MaterialRequestOrReceived>) {
                                        var adapter = MaterialTabListAdapter(materialReceivedList)
                                        recyclerView.adapter = adapter
                                        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                                        adapter.notifyDataSetChanged()
                                    }
                                })
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0->{
                        if (projectId != null) {
                            firebaseOperationsForProjectInternalMaterialTab.fetchMaterialRequests(projectId,
                                object : FirebaseOperationsForProjectInternalMaterialTab.OnMaterialRequestReceived {
                                    override fun onMaterialRequestReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                                        var adapter = MaterialTabListAdapter(materialRequestList)
                                        recyclerView.adapter = adapter
                                        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                                        adapter.notifyDataSetChanged()
                                    }

                                })
                        }else{
                            Toast.makeText(activity,"Project Id is null",Toast.LENGTH_SHORT).show()
                        }
                    }
                    1-> {
                        if (projectId != null) {
                            firebaseOperationsForProjectInternalMaterialTab.fetchMaterialReceived(projectId,
                                object : FirebaseOperationsForProjectInternalMaterialTab.OnMaterialReceivedReceived {
                                    override fun onMaterialReceivedReceived(materialReceivedList: ArrayList<MaterialRequestOrReceived>) {
                                        var adapter = MaterialTabListAdapter(materialReceivedList)
                                        recyclerView.adapter = adapter
                                        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                                        adapter.notifyDataSetChanged()
                                    }
                                })
                        }
                    }
                }
            }


        })

        tabLayout.selectTab(tabLayout.getTabAt(0))



        return view
    }
}