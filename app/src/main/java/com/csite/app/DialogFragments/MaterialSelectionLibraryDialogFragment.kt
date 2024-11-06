package com.csite.app.DialogFragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewSalesInvoiceTransactionActivity
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.Objects.Material
import com.csite.app.Objects.MaterialSelection
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.MaterialSelectionListAdapter

class MaterialSelectionLibraryDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(
            R.layout.dialog_fragment_material_selection_library,
            container,
            false
        )
        val dialog = getDialog()
        if (dialog != null) {
            dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        var materialSelectionRecyclerView = view.findViewById<RecyclerView>(R.id.selectMaterialRecyclerView)
        var fetchedmaterialList = ArrayList<Material>()
        var materialSelectionListAdapter = MaterialSelectionListAdapter(fetchedmaterialList)
        val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        firebaseOperationsForLibrary.fetchMaterialsFromMaterialLibrary(object : FirebaseOperationsForLibrary.onMaterialListReceived {
            override fun onMaterialListReceived(materialList: ArrayList<Material>) {
                fetchedmaterialList = materialList
                materialSelectionListAdapter = MaterialSelectionListAdapter(fetchedmaterialList)
                materialSelectionRecyclerView.adapter = materialSelectionListAdapter
                materialSelectionListAdapter.notifyDataSetChanged()
                materialSelectionRecyclerView.layoutManager = LinearLayoutManager(context)
                materialSelectionRecyclerView.setHasFixedSize(true)
            }

        })

        var selectedMaterialHashmap = HashMap<String, MaterialSelection>()
        var selectedMaterialList = ArrayList<MaterialSelection>()
        val selectMaterialsButton = view.findViewById<Button>(R.id.selectMaterialsButton)
        selectMaterialsButton.setOnClickListener {
            selectedMaterialHashmap = materialSelectionListAdapter.getSelectedMaterialHashmap()
            selectedMaterialList.clear()
            for (key in selectedMaterialHashmap.values){
                selectedMaterialList.add(key)
            }

            onMaterialListReceived?.sendMaterialList(selectedMaterialList)
            getDialog()?.dismiss()

            Toast.makeText(requireContext(), "Selected Materials: " + selectedMaterialList.size , Toast.LENGTH_SHORT).show()

        }

        val newMaterialButton = view.findViewById<Button>(R.id.newMaterialButton)
        newMaterialButton.setOnClickListener {
            val newMaterialDialogFragment = AddNewMaterialDialogFragment()
            newMaterialDialogFragment.show(this.parentFragmentManager, "newMaterialDialogFragment")
        }

        return view
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

    interface OnMaterialListReceived {
        fun sendMaterialList(selectedMaterialList: ArrayList<MaterialSelection>)
    }
    private var onMaterialListReceived: OnMaterialListReceived? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onMaterialListReceived = context as OnMaterialListReceived
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement OnMaterialListReceived")
        }
    }
}