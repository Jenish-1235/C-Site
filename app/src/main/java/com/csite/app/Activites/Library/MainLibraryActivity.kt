package com.csite.app.Activites.Library

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.MainLibraryListAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainLibraryActivity : AppCompatActivity() {

    val libraryReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Library")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mainLibraryRecyclerView : RecyclerView = findViewById(R.id.MainLibraryRecyclerView)
        val getLibraryList = FirebaseOperationsForLibrary()
        getLibraryList.fetchLibraryList(libraryReference, object: FirebaseOperationsForLibrary.onLibraryListReceived{
            override fun onLibraryListReceived(libraries: HashMap<String, String>) {
                val mainLibraryListAdapter: MainLibraryListAdapter = MainLibraryListAdapter(this@MainLibraryActivity, libraries)
                mainLibraryRecyclerView.adapter = mainLibraryListAdapter
                mainLibraryRecyclerView.layoutManager = LinearLayoutManager(this@MainLibraryActivity)
                mainLibraryRecyclerView.setHasFixedSize(true)
                mainLibraryRecyclerView.isNestedScrollingEnabled = false
                mainLibraryListAdapter.notifyDataSetChanged()
            }
        })

    }

    fun backButton(view:View){
        finish()
    }
}