package com.csite.app.Objects

class Workforce {
    var workforceType: String = ""
    var workforceSalaryPerShift = ""
    var workforceCategory: String = ""
    var workforceId: String = ""

    constructor(workforceType: String, workforceSalaryPerShift: String, workforceCategory: String) {
        this.workforceType = workforceType
        this.workforceSalaryPerShift = workforceSalaryPerShift
        this.workforceCategory = workforceCategory
        this.workforceId = ""
    }
    constructor()
}