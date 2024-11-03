package com.csite.app.FirebaseOperations

import com.csite.app.Objects.Material
import com.csite.app.Objects.Party
import com.csite.app.Objects.Workforce
import com.google.android.material.datepicker.MaterialTextInputPicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseOperationsForLibrary {

    val libraryReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Library")
    val materialReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Library/Material")
    val workforceReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Library/Workforce")
    val partyReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Library/Party")


    object UtilityOperations{}

    // 1. Fetch Library List
    fun fetchLibraryList(callback: onLibraryListReceived): HashMap<String, String> {
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
    fun addMaterialsToMaterialLibrary(material: Material){
        material.materialId = materialIdGenerator()
        materialReference.child(material.materialId).setValue(material)
    }


    // 2.1 Generate Material ID
    fun materialIdGenerator():String{
        val random = java.util.Random()
        val materialId = random.nextInt(900000) + 100000
        return "mat" + materialId.toString()
    }

    // 2.2 Fetch Materials from Material Library
    fun fetchMaterialsFromMaterialLibrary(callback: onMaterialListReceived):ArrayList<Material>{
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
    // 2.3 Fetch Materials from Material Library
    interface onMaterialListReceived{
        fun onMaterialListReceived(materialList: ArrayList<Material>)
    }

    // 3. Save Workforce to library
    fun addWorkforceToWorkforceLibrary(workforce: Workforce){
        workforce.workforceId = workforceIdGenerator()
        workforceReference.child(workforce.workforceId).setValue(workforce)
    }
    // 3.1 Generate Workforce ID
    fun workforceIdGenerator():String{
        val random = java.util.Random()
        val workforceId = random.nextInt(900000) + 100000
        return "wf" + workforceId.toString()
    }

    // 3.2 Fetch Workforce from Workforce Library
    fun fetchWorkforceFromWorkforceLibrary(callback: onWorkforceListReceived):ArrayList<Workforce>{
        val workforceList = ArrayList<Workforce>()
        val workforceValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                workforceList.clear()
                for (workforceSnapshot in snapshot.children) {
                    val workforce = workforceSnapshot.getValue(Workforce::class.java)
                    if (workforce != null) {
                        workforceList.add(workforce)
                    }
                }
                callback.onWorkforceListReceived(workforceList)
                }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        workforceReference.addValueEventListener(workforceValueEventListener)
        return workforceList
    }
    // 3.3 Fetch Workforce from Workforce Library
    interface onWorkforceListReceived{
        fun onWorkforceListReceived(workforceList: ArrayList<Workforce>)
    }


    // 4. Save Party to library
    fun addPartyToPartyLibrary(party: Party){
        party.partyId = partyIdGenerator()
        if (party.partyAmountToPayOrReceive == null || party.partyAmountToPayOrReceive == ""){
            party.partyAmountToPayOrReceive = "0"
        }

        if (party.partyOpeningBalanceDetails == null || party.partyOpeningBalanceDetails == ""){
            party.partyOpeningBalanceDetails = "Fresh"
        }

        partyReference.child(party.partyId).setValue(party)
    }

    // 4.1 Generate Party ID
    fun partyIdGenerator():String{
        val random = java.util.Random()
        val partyId = random.nextInt(900000) + 100000
        return "pt" + partyId.toString()
    }

    // 4.2 Fetch Party from Party Library
    fun fetchPartyFromPartyLibrary(callback: onPartyListReceived):ArrayList<Party>{
        val partyList = ArrayList<Party>()
        val partyValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                partyList.clear()
                for (partySnapshot in snapshot.children) {
                    val party = partySnapshot.getValue(Party::class.java)
                    if (party != null) {
                        partyList.add(party)
                    }
                }
                callback.onPartyListReceived(partyList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        partyReference.addValueEventListener(partyValueEventListener)
        return partyList
    }
    // 4.3 Fetch Party from Party Library
    interface onPartyListReceived{
        fun onPartyListReceived(partyList: ArrayList<Party>)
    }




}