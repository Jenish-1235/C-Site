package com.csite.app.Objects

import java.io.Serializable

class MaterialSelection : Serializable {
    var materialName: String = ""
    var materialCategory: String = ""
    var materialGST: String = ""
    var materialUnit: String = ""
    var materialId: String = ""
    var materialSelected: Boolean = false
    var materialQuantity: String = ""
    var materialUnitRate: String = ""
    var subTotal: String = ""

    constructor(materialName: String, materialCategory: String, materialGST: String, materialUnit: String, materialId: String, materialSelected: Boolean) {
        this.materialName = materialName
        this.materialCategory = materialCategory
        this.materialGST = materialGST
        this.materialUnit = materialUnit
        this.materialId = materialId
        this.materialSelected = materialSelected
        this.materialQuantity = ""
        this.materialUnitRate = ""
        this.subTotal = ""
    }

    constructor()
}