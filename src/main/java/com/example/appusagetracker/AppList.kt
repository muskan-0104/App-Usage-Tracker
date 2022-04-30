package com.example.appusagetracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AppList : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_list)
        val AppList= AppListAdapter(getApp(),this@AppList)


        val rView = findViewById<RecyclerView>(R.id.AppList)
        rView.layoutManager = LinearLayoutManager(this)
        rView.itemAnimator = DefaultItemAnimator()
        rView.adapter = AppList



        val next=findViewById<Button>(R.id.next)
        next.setOnClickListener({
            nextPage()
        })
    }
    fun homePage()
    {
        val startAct = Intent(this@AppList, MainActivity::class.java)
        startActivity(startAct)
        this.finish()
    }
    fun nextPage()
    {
        val startAct = Intent(this@AppList, MainActivity::class.java)
        startActivity(startAct)
        this.finish()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("WrongConstant")
    fun getApp():ArrayList<App>{

        val pm = packageManager
        val apps: ArrayList<App> = ArrayList()
        //thread

            val packs = packageManager.getInstalledApplications(PackageManager.GET_PERMISSIONS)
            for (i in packs) {
                if (pm.getLaunchIntentForPackage(i.packageName) != null) {
                    val appName = i.loadLabel(packageManager).toString()
                    val icon = i.loadIcon(packageManager)
                    val packages = i.packageName
                    apps.add(App(appName,icon, packages,0,"1"))
                    println(appName)
                    dbAdd(this,packages,"Don't Use Phone",false,"30")
                }
            }
        return apps
    }

    private fun dbAdd(con: Context?,
                      appDetails: String?,
                      msg: String,
                      isChecked: Boolean,
                      timer: String)
    {
        val db: DBHandler?
        db = con?.let { DBHandler(it) }

        val app_data=AppListModel(appDetails.toString(),msg,isChecked,true,timer)
        try {
            db?.addApp(app_data)
        }catch (ex:Exception)
        { }
    }

}


