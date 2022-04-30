package com.example.appusagetracker

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*
import kotlin.collections.ArrayList
var id=-1
var sum=0
class Stats : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val settingsPage: Button =findViewById(R.id.settings)
        settingsPage.setOnClickListener{
            settingsPage()
        }
        val home=findViewById<Button>(R.id.home)
        home.setOnClickListener{
            Home()
        }
        dbExtract(0)
    }

    fun Home()
    {
        val startAct = Intent(this@Stats, MainActivity::class.java)
        startActivity(startAct)
        this.finish()
    }
    fun settingsPage()
    {
        val startAct = Intent(this@Stats, Settings::class.java)
        startActivity(startAct)
        this.finish()
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
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected_date = parent?.getItemAtPosition(position).toString()
        println(selected_date)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun pieChart(appData: ArrayList<AppBarCharData>) {
        val pieChart:PieChart = findViewById(R.id.piechart)
        val entries: ArrayList<PieEntry> = ArrayList()


        for (appdata in appData) {
            val app=packageManager.getApplicationInfo(appdata.appName,0)
            val appLabel=app.loadLabel(packageManager).toString()
            var percentage=((appdata.usageTime/sum)*100)

            if(percentage>5.0f) {
                println(appLabel)
                entries.add(PieEntry(percentage,appLabel))
            }
        }

        var dataset= PieDataSet(entries,"")
        dataset.setColors(*ColorTemplate.JOYFUL_COLORS)
        var data= PieData(dataset)
        pieChart.data=data
        pieChart.setEntryLabelColor(R.color.black)
        pieChart.animateY(1000)
        //pieChart.invalidate()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun usageTracker(date_selected:Int): ArrayList<AppBarCharData> {
        val appData: ArrayList<AppBarCharData> = ArrayList()
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
            sum+=(app.totalTimeInForeground / 60000).toInt()
            id++
            val usedTime=(app.totalTimeInForeground / 60000).toFloat()
            appData.add(AppBarCharData(usedTime,id,app.packageName))

        }
        return appData

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun dbExtract(selected_date: Int) {
        var appDataNew: ArrayList<AppBarCharData>
        appDataNew=usageTracker(selected_date)
        setSpinner()
        pieChart(appDataNew)
    }


}