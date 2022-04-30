package com.example.appusagetracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VER) {

    companion object{
        var DATABASE_NAME="AppListModel"
        var DATABASE_VER=1

        private val Table="AppData"
        private val COL_AppInfo="AppInfo"
        private val COL_AlertMsg="AlertMsg"
        private val COL_Timer="Timer"
        private val COL_AlertOn="AlertOn"
        private val COL_IsChecked="isChecked"


    }


    override fun onCreate(db: SQLiteDatabase?) {
        val  CREATE_TABLE_QUERY:String="CREATE TABLE $Table (" +
                COL_AppInfo + " TEXT PRIMARY KEY," +
                COL_AlertMsg+" TEXT,"+
                COL_IsChecked+" TEXT,"+
                COL_AlertOn+" TEXT,"+
                COL_Timer+ " TEXT);"

        if (db != null) {
            db.execSQL(CREATE_TABLE_QUERY)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $Table")
        onCreate(db!!)
        db.close()
    }

    //CRUD

    fun addApp(applistmodel: AppListModel) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_AppInfo, applistmodel.AppInfo)
        values.put(COL_AlertMsg,applistmodel.AlertMsg)
        values.put(COL_Timer,applistmodel.Timer)
        values.put(COL_AlertOn,applistmodel.AlertOff)
        values.put(COL_IsChecked,applistmodel.isChecked)

        try {
            db.insert(Table, null, values)
        }catch (ex:Exception){ print("Unique Contraint failed")}

        db.close()
    }

    fun deleteTask(_id: String): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(Table, COL_AppInfo + "=?", arrayOf(_id)).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }
    fun updateApp(applistmodel: AppListModel)
    {
        // calling a method to get writable database.
        val db = this.writableDatabase
        val values = ContentValues()

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(COL_AppInfo, applistmodel.AppInfo)
        values.put(COL_AlertMsg,applistmodel.AlertMsg)
        values.put(COL_Timer,applistmodel.Timer)
        values.put(COL_IsChecked,applistmodel.isChecked)


        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(Table, values, "AppInfo=?", arrayOf(applistmodel.AppInfo))
        db.close()
    }
    fun updateAlert(alertStatus:Boolean,packageName:String)
    {
        // calling a method to get writable database.
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_AlertOn,alertStatus)
        db.update(Table, values, "AppInfo=?", arrayOf(packageName))
        db.close()
    }

}