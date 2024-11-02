package com.csite.app.Activites.Library

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.DialogFragments.AddNewWorkforceDialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.Objects.Workforce
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.WorkforceLibraryListAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class WorkforceLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workforce_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addNewWorkforceButton: Button = findViewById(R.id.addNewWorkforceButton)
        addNewWorkforceButton.setOnClickListener{
            val addNewWorkforceDialogFragment: DialogFragment = AddNewWorkforceDialogFragment()
            addNewWorkforceDialogFragment.show(supportFragmentManager, "AddNewWorkforceDialogFragment")
        }

        val workforceReference:DatabaseReference = FirebaseDatabase.getInstance().getReference("Library/Workforce")
        val workforceLibraryRecyclerView: RecyclerView = findViewById(R.id.workforceLibraryRecyclerView)
        val firebaseOperationsForLibrary: FirebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        firebaseOperationsForLibrary.fetchWorkforceFromWorkforceLibrary(workforceReference, object: FirebaseOperationsForLibrary.onWorkforceListReceived{
            override fun onWorkforceListReceived(workforceList: ArrayList<Workforce>) {
                val workforceLibraryListAdapter: WorkforceLibraryListAdapter = WorkforceLibraryListAdapter(this@WorkforceLibraryActivity, workforceList)
                workforceLibraryRecyclerView.adapter = workforceLibraryListAdapter
                workforceLibraryRecyclerView.layoutManager = LinearLayoutManager(this@WorkforceLibraryActivity)
                workforceLibraryListAdapter.notifyDataSetChanged()
            }

        })

    }

    fun backButton(view: View){
        finish()
    }
}