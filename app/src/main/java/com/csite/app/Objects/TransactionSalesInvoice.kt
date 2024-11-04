package com.csite.app.Objects

import java.util.HashMap

class TransactionSalesInvoice {
    var siDate = "";
    var siParty = "";
    var siCategory = ""
    var siAdditionalCharge:String? = null
    var siDiscount:String? = null
    var siTotal = ""
    var siNotes: String? = ""
    var siItems: HashMap<String, MaterialSelection> =  HashMap<String, MaterialSelection>();
    var siId = "";

    constructor()

    constructor(siDate: String, siParty: String, siCategory: String, siAdditionalCharge: String?, siDiscount: String?, siTotal: String, siNotes: String?, siItems: HashMap<String, MaterialSelection>){
        this.siDate = siDate;
        this.siParty = siParty;
        this.siCategory = siCategory;
        this.siAdditionalCharge = siAdditionalCharge;
        this.siDiscount = siDiscount;
        this.siTotal = siTotal;
        this.siNotes = siNotes;
        this.siItems = siItems;
        this.siId = siId;
    }

}