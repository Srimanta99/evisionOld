package com.evision.CartManage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.evision.R
import com.evision.Utils.EvisionLog
import com.evision.Utils.URL
import com.evision.mainpage.MainActivity
import kotlinx.android.synthetic.main.activity_payment_cedit_card.*
import kotlinx.android.synthetic.main.paymentredirect_activity.*

class PaymentRedirectUrlActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paymentredirect_activity)
        var toolbar=findViewById<Toolbar>(R.id.toolbar)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setSupportActionBar(toolbar)

        val url = intent.getStringExtra("url")
        web_url.settings.javaScriptEnabled = true
       // web_url.getSettings().setLoadWithOverviewMode(true)
        //web_url.getSettings().setUseWideViewPort(true)
       // web_url.getSettings().setBuiltInZoomControls(true)
        //web_url.getSettings().setJavaScriptEnabled(true)
        //web_url.getSettings().setAllowUniversalAccessFromFileURLs(true);
        //web_url.getSettings().setDomStorageEnabled(true);
        //web_url.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
       // WEBV.addJavascriptInterface(JavaScriptInterface(this), "HtmlViewer")

       // web_url.setWebViewClient(WebViewClient())
        web_url.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                tv_loadingtext.visibility=View.GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
//                if(Uri.parse(url).host=="www.example.com")
//                {
//                    return false
//                }
//
//                val intent= Intent(Intent.ACTION_VIEW,Uri.parse(url))
//                startActivity(intent)
//                return true

            }
        }
        web_url.setWebChromeClient(WebChromeClient())
        web_url.loadUrl(url)

    }

    override fun onBackPressed() {
       // super.onBackPressed()
//        super.onBackPressed()
           startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
           finish()

    }
    inner class JavaScriptInterface internal constructor(internal var mContext: Context) {

        fun showToast(toast: String) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }
    }
}