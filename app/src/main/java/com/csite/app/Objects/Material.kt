package com.csite.app.Objects

class Material {
    var materialName: String = ""
    var materialCategory: String = ""
    var materialGST: String = ""
    var materialUnit: String = ""
    var materialId: String = ""
    var isSelected: Boolean = false

    constructor(name: String, category: String, gst: String, unit: String) {
        this.materialName = name
        this.materialCategory = category
        this.materialGST = gst
        this.materialUnit = unit
        this.materialId = ""
        this.isSelected = isSelected

    }

    constructor()

}