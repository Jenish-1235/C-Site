package com.csite.app.Fragments.ProjectInternal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
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

        formTabLayout(view,projectId!!)
        return view
    }

    fun formTabLayout(view: View,projectId:String){
        val tabLayout = view.findViewById<TabLayout>(R.id.materialTabLLayout)

        val requestTab = tabLayout.newTab()
        requestTab.setText("Request")
        tabLayout.addTab(requestTab)

        val receivedTab = tabLayout.newTab()
        receivedTab.setText("Received")
        tabLayout.addTab(receivedTab)

        val tabLayoutAddOnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val materialTabRecyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.materialTabRecyclerView)
                materialTabRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                val list = ArrayList<MaterialRequestOrReceived>()
                val firebaseOperationsForProjectInternalMaterialTab = FirebaseOperationsForProjectInternalMaterialTab()

                if(tab?.getPosition() == 0) {
                    firebaseOperationsForProjectInternalMaterialTab.fetchMaterialRequests(projectId,
                        object :
                            FirebaseOperationsForProjectInternalMaterialTab.OnMaterialRequestReceived {
                            override fun onMaterialRequestReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                                val adapter = MaterialTabListAdapter(materialRequestList)
                                materialTabRecyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                        })
                }
                else if(tab?.getPosition() == 1){
                    firebaseOperationsForProjectInternalMaterialTab.fetchMaterialReceived(projectId,
                        object : FirebaseOperationsForProjectInternalMaterialTab.OnMaterialReceivedReceived {
                            override fun onMaterialReceivedReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                                val adapter = MaterialTabListAdapter(materialRequestList)
                                materialTabRecyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                        })
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val materialTabRecyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.materialTabRecyclerView)
                materialTabRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                val list = ArrayList<MaterialRequestOrReceived>()
                val firebaseOperationsForProjectInternalMaterialTab = FirebaseOperationsForProjectInternalMaterialTab()

                if(tab?.getPosition() == 0) {
                    firebaseOperationsForProjectInternalMaterialTab.fetchMaterialRequests(projectId,
                        object :
                            FirebaseOperationsForProjectInternalMaterialTab.OnMaterialRequestReceived {
                            override fun onMaterialRequestReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                                val adapter = MaterialTabListAdapter(materialRequestList)
                                materialTabRecyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                        })
                }
                else if(tab?.getPosition() == 1){
                    firebaseOperationsForProjectInternalMaterialTab.fetchMaterialReceived(projectId,
                        object : FirebaseOperationsForProjectInternalMaterialTab.OnMaterialReceivedReceived {
                            override fun onMaterialReceivedReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                                val adapter = MaterialTabListAdapter(materialRequestList)
                                materialTabRecyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                        })
                }
            }

        }
        tabLayout.addOnTabSelectedListener(tabLayoutAddOnTabSelectedListener)
        tabLayout.getTabAt(0)?.select()
        requestTab.select()

    }
}