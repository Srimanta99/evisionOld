package com.evision.mainpage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.*
import android.widget.Button

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.evision.CartManage.CartActivity
import com.evision.Category.CategoryActivity
import com.evision.ContactUs.ContactUsActivity
import com.evision.Dashboard.DashboardFragment
import com.evision.Login_Registration.LoginActivity
import com.evision.MyAccount.MyAccActivity
import com.evision.ProductList.ProductListActivity
import com.evision.R
import com.google.android.material.navigation.NavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jsoup.Jsoup
import java.io.IOException
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.evision.ContactUs.BranchesActivity
import com.evision.ContactUs.PrivacySaleActivity
import com.evision.ContactUs.TermConditionSaleActivity
import com.evision.Login_Registration.Pojo.LoginResponse
import com.evision.Utils.*
import com.evision.contactinfo.ContactInfoActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainContarctor.Main_View,BottomNavigationView.OnNavigationItemSelectedListener {
    private val REQ_LOGIN: Int = 12

    var token=""
    //lateinit var  nav_signout:MenuItem
companion object{
    var isReadyforCourtCount=false
    lateinit var  nav_signout:MenuItem
        lateinit var  menuCartItem:MenuItem
}
    lateinit var TXT_username: TextView
    lateinit var TXT_useremail: TextView
    lateinit var nav_view:NavigationView
    lateinit var drawer_layout:DrawerLayout
    lateinit var SAMSUNG_INSTALLATION:TextView
     lateinit var mFirebaseAnalytics:FirebaseAnalytics
    private val TAG = " FCM TOKEN"
    var currentVersion: String? = null
    val REQUEST_READ_PHONE_STATE:Int=222
    var imei:String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        /*var  bundle= Bundle()
bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "uu");
bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "vghj");
bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);*/
        var toolbar:Toolbar=findViewById(R.id.toolbar)
        nav_view=findViewById(R.id.nav_view)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setLogo(R.drawable.logo_24_93)
        drawer_layout=findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer_layout,toolbar , R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        nav_view.getMenu().getItem(1).setActionView(R.layout.next_menu)
        nav_view.itemIconTintList=null
        val btncategory:Button=toolbar.findViewById(R.id.btncategory)
        btncategory.setOnClickListener {
           // Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,CategoryActivity::class.java))
        }

        TXT_username = nav_view.getHeaderView(0).findViewById(R.id.TXT_Login)
        TXT_useremail = nav_view.getHeaderView(0).findViewById(R.id.TXT_EMAIL)
        SAMSUNG_INSTALLATION=nav_view.getHeaderView(0).findViewById(R.id.SAMSUNG_INSTALLATION)
        SAMSUNG_INSTALLATION.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            val browserIntent:Intent =  Intent(Intent.ACTION_VIEW, Uri.parse("https://www.samsung.com/latin/support/self-installation-online-guides/"))
            startActivity(browserIntent)

        }
        hideItem()
        supportFragmentManager.beginTransaction().replace(R.id.container, DashboardFragment.newInstance("", "")).commit()
        TXT_username.setOnClickListener {
            startActivityForResult(Intent(this, LoginActivity::class.java), REQ_LOGIN)
        }
        getCurrentVersion()
      //  FirebaseMessaging.getInstance().isAutoInitEnabled = true
        gettokenfromfcm()


    }

    @SuppressLint("MissingPermission")
    private fun gettokenfromfcm() {
        //  val TelephonyMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        //   val imei = TelephonyMgr.deviceId
        //  val imei= Settings.Secure.getString(context.getContenResolver(), InstanceID.getInstance(context).getId());
        //  AppPreferenceHalper.write(PreferenceConstantParam.DEVICE_IMEI,imei.toString())
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                     token = task.result?.token!!
                    getdeviceimeino()
                    // Log and toast
                    // val msg = getString("tdsd", token)
                    Log.d(TAG, token)
                    // Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
                })
    }

    @SuppressLint("MissingPermission")
    private fun getdeviceimeino() {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            imei= ""
            callApiforupdatetoken()
           // ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.READ_PHONE_STATE), REQUEST_READ_PHONE_STATE)
        } else {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID).toString();
               // AppPreferenceHalper.write(PreferenceConstantParam.DEVICE_IMEI,imei.toString())
                callApiforupdatetoken()
            }else {
                val TelephonyMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                imei = TelephonyMgr.deviceId.toString()
                callApiforupdatetoken()
              //  AppPreferenceHalper.write(PreferenceConstantParam.DEVICE_IMEI,imei.toString())
            }

           // gettokenfromfcm()
        }
    }

    private fun getCurrentVersion() {
        val pm = this.packageManager
        var pInfo: PackageInfo? = null

        try {
            pInfo = pm.getPackageInfo(this.packageName, 0)
        } catch (e1: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
        currentVersion = pInfo!!.versionName
        GetVersionCode().execute()

    }

    internal inner class GetVersionCode : AsyncTask<Void, String, String>() {

        override fun doInBackground(vararg voids: Void): String? {
            var newVersion: String? = null
            try {
                val document =
                        Jsoup.connect("https://play.google.com/store/apps/details?id=" + this@MainActivity.packageName + "&hl=en")
                                //  Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.app.astrolab"  + "&hl=en")
                                .timeout(30000)
                                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                .referrer("http://www.google.com")
                                .get()
                if (document != null) {
                    val element = document!!.getElementsContainingOwnText("Current Version")
                    for (ele in element) {
                        if (ele.siblingElements() != null) {
                            val sibElemets = ele.siblingElements()
                            for (sibElemet in sibElemets) {
                                newVersion = sibElemet.text()
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return newVersion

        }


        override fun onPostExecute( onlineVersion: String?) {

            super.onPostExecute(onlineVersion)
           // var onlineVersion="2"
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (java.lang.Float.valueOf(currentVersion) < java.lang.Float.valueOf(onlineVersion)) {
                    //show anything
                    showUpdateDialog()
                }

            }

            // Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);

        }
    }


    public fun updateStatusBarColor(){// Color must be in hexadecimal fromat

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this!!, R.color.colorPrimaryDark));
    }
   }
    private fun showUpdateDialog() {
        var dialog: Dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.newversionavalible))
        builder.setPositiveButton(resources.getText(R.string.update)
        ) { dialog, which ->
            /* startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                            ("market://details?id=com.wecompli")));*/
            val appPackageName = packageName // getPackageName() from Context or Activity object
            try {
                startActivity(
                        Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$appPackageName")
                        )
                )
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(
                        Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                        )
                )
            }

            dialog.dismiss()
        }

        builder.setNegativeButton(
                resources.getString(R.string.cancel),
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        dialog.dismiss()
                    }
                })

        builder.setCancelable(true)
        dialog = builder.show()
        dialog.setCancelable(false)
    }

    private fun LOGINMANAGE() {
        val logindata = ShareData(this).getUser()
        if (logindata != null) {
            ShowItem()
            TXT_useremail.setText(logindata!!.email)
            TXT_username.setText("Hello "+logindata!!.firstName)
            TXT_username.isClickable = false
            if(logindata.cartCount!=null)
            ManageCartView(logindata.cartCount)
            
        }
    }

    override fun onResume() {
        super.onResume()
      //  bottom_navigation.setSelectedItemId(R.id.navigation_home);
//        bottom_navigation.getMenu().getItem(5).setChecked(false);
        bottom_navigation.getMenu().getItem(4).setChecked(false);
        bottom_navigation.getMenu().getItem(3).setChecked(false);
        bottom_navigation.getMenu().getItem(2).setChecked(false);
        bottom_navigation.getMenu().getItem(1).setChecked(false);
        bottom_navigation.getMenu().getItem(0).setChecked(true);
        if(ShareData(this).getUser()!=null && isReadyforCourtCount)
            LOGINMANAGE()
    }

    override fun onPostResume() {
        super.onPostResume()
//        LOGINMANAGE()
    }

    override fun onPause() {
        super.onPause()
        val logindata = ShareData(this).getUser()
        if (logindata == null) {
            nav_signout.setVisible(false)
        }
    }

    override fun onBackPressed() {
        if  (drawer_layout.isDrawerOpen(GravityCompat.START)) {
           drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    /*************** MANAGE CART VIEW  */
    fun ManageCartView(i: Int) {
        val inflatedFrame = layoutInflater.inflate(R.layout.cart_layout, null)
        val item = inflatedFrame.findViewById(R.id.TXT_Counter) as TextView
        if (i <= 0)
            item.visibility = View.GONE
        else
            item.visibility = View.VISIBLE
        item.text = i.toString() + ""
        val drawable = BitmapDrawable(resources, Converter.getBitmapFromView(inflatedFrame))
        menuCartItem.setIcon(drawable)

         isReadyforCourtCount=true
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        menuCartItem = menu.findItem(R.id.action_cart)
        nav_signout=menu.findItem(R.id.nav_signout)
        nav_signout!!.setVisible(false)
        LOGINMANAGE()
        return true
    }

    @SuppressLint("MissingPermission")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            /*R.id.action_call -> {

                Dexter.withActivity(this)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(object : PermissionListener {
                            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+507-3021030"))
                                startActivity(intent)
                            }

                            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                                token!!.continuePermissionRequest()
                            }

                            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            }

                        }).check()

                return true
            }
            R.id.action_whatsapp -> {
                if (isWhatsapp()) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://api.whatsapp.com/send?phone=+507-6444-7679&text= Guarda nuestro n�mero en tus contactos para recibir nuestra newsletter. Y despu�s env�a este mensaje para comenzar.")
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Please make whatsapp to +507-6444-7679", Toast.LENGTH_SHORT).show()
                }
                return true
            }*/

            R.id.action_cart -> {
                val logindata = ShareData(this).getUser()
                if(logindata==null)
                {
                    startActivityForResult(Intent(this, LoginActivity::class.java), REQ_LOGIN)
                    return true
                }
                if(logindata!!.cartCount!=null)
                startActivity(Intent(this,CartActivity::class.java))
                return true
            }
            R.id.nav_signout->{
                ShareData(this).Logout()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
                return  true
            }
           /* R.id.btncategory->{
                Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show()
                return true
            }*/

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
           /* R.id.nav_category -> {
//                supportFragmentManager.beginTransaction().replace(R.id.container, CategoryFragment.newInstance("", "")).commit()
                startActivity(Intent(this,CategoryActivity::class.java))
                return true
            }*/
           /* R.id.nav_acc -> {
                startActivity(Intent(this, MyAccActivity::class.java))

            }*/
           /* R.id.nav_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.container, DashboardFragment.newInstance("", "")).commit()

            }*/
            R.id.nav_menu_signout -> {
                ShareData(this).Logout()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
                return true
            }
            R.id.nav_login ->{
                startActivityForResult(Intent(this, LoginActivity::class.java), REQ_LOGIN)
                return true
            }
            /*R.id.nav_contactinfo->{
                startActivity(Intent(this, ContactInfoActivity::class.java))

            }*/
           /* R.id.nav_onlinesale ->{
                startActivity(Intent(this, ProductListActivity::class.java).putExtra("pid", "BYONLINENAV").putExtra("cname", resources.getString(R.string.menu_onlinesale)))

            }*/

            R.id.nav_mail -> {

                var  emailIntent= Intent(Intent.ACTION_SEND);
                val recipients= arrayOf("ventas@evisionstore.com")
                emailIntent!!.putExtra(Intent.EXTRA_EMAIL, recipients);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Evision");
              //intent.putExtra(Intent.EXTRA_TEXT,"def");
            // intent.putExtra(Intent.EXTRA_CC,"ghi");
                emailIntent.setType("text/plain");
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                var packageManager:PackageManager=packageManager
                var matches: MutableList<ResolveInfo> =packageManager.queryIntentActivities(emailIntent,0)
                var best: ResolveInfo?=null;
                for (  info : ResolveInfo in matches)
                    if (info.activityInfo.packageName.endsWith(".gm") ||
                            info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

                startActivity(emailIntent)

            }

            R.id.nav_facebook -> {
                var url:String = "https://www.facebook.com/EVision";
                var i:Intent=  Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
            R.id.nav_instragram -> {
                var url:String = "https://www.instagram.com/evisionpanama/";
                var i:Intent=  Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
            R.id.nav_whatsapp -> {
                if (isWhatsapp()) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://api.whatsapp.com/send?phone=+507-6444-7679&text= Guarda nuestro n�mero en tus contactos para recibir nuestra newsletter. Y despu�s env�a este mensaje para comenzar.")
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Please make whatsapp to +507-64447679", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.nav_phone -> {
                Dexter.withActivity(this)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(object : PermissionListener {
                            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+507-3021030"))
                                startActivity(intent)
                            }

                            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                                token!!.continuePermissionRequest()
                            }

                            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            }

                        }).check()

                return true
            }

            R.id.nav_send -> {
                startActivity(Intent(this, ContactUsActivity::class.java))
            }
            R.id.nav_location->{
                startActivity(Intent(this, BranchesActivity::class.java))
            }
            R.id.nav_termcodition->{
                startActivity(Intent(this, TermConditionSaleActivity::class.java))
            }
            R.id.nav_privacypolicy->{
                startActivity(Intent(this, PrivacySaleActivity::class.java))
            }
            R.id.navigation_home->{
                supportFragmentManager.beginTransaction().replace(R.id.container, DashboardFragment.newInstance("", "")).commit()
            }
            R.id.navigation_cart->{
                val logindata = ShareData(this).getUser()
                if(logindata==null)
                {
                    startActivityForResult(Intent(this, LoginActivity::class.java), REQ_LOGIN)
                    return true
                }
                if(logindata!!.cartCount!=null)
                    startActivity(Intent(this,CartActivity::class.java))
            }
            R.id.navigation_myaccount->{

                val logindata = ShareData(this).getUser()
                if(logindata==null)
                {
                    startActivityForResult(Intent(this, LoginActivity::class.java), REQ_LOGIN)
                    return true
                }else
               // if(logindata!!.cartCount!=null)
                   // startActivity(Intent(this,CartActivity::class.java))
                   startActivity(Intent(this, MyAccActivity::class.java))

            }
            R.id.navigation_cantactus->{
                startActivity(Intent(this, ContactUsActivity::class.java))

            }
            R.id.navigation_stock->{
                startActivity(Intent(this, ProductListActivity::class.java).putExtra("pid", "BYONLINENAV").putExtra("cname", resources.getString(R.string.menu_onlinesale)))

            }

           // R.id.nav_samsunginstall->{
              //  val browserIntent:Intent =  Intent(Intent.ACTION_VIEW, Uri.parse("https://www.samsung.com/latin/support/self-installation-online-guides/"))
               // startActivity(browserIntent)
                 /*browserIntent.setPackage("com.android.chrome")
                 try {
                     this@MainActivity.startActivity(intent);
                 } catch ( ex: ActivityNotFoundException) {
                     // Chrome browser presumably not installed and open Kindle Browser
                     this@MainActivity.startActivity(browserIntent)

                 }*/
          //  }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_LOGIN && resultCode == Activity.RESULT_OK) {
            LOGINMANAGE()
        }
    }


    private fun isWhatsapp(): Boolean {
        val pm = packageManager
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return false
    }

    private fun hideItem() {
       val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val nav_Menu = navigationView.getMenu()
       // nav_Menu.findItem(R.id.nav_acc).setVisible(false)

        nav_Menu.findItem(R.id.nav_menu_signout).setVisible(false)
        //nav_signout!!.setVisible(false)
//        nav_Menu.findItem(R.id.nav_trackorder).setVisible(false)
        nav_Menu.findItem(R.id.nav_login).setVisible(true)
    }

    private fun ShowItem() {
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val nav_Menu = navigationView.getMenu()
       // nav_Menu.findItem(R.id.nav_acc).setVisible(true)

          nav_Menu.findItem(R.id.nav_menu_signout).setVisible(true)
          nav_signout!!.setVisible(true)
       // val inflatedFrame = layoutInflater.inflate(R.layout.logout_layout, null)
      //  val drawable = BitmapDrawable(resources, Converter.getBitmapFromView(inflatedFrame))
       // nav_signout!!.setIcon(drawable)
//        nav_Menu.findItem(R.id.nav_trackorder).setVisible(true)
        nav_Menu.findItem(R.id.nav_login).setVisible(false)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_PHONE_STATE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        imei = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID).toString();
                        callApiforupdatetoken()
                    }else {
                        val TelephonyMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                        imei = TelephonyMgr.deviceId.toString()
                        callApiforupdatetoken()
                    }

                }else
                    callApiforupdatetoken()


            }


        }
    }

    fun callApiforupdatetoken(){
        ShareData(this).Setvalueinpreference(imei!!,"IMEI")
        EvisionLog.D("## DEVICEINFO-Iemi", imei!!)
        val manufacturer:String= Build.MANUFACTURER;
        val  model = Build.MODEL
        val version = Build.VERSION.SDK_INT;
        val  versionRelease = Build.VERSION.PREVIEW_SDK_INT;
        Log.d("update", version.toString());
        val Params = HashMap<String, Any>()
        Params.put("register_token_id", token)
        Params.put("register_imei_number", imei!!)
        Params.put("register_device_name", model)
        Params.put("register_software_name","android")
        Params.put("register_software_version", version)

        onHTTP().POSTCALL(URL.SENDTOKEN, Params, object : OnHttpResponse {
            override fun onSuccess(response: String) {

                EvisionLog.D("## DEVICEINFO-", response)
                //loder.dismiss()
             //   val resp = Gson().fromJson(response, LoginResponse::class.java)
                // Toast.makeText(this@LoginActivity, resp.message, Toast.LENGTH_LONG).show()
               // if (resp.status == 200) {
                    // ShareData(this@LoginActivity).SetUserData(response)
                    // setResult(Activity.RESULT_OK)
                    //finish()
                //}
//                    else {
//                        Toast.makeText(this@LoginActivity, resp.message, Toast.LENGTH_LONG).show()
//                    }
            }

            override fun onError(error: String) {
                // loder.dismiss()
                /// Toast.makeText(this@LoginActivity, error, Toast.LENGTH_LONG).show()
            }

            override fun onStart() {
                //  loder.show()
            }

        })
    }
}
