package com.evision.Utils

import android.R.id.edit
import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.LinearLayout
import com.evision.R


class AppRater(var context : Activity?) {
    val mContext: Context=context!!

    private val APP_TITLE = "Evision"// App Name
    private val APP_PNAME = mContext.getPackageName();// Package Name

    private val DAYS_UNTIL_PROMPT = 2//Min number of days
    private val LAUNCHES_UNTIL_PROMPT = 4//Min number of launches


    public fun app_launched() {
        if (ShareData(context!!).read("dontshowagain", "").equals("1")) {
            return
        }else {
            // Increment launch counter
            val launch_count = ShareData(context!!).read("launch_count", 0)!! + 1
            ShareData(context!!).write("launch_count", launch_count!!)
            // Get date of first launch
            var date_firstLaunch: Long? = ShareData(context!!).read("date_firstlaunch", 0L)
            if (date_firstLaunch!! == 0L) {
                date_firstLaunch = System.currentTimeMillis()
                ShareData(context!!).write("date_firstlaunch", date_firstLaunch)
            }

            // Wait at least n days before opening
            if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
                //  if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                // if (System.currentTimeMillis() >= date_firstLaunch!! + DAYS_UNTIL_PROMPT) {

                // if(AppPreferenceHalper.read("dontshowagain", false)!!)
                // showRateDialog(mContext)
                showalertRateusapp(mContext!!, mContext.resources.getString(R.string.app_rating))
                // }
            }
        }


    }


    fun showalertRateusapp(activity: Context, message: String) {
        val APP_PNAME =context!!.getPackageName();// Package Name
        // var deviceResolution:DeviceResolution?=null
        val alertDialog = Dialog(activity, R.style.Transparent)
        /*alertDialog.setTitle(activity.resources.getString(R.string.app_name))
        alertDialog.setMessage(message)*/
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view: View = LayoutInflater.from(activity).inflate(R.layout.alert_rating_layout, null)
        alertDialog.setContentView(view)
        alertDialog.setCancelable(false)
        val tv_message: TextView = view.findViewById(R.id.tv_message)
        val btnrateus: Button = view.findViewById(R.id.btnrateus)
        val btn_remindermelater: Button = view.findViewById(R.id.btn_remindermelater)
        val btn_nothanks: Button = view.findViewById(R.id.btn_nothanks)
        tv_message.text=message

        btnrateus.setOnClickListener {
            alertDialog.dismiss()
            activity.startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$APP_PNAME")
                    )
            )
            // activity.finish()
        }
        btn_remindermelater.setOnClickListener {
            alertDialog.dismiss()
        }
        btn_nothanks.setOnClickListener {
            alertDialog.dismiss()
            ShareData(context!!).Setvalueinpreference("1", "dontshowagain")
        }
        tv_message.setText(message)
        alertDialog.show()
        /*alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        alertDialog.show()*/
    }
}