package com.csite.app.Fragments.MainScreen

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Utilities.SliderTabLayout
import com.csite.app.R
import com.csite.app.Activites.Library.MainLibraryActivity
import com.csite.app.Activites.BankTransfer.BankTransferActivity
import com.csite.app.Activites.MainScreen.MaterialActivity
import com.csite.app.DialogFragments.AddNewProjectDialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjects
import com.csite.app.Objects.Project
import com.csite.app.RecyclerViewListAdapters.ProjectListAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ProjectFragment : Fragment() {

    // Firebase References
    val projectReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Projects")
    val libraryReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Library")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_project, container, false)

        // Get memberAccess and check if it is manager or not
        val memberAccess: SharedPreferences = requireActivity().getSharedPreferences("memberAccess", MODE_PRIVATE)
        val memberAccessValue = memberAccess.getString("memberAccess", "")
        if (memberAccessValue == "manager") {
            tabLayoutForManager(view)
            val addProjectButton: Button = view.findViewById(R.id.addProjectButton)
            addProjectButton.visibility = View.GONE;
        }
        else{
            tabLayoutForAdmin(view)
            val addProjectButton: Button = view.findViewById(R.id.addProjectButton)
            addProjectButton.visibility = View.VISIBLE;

            addProjectButton.setOnClickListener {
                val addNewProjectFragmentManager = requireActivity().supportFragmentManager
                val addNewProjectDialogFragment: AddNewProjectDialogFragment = AddNewProjectDialogFragment()
                addNewProjectDialogFragment.show(addNewProjectFragmentManager, "AddNewProjectDialogFragment")
                addNewProjectDialogFragment.isCancelable = true
            }
        }

        // Get Mobile Number From Shared Preferences
        val mobileNumberSharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("mobileNumber", MODE_PRIVATE)
        val mobileNumber: String = mobileNumberSharedPreferences.getString("mobileNumber", "").toString()

        // Get Project List From Firebase
        var projectListAdapter: ProjectListAdapter = ProjectListAdapter(ArrayList())
        val projectListRecyclerView: RecyclerView = view.findViewById(R.id.projectListRecyclerView)
        val firebaseOperationsForProjects: FirebaseOperationsForProjects = FirebaseOperationsForProjects()
        firebaseOperationsForProjects.getProjectListFromFirebase(projectReference, "Active", mobileNumber, object: FirebaseOperationsForProjects.getProjectListFromFirebaseCallback {
            override fun onProjectListFetched(projectList: List<Project>) {
                projectListAdapter = ProjectListAdapter(projectList)
                projectListRecyclerView.adapter = projectListAdapter
                projectListRecyclerView.layoutManager = LinearLayoutManager(context)
                projectListRecyclerView.setHasFixedSize(true)
                projectListAdapter.notifyDataSetChanged()
            }
        })

        // Tab Layout For Active Or Completed Projects
        val activeOrCompletedTabLayout: SliderTabLayout = view.findViewById(R.id.activeOrCompletedTabLayout)
        activeOrCompletedTabLayout.setOnTabSelectedListener { tab ->
            if (tab == SliderTabLayout.ACTIVE_TAB){
                Toast.makeText(requireActivity(), "Active", Toast.LENGTH_SHORT).show()
                firebaseOperationsForProjects.getProjectListFromFirebase(projectReference, "Active", mobileNumber, object: FirebaseOperationsForProjects.getProjectListFromFirebaseCallback {
                    override fun onProjectListFetched(projectList: List<Project>) {
                        projectListAdapter = ProjectListAdapter(projectList)
                        projectListRecyclerView.adapter = projectListAdapter
                        projectListRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@ProjectFragment.requireActivity())
                        projectListRecyclerView.setHasFixedSize(true)
                        projectListAdapter.notifyDataSetChanged()
                    }
                })

            }else if (tab == SliderTabLayout.COMPLETED_TAB){
                Toast.makeText(requireActivity(), "Completed", Toast.LENGTH_SHORT).show()
                firebaseOperationsForProjects.getProjectListFromFirebase(projectReference, "Completed", mobileNumber, object: FirebaseOperationsForProjects.getProjectListFromFirebaseCallback {
                    override fun onProjectListFetched(projectList: List<Project>) {
                        projectListAdapter = ProjectListAdapter(projectList)
                        projectListRecyclerView.adapter = projectListAdapter
                        projectListRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
                        projectListRecyclerView.setHasFixedSize(true)
                        projectListAdapter.notifyDataSetChanged()
                    }
                })
            }
        }

        return view
    }

    // Tab Layout For Admin
    fun tabLayoutForAdmin(view: View){
        val projectFragmentTabLayout : TabLayout = view.findViewById(R.id.projectFragmentTabLayout)

        val libraryTab: TabLayout.Tab = projectFragmentTabLayout.newTab()
        val libraryTabView: View = getLayoutInflater().inflate(R.layout.bottom_tab_layout_item, null)
        val libraryTextView: TextView = libraryTabView.findViewById(R.id.tab_text)
        libraryTextView.setText("Library")
        val libraryImageView: ImageView = libraryTabView.findViewById(R.id.tab_icon)
        libraryImageView.setImageResource(R.drawable.labour)
        libraryImageView.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.golden))
        libraryTab.setCustomView(libraryTabView)
        projectFragmentTabLayout.addTab(libraryTab)

        val materialTab: TabLayout.Tab = projectFragmentTabLayout.newTab()
        val materialTabView: View = getLayoutInflater().inflate(R.layout.bottom_tab_layout_item, null)
        val materialTextView: TextView = materialTabView.findViewById(R.id.tab_text)
        materialTextView.setText("Material")
        val materialImageView: ImageView = materialTabView.findViewById(R.id.tab_icon)
        materialImageView.setImageResource(R.drawable.material_yellow)
        materialTab.setCustomView(materialTabView)
        projectFragmentTabLayout.addTab(materialTab)

        val bankTransfersTab: TabLayout.Tab = projectFragmentTabLayout.newTab()
        val bankTransfersTabView: View = getLayoutInflater().inflate(R.layout.bottom_tab_layout_item, null)
        val bankTransfersTextView: TextView = bankTransfersTabView.findViewById(R.id.tab_text)
        bankTransfersTextView.setText("Bank Transfers")
        val bankTransfersImageView: ImageView = bankTransfersTabView.findViewById(R.id.tab_icon)
        bankTransfersImageView.setImageResource(R.drawable.bank_transfer_yellow)
        bankTransfersTab.setCustomView(bankTransfersTabView)
        projectFragmentTabLayout.addTab(bankTransfersTab)


        val projectFragmentTabLayoutOnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.getPosition() == 0) {
                    // Library tab selected
                    val libraryIntent: Intent = Intent(requireActivity(), MainLibraryActivity::class.java)
                    startActivity(libraryIntent)

                } else if (tab.getPosition() == 1) {
                    // Material tab selected
                    val materialIntent: Intent = Intent(requireActivity(), MaterialActivity::class.java)
                    startActivity(materialIntent)

                } else if (tab.getPosition() == 2) {
                    // Bank Transfers tab selected
                    val bankTransfersIntent: Intent = Intent(requireActivity(), BankTransferActivity::class.java)
                    startActivity(bankTransfersIntent)

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                if (tab.getPosition() == 0) {
                    // Library tab selected
                    val libraryIntent: Intent = Intent(requireActivity(), MainLibraryActivity::class.java)
                    startActivity(libraryIntent)

                } else if (tab.getPosition() == 1) {
                    // Material tab selected
                    val materialIntent: Intent = Intent(requireActivity(), MaterialActivity::class.java)
                    startActivity(materialIntent)

                } else if (tab.getPosition() == 2) {
                    // Bank Transfers tab selected
                    val bankTransfersIntent: Intent = Intent(requireActivity(), BankTransferActivity::class.java)
                    startActivity(bankTransfersIntent)

                }
            }
        }

        projectFragmentTabLayout.addOnTabSelectedListener(projectFragmentTabLayoutOnTabSelectedListener)
    }

    // Tab Layout For Manager
    fun tabLayoutForManager(view: View){
        val projectFragmentTabLayout : TabLayout = view.findViewById(R.id.projectFragmentTabLayout)
        val libraryTab: TabLayout.Tab = projectFragmentTabLayout.newTab()
        val libraryTabView: View = getLayoutInflater().inflate(R.layout.bottom_tab_layout_item, null)
        val libraryTextView: TextView = libraryTabView.findViewById(R.id.tab_text)
        libraryTextView.setText("Library")
        val libraryImageView: ImageView = libraryTabView.findViewById(R.id.tab_icon)
        libraryImageView.setImageResource(R.drawable.labour)
        libraryTab.setCustomView(libraryTabView)
        projectFragmentTabLayout.addTab(libraryTab)

        val projectFragmentTabLayoutOnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.getPosition() == 0) {
                    // Library tab selected
                    val libraryIntent: Intent =
                        Intent(requireActivity(), MainLibraryActivity::class.java)
                    startActivity(libraryIntent)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                if (tab.getPosition() == 0) {
                // Library tab selected
                val libraryIntent: Intent =
                    Intent(requireActivity(), MainLibraryActivity::class.java)
                startActivity(libraryIntent)
            }}
        }

        projectFragmentTabLayout.addOnTabSelectedListener(projectFragmentTabLayoutOnTabSelectedListener)
    }




}