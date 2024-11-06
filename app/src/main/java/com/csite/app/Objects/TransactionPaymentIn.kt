package com.csite.app.Objects

class TransactionPaymentIn {
    var transactionDate: String = ""
    var transactionParty: String = ""
    var transactionAmount: String = ""
    var paymentInTransactionPaymentMode: String = ""
    var transactionDescription: String = ""
    var paymentInTrasactionCostCode: String = ""
    var paymentInTransactionCategory:String = ""
    var transactionId = ""
    var transactionType = "Payment In"
    constructor(paymentInTransactionDate: String, paymentInTransactionPaymentFromParty: String, paymentInTransactionAmount: String, paymentInTransactionDescription: String, paymentInTransactionPaymentMode: String, paymentInTrasactionCostCode: String, paymentInTransactionCategory:String){
        this.transactionDate = paymentInTransactionDate
        this.transactionParty = paymentInTransactionPaymentFromParty
        this.transactionAmount = paymentInTransactionAmount
        this.paymentInTransactionPaymentMode = paymentInTransactionPaymentMode
        this.transactionDescription = paymentInTransactionDescription
        this.paymentInTrasactionCostCode = paymentInTrasactionCostCode
        this.paymentInTransactionCategory = paymentInTransactionCategory
        this.transactionType = transactionType
        this.transactionId = transactionId
    }
    constructor()
}