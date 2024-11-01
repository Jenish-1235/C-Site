package com.csite.app.Objects

class Project {
    var projectId: String = "";
    var projectName: String = "";
    var projectStartDate: String = "";
    var projectStatus: String = "";
    var projectAddress: String = "";
    var projectEndDate: String = "";
    var projectValue: String = "";
    var projectCity: String = "";
    lateinit var projectMembers:HashMap<String, Member>;

    // attendance and transaction remaining.

    constructor(projectId: String, projectName: String, projectStartDate: String, projectStatus: String, projectAddress: String, projectEndDate: String, projectValue: String, projectCity: String, projectMembers: HashMap<String, Member>) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectStartDate = projectStartDate;
        this.projectStatus = projectStatus;
        this.projectAddress = projectAddress;
        this.projectEndDate = projectEndDate;
        this.projectValue = projectValue;
        this.projectCity = projectCity;
        this.projectMembers = projectMembers;
    }


    constructor()

}