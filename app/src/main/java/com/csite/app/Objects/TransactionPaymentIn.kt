package com.csite.app.Objects

class TransactionPaymentIn {
    var paymentInTransactionDate: String? = null
    var paymentInTransactionPaymentFromParty: String? = null
    var paymentInTransactionAmount: String? = null
    var paymentInTransactionPaymentMode: String? = null
    var paymentInTransactionDescription: String? = null
    var paymentInTrasactionCostCode: String? = null
    var paymentInTransactionCategory:String? = null
    var paymentInTransactionId: String? = null
    constructor(paymentInTransactionDate: String?, paymentInTransactionPaymentFromParty: String?, paymentInTransactionAmount: String?, paymentInTransactionDescription: String?, paymentInTransactionPaymentMode: String?, paymentInTrasactionCostCode: String?, paymentInTransactionCategory:String?){
        this.paymentInTransactionDate = paymentInTransactionDate
        this.paymentInTransactionPaymentFromParty = paymentInTransactionPaymentFromParty
        this.paymentInTransactionAmount = paymentInTransactionAmount
        this.paymentInTransactionPaymentMode = paymentInTransactionPaymentMode
        this.paymentInTransactionDescription = paymentInTransactionDescription
        this.paymentInTrasactionCostCode = paymentInTrasactionCostCode
        this.paymentInTransactionCategory = paymentInTransactionCategory

        this.paymentInTransactionId = paymentInTransactionId
    }
    constructor()
}