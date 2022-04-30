package com.example.appusagetracker

import android.app.AppOpsManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Process
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity



class SplashActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), packageName
        )

        if (mode== AppOpsManager.MODE_ALLOWED) {
            mainPage()
        }
        else{
            nextPage()
        }

    }
    fun mainPage()
    {
        Handler().postDelayed({
            val startAct = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(startAct)
            this.finish()
        }, 3000)
    }

    fun nextPage()
    {
        Handler().postDelayed({
            val startAct = Intent(this@SplashActivity, ExplainPermission::class.java)
            startActivity(startAct)
            this.finish()
        }, 3000)
    }
}