package com.csite.app.Objects

class CommonTransaction {
    var transactionParty: String = ""
    var transactionDate: String = ""
    var transactionDescription:String = ""
    var transactionAmount: String = ""
    var transactionType: String = ""
    var transactionId: String = ""

    constructor()

    constructor(transactionParty: String , transactionDate: String , transactionDescription: String, transactionAmount: String,  transactionType: String, transactionId:String){
        this.transactionId = transactionId
        this.transactionDate = transactionDate
        this.transactionType = transactionType
        this.transactionAmount = transactionAmount
        this.transactionDescription = transactionDescription
        this.transactionParty = transactionParty
    }
}