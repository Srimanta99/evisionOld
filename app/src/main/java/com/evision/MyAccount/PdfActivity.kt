package com.evision.MyAccount

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.evision.R
import kotlinx.android.synthetic.main.activity_pdf.*
import kotlinx.android.synthetic.main.activity_product_details.*

class PdfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        var toolbar: Toolbar =findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.setTitleTextColor(Color.WHITE)
        val myPdfUrl=intent.getStringExtra("pdf")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_back)
        val progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        web_view.requestFocus()
      //  web_view.getSettings().setJavaScriptEnabled(true)
       // val myPdfUrl = "gymnasium-wandlitz.de/vplan/vplan.pdf"
        web_view.webViewClient = WebViewClient()
        web_view.settings.setSupportZoom(true)
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setSupportMultipleWindows(true)
        web_view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        web_view.getSettings().setAllowFileAccess(true);
        web_view.getSettings().setSupportMultipleWindows(true);
        val url = "https://docs.google.com/viewer?embedded=true&url=$myPdfUrl"
        web_view.loadUrl(url)
        web_view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                var overrideUrlLoading = false;
                    view?.loadUrl(url)

                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("tag","url overrride url  = "+ url);
                progressDialog.dismiss()
            }

        }



    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}