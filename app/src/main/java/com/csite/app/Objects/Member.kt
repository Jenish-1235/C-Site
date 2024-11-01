package com.csite.app.Objects

class Member {

    // Constructor with parameters

    var name : String = ""
    var mobileNumber : String = ""
    var password : String = ""
    var memberAccess : String = ""

    constructor(name: String, mobileNumber: String, password: String, memberAccess: String) {
        this.name = name
        this.mobileNumber = mobileNumber
        this.password = password
        this.memberAccess = memberAccess
    }

    constructor(name: String, mobileNumber: String, memberAccess: String){
        this.name = name
        this.mobileNumber = mobileNumber
        this.memberAccess = memberAccess

    }
    constructor()

}