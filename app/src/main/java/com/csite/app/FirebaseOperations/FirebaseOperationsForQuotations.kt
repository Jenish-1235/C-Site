package com.csite.app.FirebaseOperations

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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




}