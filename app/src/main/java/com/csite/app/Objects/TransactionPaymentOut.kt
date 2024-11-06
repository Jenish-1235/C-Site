package com.csite.app.Objects

class TransactionPaymentOut {
    var transactionDate: String = ""
    var transactionParty: String = ""
    var transactionAmount: String = ""
    var paymentOutTransactionPaymentMode: String = ""
    var transactionDescription: String = ""
    var paymentOutTrasactionCostCode: String = ""
    var paymentOutTransactionCategory:String = ""
    var transactionId: String = ""
    var transactionType = "Payment Out"

    constructor(paymentOutTransactionDate: String, paymentOutTransactionPaymentToParty: String, paymentOutTransactionAmount: String, paymentOutTransactionDescription: String, paymentOutTransactionPaymentMode: String, paymentOutTrasactionCostCode: String, paymentOutTransactionCategory:String){
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