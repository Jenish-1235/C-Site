package com.csite.app.DialogFragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.DialogFragments.PartySelectionForBankTransferDialogFragment.OnPartySelectedListener
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjects
import com.csite.app.Objects.Project
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.ProjectSelectionListAdapter


class Project_Selection_Dialog_Fragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.dialog_fragment_project_selection, container, false)
        val selectProjectRecyclerView = view.findViewById<RecyclerView>(R.id.selectProjectRecyclerView)
        val dialog = getDialog()
        if (dialog != null) {
            dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        val sharedPreferences = requireActivity().getSharedPreferences("mobileNumber", 0)
        val mobileNumber = sharedPreferences.getString("mobileNumber", "") ?: ""

        val firebaseOperationsForProject = FirebaseOperationsForProjects()
        firebaseOperationsForProject.fetchProjectList(mobileNumber, object : FirebaseOperationsForProjects.getProjectListFromFirebaseCallback {
            override fun onProjectListFetched(projectList: List<Project>) {
                val projectSelectionListAdapter = ProjectSelectionListAdapter(projectList)
                selectProjectRecyclerView.adapter = projectSelectionListAdapter
                projectSelectionListAdapter.setOnItemClickListener(object : ProjectSelectionListAdapter.OnItemClickListener{
                    override fun OnItemClick(project: Project?) {
                        projectSelectedListener.onProjectSelected(project)
                        dismiss()
                    }
                })
                projectSelectionListAdapter.notifyDataSetChanged()
                selectProjectRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            }

        })


        return view;
    }

    // sets positioning of dialog fragment to bottom of screen.
    override fun onStart() {
        super.onStart()

        val dialog: Dialog? = getDialog()

        if (dialog != null){
            val window = dialog.getWindow()
            if (window != null){
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                window.setGravity(Gravity.BOTTOM)

                val params = window.getAttributes()
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
                params.horizontalMargin = 0f;
                params.verticalMargin = 0f;

                window.setWindowAnimations(R.style.DialogAnimation)
            }
        }
    }

    lateinit var projectSelectedListener :OnProjectSelectedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            projectSelectedListener = context as OnProjectSelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement OnPartySelectedListener")
        }
    }

    interface OnProjectSelectedListener{
        fun onProjectSelected(project: Project?)
    }

}