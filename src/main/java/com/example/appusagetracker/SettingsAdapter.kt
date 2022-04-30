package com.example.appusagetracker

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class SettingsAdapter(_appDetails: ArrayList<SelectedApp>, con:Context) :
    RecyclerView.Adapter<SettingsAdapter.MyViewHolder>() {
    var appDetails: ArrayList<SelectedApp>? = null
    var con: Context? =null
    var pos: Int? =0
    init {
        this.appDetails = _appDetails
        this.con=con
        this.pos=0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val appObject = appDetails?.get(position)
        holder.appTitle?.text = appObject?.title
        holder.icon?.setImageDrawable(appObject?.icon)
        pos=position
        //check if app is already selected
        if(appObject?.isChecked=="1")
        {
            val extrabox=holder.extrabox
            holder.appTitle?.isChecked=true
            extrabox?.visibility = View.VISIBLE
        }
        //switch click listener
        holder.appTitle!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val extrabox=holder.extrabox
                extrabox?.visibility = View.VISIBLE
                dbUpdate(con,appObject?.packages, holder.msg!!, holder.timer!!,isChecked)
                Toast.makeText(con,holder.appTitle!!.text.toString()+" Checked",Toast.LENGTH_SHORT).show()
            }
            else{
                val extrabox=holder.extrabox
                extrabox?.visibility=View.GONE
                dbUpdate(con,appObject?.packages,"No Value", holder.timer!!,isChecked)
                Toast.makeText(con,holder.appTitle!!.text.toString()+" UnChecked",Toast.LENGTH_SHORT).show()

            }
        }

    }


    fun dbUpdate(con: Context?, appDetails: String?, msg: String, timer: String, isChecked: Boolean)
    {
        val db: DBHandler?
        db = con?.let { DBHandler(it) }

        val app_data=AppListModel(appDetails.toString(),msg,isChecked,true,timer)
        db?.updateApp(app_data)

    }

    /*This has the same implementation which we did for the navigation drawer adapter*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_custom_app_list_adapter, parent, false)
        return MyViewHolder(itemView,appDetails,pos)
    }
    override fun getItemCount(): Int {
        if (appDetails == null) {
            return 0
        }
        else {
            return (appDetails as ArrayList<App>).size
        }
    }
    @SuppressLint("WrongConstant", "CutPasteId")
    class MyViewHolder(view: View, appDetails: ArrayList<SelectedApp>?, pos: Int?) : RecyclerView.ViewHolder(view) {
        var appTitle: SwitchCompat? = null
        var icon:ImageView?=null
        val appObject = appDetails?.get(pos!!)
        var extrabox:LinearLayout?=null
        var input:TextInputEditText?=null
        var time:TextInputEditText?=null
        var msg: String?=null
        var timer:String?=null
        //database operation to fetch isChcked value for each app

        init {
            appTitle = view.findViewById(R.id.switchtext) as SwitchCompat
            icon=view.findViewById(R.id.icon)
            //Databse related work starts here
            extrabox=view.findViewById(R.id.extrabox) as LinearLayout
            input=view.findViewById(R.id.textInput) as TextInputEditText
            time=view.findViewById(R.id.Timer) as TextInputEditText
            msg= input!!.getText().toString()
            timer=time!!.getText().toString()

        }

    }
}
