package com.csite.app.Objects

import java.util.HashMap

class TransactionSalesInvoice {
    var transactionDate = "";
    var transactionParty = "";
    var siCategory = ""
    var siAdditionalCharge:String? = null
    var siDiscount:String? = null
    var transactionAmount = ""
    var transactionDescription: String? = ""
    var siItems: HashMap<String, MaterialSelection> =  HashMap<String, MaterialSelection>();
    var transactionId = "";
    var transactionType = "Sales Invoice"

    constructor()

    constructor(siDate: String, siParty: String, siCategory: String, siAdditionalCharge: String?, siDiscount: String?, siTotal: String, siNotes: String?, siItems: HashMap<String, MaterialSelection>){
        this.transactionDate = siDate;
        this.transactionParty = siParty;
        this.siCategory = siCategory;
        this.siAdditionalCharge = siAdditionalCharge;
        this.siDiscount = siDiscount;
        this.transactionAmount = siTotal;
        this.transactionDescription = siNotes;
        this.siItems = siItems;
        this.transactionId = transactionId;
        this.transactionType = transactionType;
    }

}