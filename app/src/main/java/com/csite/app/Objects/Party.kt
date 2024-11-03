package com.csite.app.Objects

import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary

class Party {
    // Party Details
    var partyName = ""
    var partyMobileNumber = ""
    var partyType = ""
    var partyCondition = ""

    // Party will pay and will receive
    var partyAmountToPayOrReceive:String? = null
    var partyOpeningBalanceDetails:String? = null

    // Party with GST Details
    var partyGSTNumber:String? = null
    var partyLegalBusinessName:String? = null
    var partyStateOfSupply:String? = null
    var partyBillingAddress:String? = null

    // Party with Bank Details
    var partyAccountHolderName:String? = null
    var partyAccountNumber:String? = null
    var partyIFSCCode:String? = null
    var partyBankName:String? = null
    var partyBankAddress:String? = null
    var partyIBANNumber:String? = null
    var partyUPIId:String? = null

    // partyId
    var partyId:String = ""

    // 000 : Party Details
    constructor(partName:String , partyMobileNumber:String, partyType:String, partyCondition:String){
        this.partyName = partName
        this.partyMobileNumber = partyMobileNumber
        this.partyType = partyType
        this.partyCondition = partyCondition
        this.partyId = ""
    }

    // 100: Party have opening balance details
    constructor(partName: String, partyMobileNumber: String, partyType: String, partyCondition: String, partyAmountToPayOrReceive: String, partyOpeningBalanceDetails: String){
        this.partyName = partName
        this.partyMobileNumber = partyMobileNumber
        this.partyType = partyType
        this.partyCondition = partyCondition
        this.partyAmountToPayOrReceive = partyAmountToPayOrReceive
        this.partyOpeningBalanceDetails = partyOpeningBalanceDetails
        this.partyId = ""
    }

    // 010 : Party with GST Details
    constructor(partName: String, partyMobileNumber: String, partyType: String, partyCondition: String, partyGSTNumber: String, partyLegalBusinessName: String, partyStateOfSupply: String, partyBillingAddress: String){
        this.partyName = partName
        this.partyMobileNumber = partyMobileNumber
        this.partyType = partyType
        this.partyCondition = partyCondition
        this.partyGSTNumber = partyGSTNumber
        this.partyLegalBusinessName = partyLegalBusinessName
        this.partyStateOfSupply = partyStateOfSupply
        this.partyBillingAddress = partyBillingAddress
        this.partyId = ""
    }

    // 001 : Party with Bank Details
    constructor(partName: String, partyMobileNumber: String, partyType: String, partyCondition: String, partyAccountHolderName: String, partyAccountNumber: String, partyIFSCCode: String, partyBankName: String, partyBankAddress: String, partyIBANNumber: String, partyUPIId: String){
        this.partyName = partName
        this.partyMobileNumber = partyMobileNumber
        this.partyType = partyType
        this.partyCondition = partyCondition
        this.partyAccountHolderName = partyAccountHolderName
        this.partyAccountNumber = partyAccountNumber
        this.partyIFSCCode = partyIFSCCode
        this.partyBankName = partyBankName
        this.partyBankAddress = partyBankAddress
        this.partyIBANNumber = partyIBANNumber
        this.partyUPIId = partyUPIId
        this.partyId = ""
    }

    // 110 : Party with opening balance details and gst
    constructor(partName: String, partyMobileNumber: String, partyType: String, partyCondition: String, partyAmountToPayOrReceive: String, partyOpeningBalanceDetails: String, partyGSTNumber: String, partyLegalBusinessName: String, partyStateOfSupply: String, partyBillingAddress: String){
        this.partyName = partName
        this.partyMobileNumber = partyMobileNumber
        this.partyType = partyType
        this.partyCondition = partyCondition
        this.partyAmountToPayOrReceive = partyAmountToPayOrReceive
        this.partyOpeningBalanceDetails = partyOpeningBalanceDetails
        this.partyGSTNumber = partyGSTNumber
        this.partyLegalBusinessName = partyLegalBusinessName
        this.partyStateOfSupply = partyStateOfSupply
        this.partyBillingAddress = partyBillingAddress
        this.partyId = ""
    }

