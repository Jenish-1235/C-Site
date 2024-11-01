package com.csite.app.FirebaseOperations

import com.csite.app.Objects.BankTransfer
import com.google.firebase.database.FirebaseDatabase

class FirebaseOperationsForBankTransfers {

    object FirebaseOperationsForBankTransfers {}

    val bankTransferReference = FirebaseDatabase.getInstance().getReference("BankTransfers")
    // 1. Save Bank Transfer to Firebase
    fun saveBankTransferToFirebase(bankTransfer: BankTransfer) {
        bankTransfer.date = bankTransfer.date.replace("/", "-") + generateRandomSixDigitNumber()
        bankTransferReference.child(bankTransfer.date).setValue(bankTransfer)

    }

    // 2. Get Bank Transfers from Firebase



    fun generateRandomSixDigitNumber(): String {
        val random = java.util.Random()
        val number = random.nextInt(900000) + 100000
        return number.toString()
    }
}