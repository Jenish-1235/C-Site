package com.csite.app.DialogFragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.Objects.Party
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.PartyLibraryListAdapter

class PartySelectionLibraryDialogFragment : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_party_selection_library_dialog, container, false)
        val dialog = getDialog()
        if (dialog != null) {
            dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        val selectPartyRecyclerView = view.findViewById<RecyclerView>(R.id.selectPartyRecyclerView)
        lateinit var partyLibraryListAdapter : PartyLibraryListAdapter
        val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        firebaseOperationsForLibrary.fetchPartyFromPartyLibrary(object : FirebaseOperationsForLibrary.onPartyListReceived{
            override fun onPartyListReceived(partyList: ArrayList<Party>) {
                partyLibraryListAdapter = PartyLibraryListAdapter(partyList)
                selectPartyRecyclerView.adapter = partyLibraryListAdapter
                selectPartyRecyclerView.layoutManager = LinearLayoutManager(context)
                selectPartyRecyclerView.adapter?.notifyDataSetChanged()
                partyLibraryListAdapter.setOnItemClickListener(object :PartyLibraryListAdapter.OnItemClickListener{
                    override fun OnItemClick(party: Party?) {
                        partySelectedListener.onPartySelected(party)
                        dismiss()
                    }

                })
            }
        })



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

    lateinit var partySelectedListener :OnPartySelectedListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            partySelectedListener = context as OnPartySelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement OnPartySelectedListener")
        }
    }
    interface OnPartySelectedListener{
        fun onPartySelected(party: Party?)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val dialog: Dialog? = getDialog()
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null)
        }

    }
}