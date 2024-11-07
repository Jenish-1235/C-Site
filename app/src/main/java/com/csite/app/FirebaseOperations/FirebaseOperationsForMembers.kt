package com.csite.app.FirebaseOperations

import android.util.Log
import androidx.appcompat.view.ActionMode.Callback
import com.csite.app.Objects.Member
import com.csite.app.Objects.Party
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FirebaseOperationsForMembers {

    object FirebaseOperationsForMembers {}

    // 1. function to check whether a key exists in the database for given parent or not
    fun checkExistingMember(parent : DatabaseReference , value : String, callback: MemberExistenceCallback) {

        val memberValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild(value)) {
                    // Member exists
                    Log.d("FirebaseOperationsForMembers", "Member exists")
                    val member = dataSnapshot.child(value).getValue(Member::class.java)
                    if (member!!.password == "" && member!!.name.isNotEmpty()){
                        // Member exists but password is empty
                        Log.d("FirebaseOperationsForMembers", "Member exists but password is empty")
                        callback.isMemberExists(false)
                    }else{
                        callback.isMemberExists(true)
                    }
                } else {
                    // Member does not exist
                    Log.d("FirebaseOperationsForMembers", "Member does not exist")
                    callback.isMemberExists(false)
                    return
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        parent.addValueEventListener(memberValueEventListener)
    }
    // 1.1 Callback interface to handle the result of the check
    interface MemberExistenceCallback {
        fun isMemberExists(exists: Boolean)
    }

    // 2 Save new member to database
    fun saveNewMemberToDatabase(parent: DatabaseReference, member: Member) {
        parent.child(member.mobileNumber).setValue(member)
        var party = Party()
        party.partyName = member.name
        party.partyType = member.memberAccess
        party.partyCondition = "000"
        party.partyOpeningBalanceDetails = "Fresh"
        party.partyMobileNumber = member.mobileNumber
        party.partyAmountToPayOrReceive = "0.0"
        val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        firebaseOperationsForLibrary.addPartyToPartyLibrary(party)
    }

    // 3. Get member from database
    fun getMemberFromDatabase(parent: DatabaseReference, mobileNumber: String, callback: MemberCallback): Member {
        val memberValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild(mobileNumber)) {
                    val member = dataSnapshot.child(mobileNumber).getValue(Member::class.java)
                    if (member != null) {
                        callback.onMemberRetrieved(member)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        parent.addValueEventListener(memberValueEventListener)
        return Member()
    }
    // 3.1 Callback interface to handle the result of the member retrieval
    interface MemberCallback {
        fun onMemberRetrieved(member: Member)
    }
}