    // 101 : Party with opening balance details and bank details
    constructor(partName: String, partyMobileNumber: String, partyType: String, partyCondition: String, partyAmountToPayOrReceive: String, partyOpeningBalanceDetails: String, partyAccountHolderName: String, partyAccountNumber: String, partyIFSCCode: String, partyBankName: String, partyBankAddress: String, partyIBANNumber: String, partyUPIId: String){
        this.partyName = partName
        this.partyMobileNumber = partyMobileNumber
        this.partyType = partyType
        this.partyCondition = partyCondition
        this.partyAmountToPayOrReceive = partyAmountToPayOrReceive
        this.partyOpeningBalanceDetails = partyOpeningBalanceDetails
        this.partyAccountHolderName = partyAccountHolderName
        this.partyAccountNumber = partyAccountNumber
        this.partyIFSCCode = partyIFSCCode
        this.partyBankName = partyBankName
        this.partyBankAddress = partyBankAddress
        this.partyIBANNumber = partyIBANNumber
        this.partyUPIId = partyUPIId
        this.partyId = ""
    }

    // 111 : Party with opening balance details, gst and bank details  //// Super Constructor....
    constructor(partName: String, partyMobileNumber: String, partyType: String, partyCondition: String, partyAmountToPayOrReceive: String, partyOpeningBalanceDetails: String, partyGSTNumber: String, partyLegalBusiness: String, partyStateOfSupply: String, partyBillingAddress: String, partyAccountHolderName: String, partyAccountNumber: String, partyIFSCCode: String, partyBankName: String, partyBankAddress: String, partyIBANNumber: String, partyUPIId: String){
        this.partyName = partName
        this.partyMobileNumber = partyMobileNumber
        this.partyType = partyType
        this.partyCondition = partyCondition
        this.partyAmountToPayOrReceive = partyAmountToPayOrReceive
        this.partyOpeningBalanceDetails = partyOpeningBalanceDetails
        this.partyGSTNumber = partyGSTNumber
        this.partyLegalBusinessName = partyLegalBusiness
        this.partyStateOfSupply = partyStateOfSupply
        this.partyBillingAddress = partyBillingAddress
        this.partyAccountHolderName = partyAccountHolderName
        this.partyAccountNumber = partyAccountNumber
        this.partyIFSCCode = partyIFSCCode
        this.partyBankName = partyBankName
        this.partyBankAddress = partyBankAddress
        this.partyIBANNumber = partyIBANNumber
        this.partyUPIId = partyUPIId
        this.partyId = ""
    }

    // 011 : Party with GST and Bank Details
    constructor(partName: String, partyMobileNumber: String, partyType: String, partyCondition: String, partyGSTNumber: String, partyLegalBusinessName: String, partyStateOfSupply: String, partyBillingAddress: String, partyAccountHolderName: String, partyAccountNumber: String, partyIFSCCode: String, partyBankName: String, partyBankAddress: String, partyIBANNumber: String, partyUPIId: String){
        this.partyName = partName
        this.partyMobileNumber = partyMobileNumber
        this.partyType = partyType
        this.partyCondition = partyCondition
        this.partyGSTNumber = partyGSTNumber
        this.partyLegalBusinessName = partyLegalBusinessName
        this.partyStateOfSupply = partyStateOfSupply
        this.partyBillingAddress = partyBillingAddress
        this.partyAccountHolderName = partyAccountHolderName
        this.partyAccountNumber = partyAccountNumber
        this.partyIFSCCode = partyIFSCCode
        this.partyBankName = partyBankName
        this.partyBankAddress = partyBankAddress
        this.partyIBANNumber = partyIBANNumber
        this.partyUPIId = partyUPIId
        this.partyId = ""
    }

    // empty constructor
    constructor()

    fun updateData(partyId: String, party: Party) {
        val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        firebaseOperationsForLibrary.updateParty(partyId, party)
    }











}