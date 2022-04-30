package com.example.appusagetracker

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import java.util.*



class SelectedAppStats : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_app_stats)
        val backButton:Button=findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val startAct = Intent(this@SelectedAppStats, MainActivity::class.java)
            startActivity(startAct)
            this.finish()
        }
        //Getting app data from intent
        val packageApp=intent.getStringExtra("package")
        val intentData=intent.getStringExtra("appName")
        var alertOn=intent.getStringExtra("alertOn")
        val title:TextView=findViewById(R.id.titleOfApp)
        title.text=intentData
        setSpinner()
        if (packageApp != null) {
            usageTracker(packageApp)
        }
        val alertStop=findViewById<Button>(R.id.alertStop)
        val alertStart=findViewById<Button>(R.id.alertStart)

        alertStop.setOnClickListener {
            alertStopAction(packageApp!!)
            alertStart.visibility=View.VISIBLE
            alertStop.visibility=View.GONE

        }
        alertStart.setOnClickListener {
            alertStartAction(packageApp!!)
            alertStop.visibility=View.VISIBLE
            alertStart.visibility=View.GONE
        }
        if(alertOn.equals("1"))
        {
            alertStop.visibility=View.VISIBLE
        }
        else
        {
            alertStart.visibility=View.VISIBLE
        }

    }

    private fun alertStopAction(packageName: String) {
        val db: DBHandler?
        db = this.let { DBHandler(it) }
        db.updateAlert(false, packageName)
        Toast.makeText(this,"You won't receive alerts for this app anymore",Toast.LENGTH_SHORT).show()
        db.close()
    }

    private fun alertStartAction(packageName: String) {
        val db: DBHandler?
        db = this.let { DBHandler(it) }
        db.updateAlert(true,packageName)
        Toast.makeText(this,"You will receive alerts for this app now",Toast.LENGTH_SHORT).show()
        db.close()
    }


    private fun setSpinner() {

        val spinner: Spinner = findViewById(R.id.spinner)
        // Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.Day, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner.adapter = adapter
        spinner.onItemSelectedListener=this
            }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun usageTracker(packageName: String)
    {
        var time= Calendar.getInstance()
        time.add(Calendar.DAY_OF_MONTH,-1)
        time.set(Calendar.HOUR_OF_DAY,0)
        time.set(Calendar.MINUTE,0)

        val start=0L
        val end= System.currentTimeMillis()
        Toast.makeText(this,time.time.toString(),Toast.LENGTH_SHORT).show()

        val usageStatsManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        //app usage
        val queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,start,end)
        for (app in queryUsageStats)
        {
            if(app.packageName==packageName)
            {
                val lastUsed=Date(app.lastTimeUsed)
                val totalUsageInF=app.totalTimeInForeground / 60000
                val usageTime=findViewById<TextView>(R.id.usageTime)
                usageTime.text= totalUsageInF.toString()+" minutes "
                val lastTimeUsed=findViewById<TextView>(R.id.lastUsed)
                lastTimeUsed.text=lastUsed.toString()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected_date = parent?.getItemAtPosition(position).toString()
        println(selected_date)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}