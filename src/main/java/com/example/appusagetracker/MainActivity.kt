package com.example.appusagetracker

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.fixedRateTimer
val CHANNEL_ID="channel_id_example_01"
var notificationId=101

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settingsPage:Button=findViewById(R.id.settings)
        settingsPage.setOnClickListener{
            settingsPage()
        }
        val statsPage:Button=findViewById(R.id.stats)
        statsPage.setOnClickListener{
            statsPage()
        }

        fixedRateTimer("timer",false,0,600000){
            this@MainActivity.runOnUiThread {
                //Toast.makeText(this@MainActivity, "text", Toast.LENGTH_SHORT).show()
                val appList= HomeAppListAdapter(dbExtract(),this@MainActivity)
                val rView = findViewById<RecyclerView>(R.id.recyclerview)
                rView.layoutManager= LinearLayoutManager(applicationContext)
                rView.itemAnimator= DefaultItemAnimator()
                rView.adapter = appList
            }
        }
        //STARTING SERVICE FOR ALERT
        startService(Intent(this, BackgroundAlertService::class.java))


    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun usageTracker(packageName: String): Long {

        val time=Calendar.getInstance()
        time.set(Calendar.HOUR_OF_DAY,0)
        time.set(Calendar.MINUTE,0)
        var totalTimeOnPhone=0.0
        var selectedappUsage=0.0

        val start=time.timeInMillis
        val end= System.currentTimeMillis()
        val usageStatsManager:UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        //app usage
        val queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,start,end)
         for (app in queryUsageStats)
        {
            totalTimeOnPhone+=(app.totalTimeInForeground / 60000)
            if(app.packageName==packageName)
            {
                selectedappUsage= (app.totalTimeInForeground / 60000).toDouble()
            }
        }
        var phoneTime=findViewById<TextView>(R.id.totalTimeOnPhone)
        phoneTime.setText("Total Time on Phone: "+(totalTimeOnPhone/60).toInt()+"hr "+(totalTimeOnPhone%60).toInt()+"min")
        return selectedappUsage.toLong()

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun dbExtract(): ArrayList<App> {

        val apps: ArrayList<App> = ArrayList()
        val db: DBHandler?
        db = DBHandler(this@MainActivity)
        val dbcur=db.readableDatabase

        val rs=dbcur.rawQuery("SELECT * FROM AppData",null)

        while(rs.moveToNext())
        {
            val x=rs.getString(0)
            val isChecked=rs.getString(2)
            val alertOn=rs.getString(3)
            val app=packageManager.getApplicationInfo(x,0)
            val appName=app.loadLabel(packageManager).toString()
            val icon=app.loadIcon(packageManager)
            val packageName=app.packageName


            if(isChecked.equals("1"))
            {
                val usageTime=usageTracker(packageName)
                apps.add(App(appName,icon,x,usageTime, alertOn))
                //Toast.makeText(this@MainActivity,isChecked, Toast.LENGTH_SHORT).show()
            }

        }
        db.close()

        return apps
    }

    fun settingsPage()
    {
        val startAct = Intent(this@MainActivity, Settings::class.java)
        startActivity(startAct)
        this.finish()
    }
    fun statsPage()
    {
        val startAct = Intent(this@MainActivity, Stats::class.java)
        startActivity(startAct)
        this.finish()
    }

}

