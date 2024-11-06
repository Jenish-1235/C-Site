package com.cmpte.app.Objects

import com.csite.app.Objects.MaterialSelection
import java.util.HashMap

class TransactionMaterialPurchase {
    var transactionDate = "";
    var transactionParty = "";
    var mpCategory = ""
    var mpAdditionalCharge:String = ""
    var mpDiscount:String = ""
    var transactionAmount = ""
    var transactionDescription: String = ""
    var mpItems: HashMap<String, MaterialSelection> =  HashMap<String, MaterialSelection>();
    var transactionId = "";
    var transactionType = "Material Purchase";

    constructor()

    constructor(mpDate: String, mpParty: String, mpCategory: String, mpAdditionalCharge: String, mpDiscount: String, mpTotal: String, mpNotes: String, mpItems: HashMap<String, MaterialSelection>){
        this.transactionDate = mpDate;
        this.transactionParty = mpParty;
        this.mpCategory = mpCategory;
        this.mpAdditionalCharge = mpAdditionalCharge;
        this.mpDiscount = mpDiscount;
        this.transactionAmount = mpTotal;
        this.transactionDescription = mpNotes;
        this.mpItems = mpItems;
        this.transactionId = transactionId;
        this.transactionType = transactionType;
    }

}