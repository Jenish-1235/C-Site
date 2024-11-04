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

    constructor(name: String, category: String, gst: String, unit: String, materialId: String, materialSelected: Boolean) {
        this.materialName = name
        this.materialCategory = category
        this.materialGST = gst
        this.materialUnit = unit
        this.materialId = materialId
        this.materialSelected = materialSelected
        this.materialQuantity = ""
        this.materialUnitRate = ""
        this.subTotal = ""
    }

    constructor()
}