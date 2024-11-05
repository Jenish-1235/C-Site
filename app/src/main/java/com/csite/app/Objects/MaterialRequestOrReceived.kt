package com.csite.app.Objects

class MaterialRequestOrReceived {
    var type:String = ""
    var dateTimeStamp = ""
    var materialName = ""
    var materialQuantity = "0"
    var materialId = ""
    var materialUnit = ""
    var materialCategory = ""

    constructor()

    constructor(type:String,dateTimeStamp:String,materialName:String,materialQuantity:String,materialId:String,materialUnit:String,materialCategory:String){
        this.type = type
        this.dateTimeStamp = dateTimeStamp
        this.materialName = materialName
        this.materialQuantity = materialQuantity
        this.materialId = materialId
        this.materialUnit = materialUnit
        this.materialCategory = materialCategory
    }

}