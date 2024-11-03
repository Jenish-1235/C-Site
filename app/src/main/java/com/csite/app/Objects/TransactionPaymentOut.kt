package com.csite.app.Objects

class TransactionPaymentOut {
    var paymentOutTransactionDate: String? = null
    var paymentOutTransactionPaymentFromParty: String? = null
    var paymentOutTransactionAmount: String? = null
    var paymentOutTransactionPaymentMode: String? = null
    var paymentOutTransactionDescription: String? = null
    var paymentOutTrasactionCostCode: String? = null
    var paymentOutTransactionCategory:String? = null
    var paymentOutTransactionId: String? = null
    constructor(paymentOutTransactionDate: String?, paymentOutTransactionPaymentFromParty: String?, paymentOutTransactionAmount: String?, paymentOutTransactionDescription: String?, paymentOutTransactionPaymentMode: String?, paymentOutTrasactionCostCode: String?, paymentOutTransactionCategory:String?){
        this.paymentOutTransactionDate = paymentOutTransactionDate
        this.paymentOutTransactionPaymentFromParty = paymentOutTransactionPaymentFromParty
        this.paymentOutTransactionAmount = paymentOutTransactionAmount
        this.paymentOutTransactionPaymentMode = paymentOutTransactionPaymentMode
        this.paymentOutTransactionDescription = paymentOutTransactionDescription
        this.paymentOutTrasactionCostCode = paymentOutTrasactionCostCode
        this.paymentOutTransactionCategory = paymentOutTransactionCategory
        this.paymentOutTransactionId = paymentOutTransactionId
    }
    constructor()
}