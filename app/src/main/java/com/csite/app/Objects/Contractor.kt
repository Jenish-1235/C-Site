package com.csite.app.Objects

data class Contractor(
    var contractorName: String = "",
    var contractorId: String = "",
    var contractorPhoneNumber:String = "",
    var contractorWorkforce: HashMap<String, Workforce> = HashMap<String, Workforce>()
)
