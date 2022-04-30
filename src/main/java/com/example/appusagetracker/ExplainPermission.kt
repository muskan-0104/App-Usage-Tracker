package com.example.appusagetracker

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.audiofx.BassBoost
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class ExplainPermission : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explain_permission)

        var next=findViewById<Button>(R.id.next)
        next.setOnClickListener{
            val startAct = Intent(this@ExplainPermission, Permission::class.java)
            startActivity(startAct)
            this.finish()
        }

    }

}
