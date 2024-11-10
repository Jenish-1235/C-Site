package com.csite.app.Objects

data class Workforce(
    var workforceType:String = "",
    var workforceSalaryPerDay:String = "",
    var workforceCategory:String = "",
    var workforceNumberOfWorkers:String = "0",
    var workforceId:String = "",
    var workforceIsLate:Boolean = false,
    var workforceIsOverTime:Boolean = false,
    var workforceOverTimePay:String = "0",
    var workforceLateFine:String = "0",
    var workforceHasAllowance:Boolean = false,
    var workforceAllowance:String = "0",
    var workforceHasDeduction:Boolean = false,
    var workforceDeduction:String = "0",
    var workforceNotes:String = "",
    var workforcePresentWorkers:String = "0",
    var workforceAbsentWorkers:String = "0"
)
