package com.site.app.Objects

import com.csite.app.Objects.MaterialSelection
import java.util.HashMap

class Quotation {
    var quoteDate = "";
    var quoteParty = "";
    var quoteCategory = ""
    var quoteAdditionalCharge:String? = null
    var quoteDiscount:String? = null
    var quoteTotal = ""
    var quoteNotes: String? = ""
    var quoteItems: HashMap<String, MaterialSelection> =  HashMap<String, MaterialSelection>();
    var quoteId = "";

    constructor()

    constructor(quoteDate: String, quoteParty: String, quoteCategory: String, quoteAdditionalCharge: String?, quoteDiscount: String?, quoteTotal: String, quoteNotes: String?, quoteItems: HashMap<String, MaterialSelection>){
        this.quoteDate = quoteDate;
        this.quoteParty = quoteParty;
        this.quoteCategory = quoteCategory;
        this.quoteAdditionalCharge = quoteAdditionalCharge;
        this.quoteDiscount = quoteDiscount;
        this.quoteTotal = quoteTotal;
        this.quoteNotes = quoteNotes;
        this.quoteItems = quoteItems;
        this.quoteId = quoteId;
    }

}