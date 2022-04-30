package com.example.appusagetracker

import android.Manifest
import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class Permission : AppCompatActivity() {

    var permissionsString = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.WAKE_LOCK,
    )
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), packageName
        )

        if (mode==AppOpsManager.MODE_ALLOWED) {
            //nextPage(AppList)
        }
        else{
            val MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 0
            startActivityForResult(
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS
            );
        }
        if (!hasPermissions(this@Permission, *permissionsString)) {
            ActivityCompat.requestPermissions(
                this@Permission, permissionsString,
                131
            )
        }

        var next=findViewById<Button>(R.id.next)
        next.setOnClickListener{
            nextPage()
        }
    }

    fun nextPage()
    {
        val loading:ProgressBar=findViewById(R.id.progressBar)
        loading.visibility=View.VISIBLE
        val startAct = Intent(this@Permission, AppList::class.java)
        startActivity(startAct)
        this.finish()
    }
    //FETCH ALL APPS
    @SuppressLint("WrongConstant")


    //GETTING PERMISSIONS
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out
        String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            131 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        this@Permission,
                        "All permission granted",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        this@Permission,
                        "Please grant all the permissions to continue.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                return
            }
            else -> {
                Toast.makeText(
                    this@Permission, "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
                this.finish()
                return
            }
        }
    }


    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        var hasAllPermissions = true
        for (permission in permissions) { /*for loop to check for every single permission*/
            val res = context.checkCallingOrSelfPermission(permission)
            if (res != PackageManager.PERMISSION_GRANTED) {
                hasAllPermissions = false
            }
        }
        return hasAllPermissions
    }

}