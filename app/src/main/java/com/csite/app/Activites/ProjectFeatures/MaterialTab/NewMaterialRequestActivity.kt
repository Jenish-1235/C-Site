package com.csite.app.Activites.ProjectFeatures.MaterialTab

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.DialogFragments.MaterialSelectionLibraryDialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalMaterialTab
import com.csite.app.Objects.MaterialSelection
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.NewMaterialRequestListAdapter
import com.csite.app.RecyclerViewListAdapters.SelectedMaterialsListAdapter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class NewMaterialRequestActivity : AppCompatActivity(), MaterialSelectionLibraryDialogFragment.OnMaterialListReceived{


    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_material_request)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addMaterialFab = findViewById<ExtendedFloatingActionButton>(R.id.fabAddMaterial)
        addMaterialFab.setOnClickListener {
            val materialSelectionLibraryDialogFragment = MaterialSelectionLibraryDialogFragment()
            materialSelectionLibraryDialogFragment.show(supportFragmentManager, "MaterialSelectionLibraryDialogFragment")
        }
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


        val makeRequestButton = findViewById<Button>(R.id.makeRequestButton)
        makeRequestButton.setOnClickListener {

            try {
                var requestedMaterialHashMap = newMaterialRequestListAdapter.getFinalMaterialList()
                if (requestedMaterialHashMap.size == 0) {
                    Toast.makeText(this, "No Materials Selected", Toast.LENGTH_SHORT).show()
                } else {
                    val projectId = intent.getStringExtra("projectId")
                    val firebaseOperationsForProjectInternalMaterialTab =
                        FirebaseOperationsForProjectInternalMaterialTab()
                    if (projectId != null) {
                        firebaseOperationsForProjectInternalMaterialTab.saveMaterialRequests(
                            projectId,
                            requestedMaterialHashMap
                        )
                        finish()
                    } else {
                        Toast.makeText(this, "Project id NOT FOUND", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e:Exception){
                Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
                Log.e("Error", e.toString())
            }
        }


    }

    fun backButton(view: View){
        finish()
    }

    var materialList = ArrayList<MaterialSelection>()
    var newMaterialRequestListAdapter : NewMaterialRequestListAdapter = NewMaterialRequestListAdapter(materialList)
    override fun sendMaterialList(selectedMaterialList: ArrayList<MaterialSelection>) {
        materialList.clear()
        materialList = selectedMaterialList
        Toast.makeText(this, "Material List Received", Toast.LENGTH_SHORT).show()
        val addMaterialFabButton = findViewById<ExtendedFloatingActionButton>(R.id.fabAddMaterial)
        if(materialList.size != 0){
            addMaterialFabButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)))
            addMaterialFabButton.icon = getResources().getDrawable(R.drawable.icon_edit)
            addMaterialFabButton.text = "Edit Materials"
        }else{
            addMaterialFabButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)))
            addMaterialFabButton.icon = getResources().getDrawable(R.drawable.icon_add)
            addMaterialFabButton.text = "Add Materials"
        }
        newMaterialRequestListAdapter = NewMaterialRequestListAdapter(materialList)
        recyclerView.adapter  = newMaterialRequestListAdapter
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.adapter?.notifyItemRangeChanged(0, materialList.size)
        recyclerView.layoutManager = LinearLayoutManager(this)


    }
}