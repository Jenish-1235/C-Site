package com.csite.app.Objects

class BankTransfer {
    var date: String = ""
    var partyName: String = ""
    var amount: String = ""
    var accountNumber: String = "" ;
    var head: String = "";
    var doneBy: String = "";
    var site: String = "";

    // Constructor
    constructor(date: String, partyName: String, accountNumber: String, amount: String, head: String, doneBy: String, site: String) {
        this.date = date;
        this.partyName = partyName;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.head = head;
        this.doneBy = doneBy;
        this.site = site;
    }

    constructor(){}

}