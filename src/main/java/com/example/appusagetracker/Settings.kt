package com.example.appusagetracker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var home=findViewById<Button>(R.id.home)
        home.setOnClickListener{
            Home()
        }
        val statsPage:Button=findViewById(R.id.stats)
        statsPage.setOnClickListener{
            statsPage()
        }

        val SAdapter= SettingsAdapter(dbExtract(),this@Settings)

        var rView = findViewById<RecyclerView>(R.id.recyclerviewSettings)
        rView.layoutManager= LinearLayoutManager(this)
        rView.itemAnimator= DefaultItemAnimator()
        rView.adapter = SAdapter


    }
    private fun dbExtract(): ArrayList<SelectedApp> {
        var db: DBHandler? =null
        db = DBHandler(this@Settings)
        var dbcur=db.readableDatabase

        var rs=dbcur.rawQuery("SELECT * FROM AppData",null)
        val apps: ArrayList<SelectedApp> = ArrayList()
        while(rs.moveToNext())
        {
            var x=rs.getString(0)
            var isChecked=rs.getString(2)
            var app=packageManager.getApplicationInfo(x,0)
            var appName=app.loadLabel(packageManager).toString()
            var icon=app?.loadIcon(packageManager)

                apps.add(SelectedApp(appName, icon,x,isChecked))
                //Toast.makeText(this@Settings,isChecked, Toast.LENGTH_SHORT).show()
        }
        db.close()
        return apps
    }
    fun Home()
    {
        val startAct = Intent(this@Settings, MainActivity::class.java)
        startActivity(startAct)
        this.finish()
    }
    fun statsPage()
    {
        val startAct = Intent(this@Settings, Stats::class.java)
        startActivity(startAct)
        this.finish()
    }
}