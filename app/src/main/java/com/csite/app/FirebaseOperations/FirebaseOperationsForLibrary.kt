package com.csite.app.FirebaseOperations

import com.csite.app.Objects.Material
import com.csite.app.Objects.Workforce
import com.google.android.material.datepicker.MaterialTextInputPicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FirebaseOperationsForLibrary {
    object UtilityOperations{}

    // 1. Fetch Library List
    fun fetchLibraryList(libraryReference: DatabaseReference , callback: onLibraryListReceived): HashMap<String, String> {
        val libraryList = HashMap<String, String>()
        val libraryValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (librarySnapshot in snapshot.children) {
                    val libraryName = librarySnapshot.key
                    val libraryItems = librarySnapshot.childrenCount
                    if (libraryName != null) {
                        libraryList[libraryName] = "$libraryItems Items"
                    }
                }

                callback.onLibraryListReceived(libraryList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        libraryReference.addValueEventListener(libraryValueEventListener)
        return libraryList
    }

    interface onLibraryListReceived{
        fun onLibraryListReceived(libraryList: HashMap<String, String>)
    }

    // 2. Add Materials to Material Library
    fun addMaterialsToMaterialLibrary(materialReference: DatabaseReference, material: Material){
        material.materialId = materialIdGenerator()
        materialReference.child(material.materialId).setValue(material)
    }

    fun materialIdGenerator():String{
        val random = java.util.Random()
        val materialId = random.nextInt(1000000)
        return "mat" + materialId.toString()
    }

    // 3. Fetch Materials from Material Library
    fun fetchMaterialsFromMaterialLibrary(materialReference: DatabaseReference, callback: onMaterialListReceived):ArrayList<Material>{
        val materialList = ArrayList<Material>()
        val materialValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                materialList.clear()
                for (materialSnapshot in snapshot.children) {
                    val material = materialSnapshot.getValue(Material::class.java)
                    if (material != null) {
                        materialList.add(material)
                    }
                }
                callback.onMaterialListReceived(materialList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        materialReference.addValueEventListener(materialValueEventListener)
        return materialList
    }

    interface onMaterialListReceived{
        fun onMaterialListReceived(materialList: ArrayList<Material>)
    }

    // 4. Save Workforce to library
    fun addWorkforceToWorkforceLibrary(workforceReference: DatabaseReference, workforce: Workforce){
        workforce.workforceId = workforceIdGenerator()
        workforceReference.child(workforce.workforceId).setValue(workforce)
    }

    fun workforceIdGenerator():String{
        val random = java.util.Random()
        val workforceId = random.nextInt(1000000)
        return "wf" + workforceId.toString()
    }

}