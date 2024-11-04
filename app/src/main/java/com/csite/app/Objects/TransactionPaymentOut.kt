package com.csite.app.Objects

class TransactionPaymentOut {
    var transactionDate: String? = null
    var transactionParty: String? = null
    var transactionAmount: String? = null
    var paymentOutTransactionPaymentMode: String? = null
    var transactionDescription: String? = null
    var paymentOutTrasactionCostCode: String? = null
    var paymentOutTransactionCategory:String? = null
    var transactionId: String? = null
    var transactionType = "Payment Out"

    constructor(paymentOutTransactionDate: String?, paymentOutTransactionPaymentToParty: String?, paymentOutTransactionAmount: String?, paymentOutTransactionDescription: String?, paymentOutTransactionPaymentMode: String?, paymentOutTrasactionCostCode: String?, paymentOutTransactionCategory:String?){
        this.transactionDate = paymentOutTransactionDate
        this.transactionParty = paymentOutTransactionPaymentToParty
        this.transactionAmount = paymentOutTransactionAmount
        this.paymentOutTransactionPaymentMode = paymentOutTransactionPaymentMode
        this.transactionDescription = paymentOutTransactionDescription
        this.paymentOutTrasactionCostCode = paymentOutTrasactionCostCode
        this.paymentOutTransactionCategory = paymentOutTransactionCategory
        this.transactionId = transactionId
        this.transactionType = transactionType
    }
    constructor()
}