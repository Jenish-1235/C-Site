package com.csite.app.Objects

class TransactionPaymentIn {
    var transactionDate: String? = null
    var transactionParty: String? = null
    var transactionAmount: String? = null
    var paymentInTransactionPaymentMode: String? = null
    var transactionDescription: String? = null
    var paymentInTrasactionCostCode: String? = null
    var paymentInTransactionCategory:String? = null
    var transactionId = ""
    var transactionType = "Payment In"
    constructor(paymentInTransactionDate: String?, paymentInTransactionPaymentFromParty: String?, paymentInTransactionAmount: String?, paymentInTransactionDescription: String?, paymentInTransactionPaymentMode: String?, paymentInTrasactionCostCode: String?, paymentInTransactionCategory:String?){
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