package com.csite.app.Objects

class PartyPayForProjects {
    var partyId = ""
    var payAmount = "0"
    var partyProjectId = ""
    var partyName = ""

    constructor(partyId: String, payAmount:String, partyProjectId:String, partyName:String) {
        this.partyId = partyId
        this.payAmount = payAmount
        this.partyProjectId = partyProjectId
        this.partyName = partyName
    }
}