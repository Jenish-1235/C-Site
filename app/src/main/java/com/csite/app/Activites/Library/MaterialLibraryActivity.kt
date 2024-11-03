package com.csite.app.Activites.Library

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.DialogFragments.AddNewMaterialDialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.Objects.Material
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.MaterialLibraryListAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MaterialLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addNewMaterialButton: Button = findViewById(R.id.addNewMaterialButton)
        addNewMaterialButton.setOnClickListener{
            val dialogFragment: DialogFragment = AddNewMaterialDialogFragment()
            dialogFragment.show(supportFragmentManager, "AddNewMaterialDialogFragment")
        }
        val materialLibraryRecyclerView: RecyclerView = findViewById(R.id.materialLibraryRecyclerView)
        val firebaseOperationsForLibrary: FirebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        firebaseOperationsForLibrary.fetchMaterialsFromMaterialLibrary(object:FirebaseOperationsForLibrary.onMaterialListReceived{
            override fun onMaterialListReceived(materialList: ArrayList<Material>) {
                val materialLibraryListAdapter: MaterialLibraryListAdapter = MaterialLibraryListAdapter(applicationContext, materialList)
                materialLibraryRecyclerView.adapter = materialLibraryListAdapter
                materialLibraryRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(applicationContext)
                materialLibraryListAdapter.notifyDataSetChanged()
            }
        })

    }

    fun backButton(view: View){
        finish()
    }
}