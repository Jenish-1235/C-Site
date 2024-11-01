package com.csite.app.FirebaseOperations

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


}