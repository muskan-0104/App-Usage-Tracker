package com.example.appusagetracker

class AppListModel {
    var AppInfo:String?=null
    var AlertMsg:String?=null
    var isChecked:Boolean?= null
    var AlertOff:Boolean?=null
    var Timer:String?=null


    constructor(){}

    constructor(appInfo: String, message:String,isChecked:Boolean, alertOff:Boolean,timer:String) {
        this.AppInfo=appInfo
        this.AlertMsg=message
        this.isChecked=isChecked
        this.AlertOff=alertOff
        this.Timer=timer
    }


}