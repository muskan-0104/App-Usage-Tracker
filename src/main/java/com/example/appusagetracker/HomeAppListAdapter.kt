package com.example.appusagetracker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class HomeAppListAdapter(_appDetails: ArrayList<App>, context: Context) :
    RecyclerView.Adapter<HomeAppListAdapter.MyViewHolder>() {
    var appDetails: ArrayList<App>? = null
    var context=context
    init {
        this.appDetails = _appDetails
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_custom_app_list_mainactivity_adapter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appObject = appDetails?.get(position)
        holder.appName?.text = appObject?.title
        holder.icon?.setImageDrawable(appObject?.icon)
        //Go to app graph
        holder.appAdapter?.setOnClickListener {
            val intent = Intent(context, SelectedAppStats::class.java)
            intent.putExtra("package",appObject?.packages)
            intent.putExtra("appName",appObject?.title)
            intent.putExtra("alertOn",appObject?.alertOn)
            context.startActivity(intent)
        }
        if(appObject?.usageTime!! >60) {
            holder.time?.text =
                (appObject?.usageTime / 60).toString() + ":" + (appObject?.usageTime % 60) + "hr"
            println(holder.appName!!.text.toString()+"|"+appObject.usageTime+"main")
        }
        else {
            holder.time?.text = appObject?.usageTime.toString() + "min"
            println(holder.appName!!.text.toString()+"|"+appObject.usageTime+"main")

        }
    }

    override fun getItemCount(): Int {
        if (appDetails == null) {
            return 0
        }
        else {
            return (appDetails as ArrayList<String>).size
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var appName: TextView?=null
        var icon: ImageView? = null
        var time:TextView?=null
        var appAdapter:LinearLayout?=null


        init {
            appName=view.findViewById(R.id.appName)
            icon = view.findViewById(R.id.icon)
            time=view.findViewById(R.id.appUsage)
            appAdapter = view.findViewById(R.id.appData)
            }
        }

    }