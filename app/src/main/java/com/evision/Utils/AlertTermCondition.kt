package com.evision.Utils

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import com.evision.R
import android.webkit.WebView
import android.widget.TextView


class AlertTermCondition {
    companion object{
        fun showalert(activity: Activity, message: String, string: String) {
            //  var deviceResolution:DeviceResolution?=null
            val alertDialog = Dialog(activity, R.style.Transparent)
            /*alertDialog.setTitle(activity.resources.getString(R.string.app_name))
            alertDialog.setMessage(message)*/
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view: View = LayoutInflater.from(activity).inflate(R.layout.term_condition_layout, null)
            val termtitle:TextView=view.findViewById(R.id.termtitle)
            alertDialog.setContentView(view)
            alertDialog.setCancelable(false)
            /*alertDialog.setTitle(activity.resources.getString(R.string.app_name))
            alertDialog.setMessage(message)*/
            val webView = view.findViewById(R.id.web_termcondition) as WebView
            termtitle.setText(string)
            val webSettings = webView.settings
            webSettings.javaScriptEnabled = true
            webView.loadUrl(message)
            val imgback:ImageView=view.findViewById(R.id.imgback)
            imgback.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
            /*alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            alertDialog.show()*/
        }
    }
}