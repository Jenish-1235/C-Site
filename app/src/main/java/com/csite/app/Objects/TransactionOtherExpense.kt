package com.csite.app.Objects

class TransactionOtherExpense {
    var otherExpenseTransactionDate = ""
    var otherExpenseTransactionTo = ""
    var otherExpenseTransactionQuantity = ""
    var otherExpenseTransactionUnit = ""
    var otherExpenseTransactionUnitPrice = ""
    var otherExpenseTransactionAdditionalCharges : String? = null
    var otherExpenseTransactionDiscount: String? = null
    var otherExpenseTransactionTotalAmount = "0"
    var otherExpenseTransactionCategory = ""
    var otherExpenseTransactionNotes :String? = null
    var otherExpenseTransactionId = ""

    constructor(otherExpenseTransactionDate: String, otherExpenseTransactionTo: String, otherExpenseTransactionQuantity: String, otherExpenseTransactionUnit: String, otherExpenseTransactionUnitPrice: String, otherExpenseTransactionAdditionalCharges: String?, otherExpenseTransactionDiscount: String?, otherExpenseTransactionTotalAmount: String, otherExpenseTransactionCategory: String, otherExpenseTransactionNotes: String?) {
        this.otherExpenseTransactionDate = otherExpenseTransactionDate
        this.otherExpenseTransactionTo = otherExpenseTransactionTo
        this.otherExpenseTransactionQuantity = otherExpenseTransactionQuantity
        this.otherExpenseTransactionUnit = otherExpenseTransactionUnit
        this.otherExpenseTransactionUnitPrice = otherExpenseTransactionUnitPrice
        this.otherExpenseTransactionAdditionalCharges = otherExpenseTransactionAdditionalCharges
        this.otherExpenseTransactionDiscount = otherExpenseTransactionDiscount
        this.otherExpenseTransactionTotalAmount = otherExpenseTransactionTotalAmount
        this.otherExpenseTransactionCategory = otherExpenseTransactionCategory
        this.otherExpenseTransactionNotes = otherExpenseTransactionNotes
        this.otherExpenseTransactionId = otherExpenseTransactionId
    }


    constructor()

}