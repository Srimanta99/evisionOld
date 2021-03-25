package com.evision

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.evision.ProductList.ProductDetailsActivity
import com.evision.Utils.ShareData
import com.evision.mainpage.MainActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    val REQUEST_READ_PHONE_STATE: Int = 222


    companion object{
        public   var is_from_dynamic_link = false
        public   var gotoprolist = false
        public   var CatId :String?=""
        var Dynamiclink_url = ""
        var new_id:kotlin.String? = ""
        public var gotoContnct=false
        public  var gotoproductdetails=false
        public  var gotomyaccount=false
        var is_alert_for_delivery=true;
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        Glide.with(this).load(R.drawable.bg_splace).apply(RequestOptions().centerCrop()).into(ING_BG)
       // Glide.with(this).load(R.drawable.logo2).apply(RequestOptions().fitCenter()).into(LOG)
/*
        val stock_list = ArrayList<String>()
        stock_list.add(Manifest.permission.WRITE_CONTACTS)
        stock_list.add(Manifest.permission.READ_PHONE_STATE)
        var stockArr = arrayOfNulls<String>(stock_list.size)
        stockArr = stock_list.toArray(stockArr)*/
        val multiple_permissions = listOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_CONTACTS)

        val addContactsUri = ContactsContract.Data.CONTENT_URI
        var call = Intent(this, MainActivity::class.java)
        call.putExtra("ACTION", "NO")
       /*  Intent intent = getIntent();
    Uri data = intent.getData();

    if (data.getQueryParameter("faqid") != null) {
      Support.showSingleFAQ(this, data.getQueryParameter("faqid"));
      finish();
    } else if (data.getQueryParameter("sectionid") != null) {
      Support.showFAQSection(this, data.getQueryParameter("sectionid"));
      finish();
    } else {
      deepLinkUrl.setText("Deep link received - " + data);
    }*/

       // Dexter.withActivity(this)
              //  .withPermissions(multiple_permissions)
                /* .withPermission(Manifest.permission.WRITE_CONTACTS)
                .withListener(object : PermissionListener {

                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        checkpermessionwritecontact()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        // check for permanent denial of permission
                       // if (response.is) {
                            checkpermessionwritecontact()
                      //  }
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()*/

                /*.withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(response: MultiplePermissionsReport?) {
                        if (response!!.areAllPermissionsGranted()) {
                            val rowContactId = getRawContactId()
                            insertContactDisplayName(addContactsUri, rowContactId, "Evision care")
                            insertContactPhoneNumber(addContactsUri, rowContactId, "9733759131", "Work")
                            Handler().postDelayed({
                                startActivity(call)
                                finish()
                            }, 2000)
                        } else {
                            Handler().postDelayed({
                                startActivity(call)
                                finish()
                            }, 2000)
                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(permission: List<PermissionRequest>?, token: PermissionToken?) {
                        token!!.continuePermissionRequest()
                    }


                })
                .onSameThread()
                .check()*/

        /*override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        Handler().postDelayed({
                            startActivity(call)
                            finish()
                        }, 2000)
                    }

                }).check()*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ShareData(this).read("isdeney","")!!.equals("")&&ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
             permessionforphonecall()
        }else{
            val call = Intent(this@SplashActivity, MainActivity::class.java)
            call.putExtra("ACTION", "NO")

            //  val rowContactId = getRawContactId()
            // insertContactDisplayName(addContactsUri, rowContactId, "Evision care")
            //insertContactPhoneNumber(addContactsUri, rowContactId, "9733759131", "Work")
            Handler().postDelayed({
                startActivity(call)
                finish()
            }, 2000)
        }
        }else{
            val call = Intent(this@SplashActivity, MainActivity::class.java)
            call.putExtra("ACTION", "NO")

            //  val rowContactId = getRawContactId()
            // insertContactDisplayName(addContactsUri, rowContactId, "Evision care")
            //insertContactPhoneNumber(addContactsUri, rowContactId, "9733759131", "Work")
            Handler().postDelayed({
                startActivity(call)
                finish()
            }, 2000)
        }
    }

    private fun insertContactDisplayName(addContactsUri: Uri, rawContactId: Long, displayName: String) {
        val contentValues = ContentValues()

        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)

        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)

        // Put contact display name value.
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName)

        contentResolver.insert(addContactsUri, contentValues)

    }

    private fun insertContactPhoneNumber(addContactsUri: Uri, rawContactId: Long, phoneNumber: String, phoneTypeStr: String) {
        // Create a ContentValues object.
        val contentValues = ContentValues()

        // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)

        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)

        // Put phone number value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)

        // Calculate phone type by user selection.
        var phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME

        if ("home".equals(phoneTypeStr, ignoreCase = true)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME
        } else if ("mobile".equals(phoneTypeStr, ignoreCase = true)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
        } else if ("work".equals(phoneTypeStr, ignoreCase = true)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK
        }
        // Put phone type value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType)

        // Insert new contact data into phone contact list.
        contentResolver.insert(addContactsUri, contentValues)

    }

    private fun getRawContactId(): Long {
        // Inser an empty contact.
        val contentValues = ContentValues()
        val rawContactUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, contentValues)
        // Get the newly created contact raw id.
        return ContentUris.parseId(rawContactUri)
    }

    fun checkpermessionwritecontact() {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.READ_PHONE_STATE), REQUEST_READ_PHONE_STATE)
        } else {
            val addContactsUri = ContactsContract.Data.CONTENT_URI
            val call = Intent(this, MainActivity::class.java)
            call.putExtra("ACTION", "NO")
            val rowContactId = getRawContactId()
            insertContactDisplayName(addContactsUri, rowContactId, "Evision care")
            insertContactPhoneNumber(addContactsUri, rowContactId, "9733759131", "Work")
            Handler().postDelayed({
                startActivity(call)
                finish()
            }, 2000)
        }

        /* val addContactsUri = ContactsContract.Data.CONTENT_URI
        val call = Intent(this, MainActivity::class.java)
        call.putExtra("ACTION", "NO")
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_CONTACTS)
                .withListener(object : PermissionListener {

                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        val rowContactId = getRawContactId()
                        insertContactDisplayName(addContactsUri, rowContactId, "Evision care")
                        insertContactPhoneNumber(addContactsUri, rowContactId, "9733759131", "Work")
                        Handler().postDelayed({
                            startActivity(call)
                            finish()
                        }, 2000)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        // check for permanent denial of permission
                       // if (response.isPermanentlyDenied) {
                            val rowContactId = getRawContactId()
                            insertContactDisplayName(addContactsUri, rowContactId, "Evision care")
                            insertContactPhoneNumber(addContactsUri, rowContactId, "9733759131", "Work")
                            Handler().postDelayed({
                                startActivity(call)
                                finish()
                            }, 2000)
                       // }
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()*/
    }

    override fun onStart() {
         super.onStart()
        try {
            FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
                    .addOnSuccessListener { pendingDynamicLinkData: PendingDynamicLinkData? ->
                        var deepLink: Uri? = null
                        Dynamiclink_url=""
                        gotoprolist = false
                        gotoContnct=false
                        gotomyaccount=false
                        gotoproductdetails=false
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.link
                            Dynamiclink_url = deepLink.toString()
                            Log.w("gg", Dynamiclink_url)
                            val result: Array<String> = Dynamiclink_url.split("=".toRegex()).toTypedArray()
                            val resultpro: Array<String> = Dynamiclink_url.split("&".toRegex()).toTypedArray()
                          //  if(resultpro[resultpro.size-1].startsWith("pro"))
                            is_from_dynamic_link = true
                            if(Dynamiclink_url.contains("contacto"))
                                gotoContnct=true
                           else if(Dynamiclink_url.contains("article"))
                                gotoproductdetails=true
                            else if(Dynamiclink_url.contains("myaccount"))
                                gotomyaccount=true
                            else
                                gotoprolist=true

                                new_id = result[result.size - 1]

                        } else {
                            val uri = intent.data
                            if (uri != null) {
                                Log.i("MyApp", "")
                                Dynamiclink_url = uri.toString()
                                Log.w("gg", Dynamiclink_url)
                                val result: Array<String> = Dynamiclink_url.split("=".toRegex()).toTypedArray()
                             //   val resultpro: Array<String> = Dynamiclink_url.split("?".toRegex()).toTypedArray()
                                is_from_dynamic_link = true
                                if(Dynamiclink_url.contains("contacto"))
                                    gotoContnct=true
                                else if(Dynamiclink_url.contains("article"))
                                    gotoproductdetails=true
                                else if(Dynamiclink_url.contains("myaccount"))
                                    gotomyaccount=true
                                else
                                    gotoprolist=true

                                new_id = result[result.size - 1]
                            }
                        }
                    }
                    .addOnFailureListener(this, OnFailureListener {
                        is_from_dynamic_link = false
                    })
        }catch (e :Exception){
            e.printStackTrace()
            is_from_dynamic_link = false
        }
       /* if (is_from_dynamic_link){
            val call = Intent(this@SplashActivity, ProductDetailsActivity::class.java)
            call.putExtra("pid", new_id)
            startActivity(call)
            finish()
        }*/


    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_PHONE_STATE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  //  val addContactsUri = ContactsContract.Data.CONTENT_URI
                    val call = Intent(this, MainActivity::class.java)
                    call.putExtra("ACTION", "NO")

                   // val rowContactId = getRawContactId()
                  //  insertContactDisplayName(addContactsUri, rowContactId, "Evision care")
                   // insertContactPhoneNumber(addContactsUri, rowContactId, "9733759131", "Work")
                    Handler().postDelayed({
                        startActivity(call)
                        finish()
                    }, 2000)
                }else{
                    ShareData(this).Setvalueinpreference("notshow","isdeney")
                    //val addContactsUri = ContactsContract.Data.CONTENT_URI
                    val call = Intent(this@SplashActivity, MainActivity::class.java)
                    call.putExtra("ACTION", "NO")

                    //  val rowContactId = getRawContactId()
                    // insertContactDisplayName(addContactsUri, rowContactId, "Evision care")
                    //insertContactPhoneNumber(addContactsUri, rowContactId, "9733759131", "Work")
                    Handler().postDelayed({
                        startActivity(call)
                        finish()
                    }, 2000)

                }
                /* if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                       // imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID).toString();
                      //  callApiforupdatetoken()
                    }else {
                        val TelephonyMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                        imei = TelephonyMgr.deviceId.toString()
                        callApiforupdatetoken()
                    }

                }*/

            }


        }
    }

    fun permessionforphonecall() {
        val multiple_permissions = listOf(Manifest.permission.READ_PHONE_STATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
              //  if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
                    var alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage(getString(R.string.acceptnotification))
                    alertBuilder.setPositiveButton("Aceptar",object :DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            ActivityCompat.requestPermissions(this@SplashActivity, multiple_permissions.toTypedArray(), REQUEST_READ_PHONE_STATE);
                        }
                    })
                    alertBuilder.setNegativeButton("No",object :DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                           // val addContactsUri = ContactsContract.Data.CONTENT_URI
                            val call = Intent(this@SplashActivity, MainActivity::class.java)
                            call.putExtra("ACTION", "NO")
                          //  val rowContactId = getRawContactId()
                           // insertContactDisplayName(addContactsUri, rowContactId, "Evision care")
                            //insertContactPhoneNumber(addContactsUri, rowContactId, "9733759131", "Work")
                            Handler().postDelayed({
                                startActivity(call)
                                finish()
                            }, 2000)
                        }
                    })
                    val alert = alertBuilder.create()
                    alert.setCanceledOnTouchOutside(false)
                    alert.show()
                //}
                    // else {

               // }
            }
        }else{
           // val addContactsUri = ContactsContract.Data.CONTENT_URI
            val call = Intent(this, MainActivity::class.java)
            call.putExtra("ACTION", "NO")
           // val rowContactId = getRawContactId()
           // insertContactDisplayName(addContactsUri, rowContactId, "Evision care")
           // insertContactPhoneNumber(addContactsUri, rowContactId, "9733759131", "Work")
            Handler().postDelayed({
                startActivity(call)
                finish()
            }, 2000)
        }
    }


}
