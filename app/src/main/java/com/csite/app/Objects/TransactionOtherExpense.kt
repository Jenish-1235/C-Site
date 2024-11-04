package com.csite.app.Objects

class TransactionOtherExpense {
    var transactionDate = ""
    var transactionParty = ""
    var otherExpenseTransactionQuantity = ""
    var otherExpenseTransactionUnit = ""
    var otherExpenseTransactionUnitPrice = ""
    var otherExpenseTransactionAdditionalCharges : String? = null
    var otherExpenseTransactionDiscount: String? = null
    var transactionAmount = "0"
    var otherExpenseTransactionCategory = ""
    var transactionDescription :String? = null
    var transactionId = ""
    var transactionType = "Other Expense"

    constructor(otherExpenseTransactionDate: String, otherExpenseTransactionTo: String, otherExpenseTransactionQuantity: String, otherExpenseTransactionUnit: String, otherExpenseTransactionUnitPrice: String, otherExpenseTransactionAdditionalCharges: String?, otherExpenseTransactionDiscount: String?, otherExpenseTransactionTotalAmount: String, otherExpenseTransactionCategory: String, otherExpenseTransactionNotes: String?) {
        this.transactionDate = otherExpenseTransactionDate
        this.transactionParty = otherExpenseTransactionTo
        this.otherExpenseTransactionQuantity = otherExpenseTransactionQuantity
        this.otherExpenseTransactionUnit = otherExpenseTransactionUnit
        this.otherExpenseTransactionUnitPrice = otherExpenseTransactionUnitPrice
        this.otherExpenseTransactionAdditionalCharges = otherExpenseTransactionAdditionalCharges
        this.otherExpenseTransactionDiscount = otherExpenseTransactionDiscount
        this.transactionAmount = otherExpenseTransactionTotalAmount
        this.otherExpenseTransactionCategory = otherExpenseTransactionCategory
        this.transactionDescription = otherExpenseTransactionNotes
        this.transactionId = transactionId
        this.transactionType = transactionType
    }


    constructor()

}