package com.csite.app.Objects

data class ProjectWorker(
    var wParty: String = "",
    var wName: String = "",
    var wCategory: String = "",
    var wNoOfWorker: String = "",
    var wNoOfShifts: String = "",
    var wIsOvertime: Boolean = false,
    var wIsLate: Boolean = false,
    var wOvertimeAmount: String = "",
    var wLateAmount: String = "",
    var wIsAllowance: Boolean = false,
    var wIsDeduction: Boolean = false,
    var wAllowanceAmount: String = "",
    var wDeductionAmount: String = "",
    var wNote: String = "",
    var wSalaryPerDay: String = "",
    var wId: String = "",
    var wIsSelected: Boolean = false,
)