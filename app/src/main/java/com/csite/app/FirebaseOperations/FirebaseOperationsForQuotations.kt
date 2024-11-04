package com.csite.app.FirebaseOperations

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.site.app.Objects.Quotation
import java.util.Random

class FirebaseOperationsForQuotations {

    class FirebaseOperationsForQuotations {}

    val quotationReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Quotations")

    fun randomSixDigitIdGenerator():String{
        val random = Random()
        var id = random.nextInt(900000) + 100000
        return id.toString()
    }

    // 1. Save Quotation
    fun saveQuotation(quotation: Quotation) {
        quotation.quoteId = "q" + randomSixDigitIdGenerator()
        quotationReference.child(quotation.quoteId).setValue(quotation)
    }

    // 2. Fetch Quotation List From Backend
    fun fetchQuotationForFirebase(callback: OnQuotationFetchedListener){
        val quotationList = ArrayList<Quotation>()
        val quotationValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val quotation = dataSnapshot.getValue(Quotation::class.java)
                    quotation?.let {
                        quotationList.add(it)
                    }

                    callback.onQuotationFetched(quotationList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        quotationReference.addListenerForSingleValueEvent(quotationValueEventListener)
    }

    interface OnQuotationFetchedListener {
        fun onQuotationFetched(quotationList: ArrayList<Quotation>)
    }
}