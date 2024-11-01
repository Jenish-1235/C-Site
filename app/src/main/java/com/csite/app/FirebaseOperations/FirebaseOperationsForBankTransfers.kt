package com.csite.app.FirebaseOperations

import android.util.Log
import android.widget.Toast
import com.csite.app.Objects.BankTransfer
import com.google.firebase.database.FirebaseDatabase

class FirebaseOperationsForBankTransfers {


    object FirebaseOperationsForBankTransfers {}

    val bankTransferReference = FirebaseDatabase.getInstance().getReference("BankTransfers")
    // 1. Save Bank Transfer to Firebase
    fun saveBankTransferToFirebase(bankTransfer: BankTransfer) {
        val key = bankTransfer.date.replace("/", "-") + generateRandomSixDigitNumber()
        bankTransferReference.child(key).setValue(bankTransfer)

    }

    // 2. Get Bank Transfers from Firebase
    fun getBankTransfersFromFirebase(callback: onBankTransfersFetched) : ArrayList<BankTransfer> {
        var bankTransfers = ArrayList<BankTransfer>()

        bankTransferReference.get().addOnSuccessListener { dataSnapshot ->
            dataSnapshot.children.forEach { snapshot ->
                val bankTransfer = snapshot.getValue(BankTransfer::class.java)
                Log.d("BankTransferMunna1", bankTransfer.toString())
//                bankTransfer!!.date = bankTransfer!!.date.substring(0, bankTransfer.date.length - 6)
                bankTransfers.add(bankTransfer!!)
            }
            bankTransfers.sortByDescending { it.date }
            callback.onBankTransfersFetched(bankTransfers)

        }
        return bankTransfers
    }
    // 2.1 Get Bank Transfers from Firebase
    interface onBankTransfersFetched{
        fun onBankTransfersFetched(bankTransfers: ArrayList<BankTransfer>)
    }


    // 3. Delete Bank Transfer from Firebase
    fun deleteBankTransferFromFirebase() {
        FirebaseDatabase.getInstance().reference.child("BankTransfers").removeValue()
    }

    // Generates 6 digit random code for bank transfer to be saved with duplicate dates.
    fun generateRandomSixDigitNumber(): String {
        val random = java.util.Random()
        val number = random.nextInt(900000) + 100000
        return number.toString()
    }
}