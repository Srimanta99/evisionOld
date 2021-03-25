package com.evision.ContactUs

import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.evision.R
import com.evision.Utils.AppDialog


class BranchesActivity:AppCompatActivity() {
    lateinit var loader: AppDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branches)
        var toolbar: Toolbar =findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.branches)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_back)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
       val webbranches=findViewById<WebView>(R.id.web_branches)
        val webSettings = webbranches.settings
        webSettings.javaScriptEnabled = true
        loader = AppDialog(this)
        loader.show()
        webbranches.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                loader.dismiss()
            }
        })
       // webbranches.loadUrl("https://www.evisionstore.com/index.php?categoria=sucursales")
        webbranches.loadUrl("https://www.evisionstore.com/storeaddress.php");
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}