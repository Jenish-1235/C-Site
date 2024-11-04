package com.cmpte.app.Objects

import com.csite.app.Objects.MaterialSelection
import java.util.HashMap

class TransactionMaterialPurchase {
    var mpDate = "";
    var mpParty = "";
    var mpCategory = ""
    var mpAdditionalCharge:String? = null
    var mpDiscount:String? = null
    var mpTotal = ""
    var mpNotes: String? = ""
    var mpItems: HashMap<String, MaterialSelection> =  HashMap<String, MaterialSelection>();
    var mpId = "";

    constructor()

    constructor(mpDate: String, mpParty: String, mpCategory: String, mpAdditionalCharge: String?, mpDiscount: String?, mpTotal: String, mpNotes: String?, mpItems: HashMap<String, MaterialSelection>){
        this.mpDate = mpDate;
        this.mpParty = mpParty;
        this.mpCategory = mpCategory;
        this.mpAdditionalCharge = mpAdditionalCharge;
        this.mpDiscount = mpDiscount;
        this.mpTotal = mpTotal;
        this.mpNotes = mpNotes;
        this.mpItems = mpItems;
        this.mpId = mpId;
    }

}