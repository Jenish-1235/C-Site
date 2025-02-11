package com.csite.app.Objects

class InventoryItem {
    var materialName = ""
    var materialCategory = ""
    var materialId = ""
    var totalQuantity = 0
    var materialUnit = ""

    constructor()

    constructor(materialName: String, materialCategory: String, materialId: String, totalQuantity: Int, materialUnit: String) {
        this.materialName = materialName
        this.materialCategory = materialCategory
        this.materialId = materialId
        this.totalQuantity = totalQuantity
        this.materialUnit = materialUnit
    }

}