package com.example.appusagetracker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.concurrent.fixedRateTimer

//BACKGROUND THREAD PART//
class BackgroundAlertService: Service()
{
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        notificationCreator(CHANNEL_ID)

        fixedRateTimer("timers",false,0,900000){
           dbExtract()
        }
        return super.onStartCommand(intent, flags, startId)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun dbExtract() {
        val db: DBHandler?
        db = DBHandler(this@BackgroundAlertService)
        val dbcur=db.readableDatabase

        val rs=dbcur.rawQuery("SELECT * FROM AppData",null)

        while(rs.moveToNext())
        {
            val x=rs.getString(0)
            val msg=rs.getString(1)
            val isChecked=rs.getString(2)
            val alertOn=rs.getString(3)
            val timer=rs.getString(4).toInt()

            val app=packageManager.getApplicationInfo(x,0)
            val appName=app.loadLabel(packageManager).toString()
            val packageName=app.packageName

            if(isChecked.equals("1"))
            {
                val usageTime=usageTracker(packageName)
                println(appName+"|"+usageTime)
                if (usageTime > timer && alertOn.equals("1"))
                {
                    //Toast.makeText(this@MainActivity, appName, Toast.LENGTH_SHORT).show()
                    sendNotification(CHANNEL_ID,notificationId++,msg,appName)
                }
            }

        }
        db.close()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun usageTracker(packageName: String?): Long {
        val time= Calendar.getInstance()
        time.set(Calendar.HOUR_OF_DAY,0)
        time.set(Calendar.MINUTE,0)

        val start=time.timeInMillis
        val end= System.currentTimeMillis()
        val usageStatsManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        //app usage
        val queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,start,end)
        for (app in queryUsageStats)
        {
            if(app.packageName==packageName)
            {
                return app.totalTimeInForeground / 60000
            }
        }
        return 0
    }

    fun sendNotification(CHANNEL_ID: String, notificationId: Int, msg: String, appName: String)
    {
        val builder= NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(appName)
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId,builder.build())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun notificationCreator(CHANNEL_ID: String) {
        val name="Notification Title"
        val descriptionText="Notification Description"
        val importance= NotificationManager.IMPORTANCE_DEFAULT
        val channel= NotificationChannel(CHANNEL_ID,name,importance).apply {
            description=descriptionText
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}