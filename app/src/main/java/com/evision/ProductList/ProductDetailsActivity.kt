package com.evision.ProductList

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.text.Html
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.evision.CartManage.CartActivity
import com.evision.Login_Registration.LoginActivity
import com.evision.ProductList.Adapter.ColorAdapter
import com.evision.ProductList.Adapter.ProductMultipuleImageAdapter
import com.evision.ProductList.Pojo.ProductQuote
import com.evision.R
import com.evision.SplashActivity
import com.evision.Utils.*
import com.evision.mainpage.MainActivity
import com.evision.model.ColorAttribute
import com.evision.quote.QuoteActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_product_details.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*
import kotlin.collections.HashMap


class ProductDetailsActivity : AppCompatActivity() {
    lateinit var menuCartItem: MenuItem
    public  lateinit var loader: AppDialog
    private val REQ_LOGIN: Int = 12
    var sahrelink = ""
    var cat_id = ""
    var cat_name = ""
    var modelno = ""
    var brandname=""
    var category_id=""
    var imageUrl=""
    var isReadyforCourtCount = false
    public var prod_id=""
    var productimage_list= ArrayList<String>()
    var quotelistItem=ArrayList<ProductQuote>()
    var colorAttribute:ColorAttribute?=null
    var checkattribute=JSONArray()
    var colorAttributeCount=false
    var currency:String="$"
    var is_allowed_bac_credomatic=false
    var  share_url=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        var toolbar:Toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        loader = AppDialog(this)


        //val strJunk = "AtrÃ©vete a SoÃ±ar"
      //  val arrByteForSpanish = strJunk.toByteArray(charset("ISO-8859-1"))
       // val strSpanish = String(arrByteForSpanish)
       // System.out.println("ghg"+strSpanish)

        toolbar.setTitleTextColor(Color.WHITE)

      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

       // window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_back)

        val IMG_Product = findViewById<ImageView>(R.id.IMG_Product)
     //   AppRater(this!!).app_launched();
        val params = HashMap<String, Any>()
        if(SplashActivity.is_from_dynamic_link) {
            params.put("model_number", intent.getStringExtra("pid"))
            params.put("product_id", "")
        }else {
            params.put("model_number", "")
          //  params.put("product_id","11212")
            params.put("product_id", intent.getStringExtra("pid"))
        }
        imgwhatsapp.setOnClickListener {
            shareWhatsapp(this, share_url)
        }
        imgfacebook.setOnClickListener {
            shareFacebook(share_url)
        }
        imgtwitter.setOnClickListener {
            shareTwitter(share_url)
        }
        prod_id= intent.getStringExtra("pid")
        rl_quote.setOnClickListener {
            if (ShareData(this).getUser() == null) {

                startActivityForResult(Intent(this, LoginActivity::class.java), 102)
                return@setOnClickListener
            }else {
                showquote()
            }

        }
        SplashActivity.is_from_dynamic_link=false
        EvisionLog.E("##params", params.toString())
        EvisionLog.E("##product_id", intent.getStringExtra("pid"))
        onHTTP().POSTCALL(URL.PRODUCTDETAILs_MULTICOLOR, params, object : OnHttpResponse {
            override fun onSuccess(response: String) {
                EvisionLog.E("##ProductDetailsResponse", response)
                if (response.isEmpty()) {
                    loader.dismiss()
                    Toast.makeText(this@ProductDetailsActivity, "Este producto no está disponible en este momento. Volveremos pronto.", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    val objRes = JSONObject(response)
//                val details = Gson().fromJson(response, PDetails::class.java)
                    if (objRes.optInt("status") == 200) {
//                    toolbar.setTitle(details.product_view.get(0).category_name)
                        //bac payment
                        toolbar.setTitle(objRes.optJSONArray("product_view").optJSONObject(0).optString("category_name"))
//                    Glide.with(this@ProductDetailsActivity).load(details.product_view.get(0).product_image).apply(RequestOptions().placeholder(R.drawable.ic_placeholder)).into(IMG_Product)
                        Glide.with(this@ProductDetailsActivity).load(objRes.optJSONArray("product_view").optJSONObject(0).optString("product_image")).apply(RequestOptions().placeholder(R.drawable.ic_placeholder)).into(IMG_Product)
                        imageUrl = objRes.optJSONArray("product_view").optJSONObject(0).optString("product_image");
                        share_url = objRes.optJSONArray("product_view").optJSONObject(0).optString("product_link")

                        // multipule image array
                        val multiple_images = objRes.optJSONArray("multiple_images")
                        if (multiple_images.length() > 0) {
                            ll_productimagelist.visibility = View.VISIBLE
                            for (i in 0 until multiple_images.length()) {
                                productimage_list.add(multiple_images.optJSONObject(i).optString("image_name"))

                            }
                            setadapterforimagelist()
                        }
                        // WebV.settings.setAppCacheEnabled(true)
                        val settings1 = WebV.getSettings()
                        settings1.setDefaultTextEncodingName("utf-8");
//                      WebV.loadData(details.product_view.get(0).descripcion, "text/html; charset=utf-8", "UTF-8")

                        val header = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"
                        val ss: String = objRes.optJSONArray("product_view").optJSONObject(0).optString("descripcion")
                        val arrByteForSpanish = ss.toByteArray()
                        val strSpanish = String(arrByteForSpanish)
                        System.out.println("ghg" + strSpanish)
                        // WebV.loadData(ss, "text/html; charset=utf-8", null);
                        // WebV.loadDataWithBaseURL(null,objRes.optJSONArray("product_view").optJSONObject(0).optString("descripcion"), "text/html",  "utf-8", null)
                        //WebV.loadData(header+objRes.optJSONArray("product_view").optJSONObject(0).optString("descripcion"), "text/html; charset=utf-8", null);

                        // modified
                        WebV.webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                                //  view?.loadUrl(url)
                                var overrideUrlLoading = false;
                                Log.e("tag", "url overrride url  = " + url);
                                // https://web.whatsapp.com/send?phone=50761644504
                                //if(url!!.startsWith("https://web.whatsapp.com") || url.startsWith("whatsapp:") || url.startsWith("web:")) {
                                if (url!!.startsWith("https://web.whatsapp.com") || url.startsWith("whatsapp:") || url.startsWith("web:") || url!!.startsWith("tel:") || url!!.startsWith("intent://") || url!!.startsWith("http://") || url!!.startsWith("https://wa.me")) {
                                    /* val intent =  Intent(Intent.ACTION_VIEW);
                                   intent.setData(Uri.parse(url));
                                   startActivity(intent);*/
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.data = Uri.parse("http://api.whatsapp.com/send?phone=+507-61644504")
                                    startActivity(intent)
                                    return true;
                                } else
                                    view?.loadUrl(url)

                                return false
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                Log.e("tag", "url overrride url  = " + url);
                            }

                        }
                        WebV.settings.javaScriptEnabled = true
                        WebV.loadUrl(objRes.optJSONArray("product_view").optJSONObject(0).optString("desc2"))
                        //  WebV.loadDataWithBaseURL("",objRes.optJSONArray("product_view").optJSONObject(0).optString("descripcion"),"text/html","utf-8", null)
                        // WebV.loadData(strSpanish, "text/html; charset=UTF-8", null);
                        WebV.getSettings().setJavaScriptEnabled(true);
                        WebV.getSettings().setSupportMultipleWindows(true)
                        WebV.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

                        WebV.getSettings().setAllowFileAccess(true);
                        WebV.getSettings().setSupportMultipleWindows(true);

                        //  WebV.setWebViewClient(WebViewClient())
                        WebV.addJavascriptInterface(object : Any() {
                            @JavascriptInterface           // For API 17+
                            fun performClick() {
                                //  Toast.makeText(this@ProductDetailsActivity, " rate review", Toast.LENGTH_SHORT).show()
                                val logindata = ShareData(this@ProductDetailsActivity).getUser()
                                if (logindata == null) {
                                    //  ManageCartView(ShareData(this).getUser()!!.cartCount)
                                    startActivityForResult(Intent(this@ProductDetailsActivity, LoginActivity::class.java), REQ_LOGIN)

                                }
                                if (logindata!!.cartCount != null)
                                    CustomAlertForRating(this@ProductDetailsActivity, intent.getStringExtra("pid"), ShareData(this@ProductDetailsActivity).getUser()!!).show()

                            }
                        }, "ok")

                        //for popup show
                        WebV.webChromeClient = object : WebChromeClient() {
                            override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                                // return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
                                val newWebView: WebView = WebView(this@ProductDetailsActivity);
                                newWebView.getSettings().setJavaScriptEnabled(true);
                                newWebView.getSettings().setSupportZoom(true);
                                newWebView.getSettings().setBuiltInZoomControls(true);
                                newWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                                newWebView.getSettings().setSupportMultipleWindows(true);
                                view!!.addView(newWebView);

                                val transport: WebView.WebViewTransport = resultMsg!!.obj as WebView.WebViewTransport
                                transport.setWebView(newWebView);
                                resultMsg.sendToTarget();
                                newWebView.webViewClient = object : WebViewClient() {
                                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                                        if (url!!.startsWith("https://web.whatsapp.com") || url.startsWith("whatsapp:") || url.startsWith("web:") || url!!.startsWith("tel:") || url!!.startsWith("intent://") || url!!.startsWith("http://") || url!!.startsWith("https://wa.me")) {
                                            /* val intent =  Intent(Intent.ACTION_VIEW);
                                             intent.setData(Uri.parse(url));
                                             startActivity(intent);*/
                                            val intent = Intent(Intent.ACTION_VIEW)
                                            intent.data = Uri.parse("http://api.whatsapp.com/send?phone=+507-61644504")
                                            startActivity(intent)
                                            return true;
                                        } else
                                            view?.loadUrl(url)

                                        return true
                                    }

                                }
                                return true
                            }

                        }

                        //  WebV.loadData(objRes.optJSONArray("product_view").optJSONObject(0).optString("descripcion"), "text/html; charset=utf-8", "utf-8")
                        val settings = WebV2.getSettings()
                        settings.setDefaultTextEncodingName("utf-8");
                        WebV2.settings.javaScriptEnabled = true
                        // WebV2.settings.setAppCacheEnabled(true)
                        // WebV2.webChromeClient = object : WebChromeClient() {}
//                    WebV2.loadUrl("http://dev.indigitalsoft.com/evision/extradesc.php?brand=lg&model=32lk540bpda")
//                    WebV2.loadUrl(details.product_view[0].extra_html_description)
                        // WebV2.loadDataWithBaseURL(null, objRes.optJSONArray("product_view").optJSONObject(0).optString("extra_html_description"), "text/html", "utf-8", null);
                        WebV2.loadUrl(objRes.optJSONArray("product_view").optJSONObject(0).optString("extra_html_description"))
                        // WebV2.loadData(objRes.optJSONArray("product_view").optJSONObject(0).optString("extra_html_description"),"text/html; charset=utf-8","UTF-8")

//                      TXT_Pname.setText(details.product_view.get(0).product_name)
                        TXT_Pname.setText(objRes.optJSONArray("product_view").optJSONObject(0).optString("product_name"))
//                    TXT_ModelNo.setText(details.product_view.get(0).modelo)
//                    sahrelink = details.product_view[0].product_ink
                        sahrelink = objRes.optJSONArray("product_view").optJSONObject(0).optString("product_link")
//                    cat_id = details.product_view[0].category_id
                        cat_id = objRes.optJSONArray("product_view").optJSONObject(0).optString("category_id")
//                    cat_name = details.product_view[0].category_name
                        cat_name = objRes.optJSONArray("product_view").optJSONObject(0).optString("category_name")
                        category_id = objRes.optJSONArray("product_view").optJSONObject(0).optString("category_id")
//                    modelno = details.product_view[0].modelo
                        modelno = objRes.optJSONArray("product_view").optJSONObject(0).optString("modelo")
                        brandname = objRes.optJSONArray("product_view").optJSONObject(0).optString("brand")
//                    val spclprice = details.product_view.get(0).special_price.toDouble()
                        currency = objRes.optJSONArray("product_view").optJSONObject(0).optString("currency")

                        if (!objRes.optJSONArray("product_view").optJSONObject(0).optString("price").toString().equals("0.00")) {
                            val spclprice = objRes.optJSONArray("product_view").optJSONObject(0).optString("special_price").toDouble()
                            if (spclprice > 0) {
//                        val newprice = "<b>" + details.product_view.get(0).currency + details.product_view.get(0).special_price + "</b>"
                                val newprice = "<b>" + objRes.optJSONArray("product_view").optJSONObject(0).optString("currency") + objRes.optJSONArray("product_view").optJSONObject(0).optString("special_price") + "</b>"
                                TXT_Price_new.setText(Html.fromHtml(newprice))
//                        TXT_Price_new.setText(details.product_view.get(0).currency + details.product_view.get(0).price)
                                // TXT_Price_new.setText(objRes.optJSONArray("product_view").optJSONObject(0).optString("currency") + objRes.optJSONArray("product_view").optJSONObject(0).optString("price"))
                                TXT_Price.paintFlags = TXT_Price.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
                            }
                            //modified
                            TXT_Price.setText(objRes.optJSONArray("product_view").optJSONObject(0).optString("currency") + objRes.optJSONArray("product_view").optJSONObject(0).optString("price"))

                        } else {
                            ll_price.visibility = View.INVISIBLE
                        }
                        // TXT_Price.paintFlags = TXT_Price.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG


                        bycategory.setText(resources.getString(R.string.seemore) + " " + objRes.optJSONArray("product_view").optJSONObject(0).optString("category_name"))
                        bymodel.setText(resources.getString(R.string.seemore) + " " + objRes.optJSONArray("product_view").optJSONObject(0).optString("brand"))

                        if (objRes.optJSONArray("product_view").optJSONObject(0).optInt("addtocart_option") == 1) {
                            LL_addtocartView.visibility = View.VISIBLE
                            ll_notify.visibility = View.GONE
                        } else {
                            LL_addtocartView.visibility = View.GONE
                            ll_notify.visibility = View.VISIBLE

                        }

                        loader.dismiss()
                    }
                    is_allowed_bac_credomatic = objRes.optJSONArray("product_view").optJSONObject(0).optBoolean("is_allowed_bac_credomatic")
                    if (is_allowed_bac_credomatic) {
                        var prodamount: Float = TXT_Price!!.text.toString().substring(1).toFloat()
                        var tax_amount = prodamount * 7 / 100
                        var product_amount_with_tax = tax_amount + prodamount
                        //  val df = DecimalFormat("0.00")
                        var per_month_price = (product_amount_with_tax / 12)

                        ll_bac.visibility = View.VISIBLE
                        tv_attentation.setText("12 cuotas de " + objRes.optJSONArray("product_view").optJSONObject(0).optString("currency") + String.format("%.2f", per_month_price).replace(",", ".") + " cada una")
                    } else {
                        ll_bac.visibility = View.GONE
                    }
                    val attribute_count = objRes.optJSONArray("product_view").optJSONObject(0).optString("attribute_count")
                    if (attribute_count.toInt() > 0) {
                        colorAttributeCount = true
                        ll_different_color.visibility = View.VISIBLE
                        colorAttribute = Gson().fromJson((objRes.optJSONArray("product_view").optJSONObject(0).optJSONArray("attribute_data").optJSONObject(0)).toString(), ColorAttribute::class.java)
                        val adapter = ColorAdapter(this@ProductDetailsActivity, colorAttribute!!.attribute_terms_arr, TXT_Price)
                        val linLayoutManager = LinearLayoutManager(this@ProductDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
                        rec_colorview.layoutManager = linLayoutManager
                        rec_colorview.adapter = adapter
                        rec_colorview.smoothScrollToPosition(1)
                        // TXT_Price.setText(objRes.optJSONArray("product_view").optJSONObject(0).optString("show_price"))

                    }
                }
                //  loader.dismiss()
            }

            override fun onError(error: String) {
                loader.dismiss()
            }

            override fun onStart() {
                loader.show()
            }

        })

        //show app rating dialog
        AppRater(this!!).app_launched();

        LL_addtocart.setOnClickListener {
            if (ShareData(this).getUser() == null) {
                val i = ShareData(this).read("cart", 0)
                val cart_ids = ShareData(this).read("cartid", "")
                val cartcount=i!!+1;
                ShareData(this).write("cart", cartcount)
                ShareData(this).write("cartid", cart_ids + "," + intent.getStringExtra("pid"))
               ManageCartViewwithoutlogin();
              //  startActivityForResult(Intent(this, LoginActivity::class.java), REQ_LOGIN)
               // return@setOnClickListener
            }else {
                if(colorAttributeCount) {
                    if (checkattribute!!.length()>0) {
                        val params = HashMap<String, Any>()
                        params.put("customer_id", ShareData(this@ProductDetailsActivity).getUser()!!.customerId)
                        params.put("product_id", intent.getStringExtra("pid"))
                        params.put("qty", EDX_cart.text.toString())
                        params.put("product_attributes", checkattribute)
                        onHTTP().POSTCALL(com.evision.Utils.URL.ADD_TO_CART_REACT, params, object : OnHttpResponse {
                            override fun onSuccess(response: String) {
                                loader.dismiss()
                                if (JSONObject(response).optInt("code") == 200) {
                                    val userdata = ShareData(this@ProductDetailsActivity).getUser()
                                    EvisionLog.E("##userData", Gson().toJson(userdata))
                                    userdata!!.cartCount = userdata.cartCount + EDX_cart.text.toString().toInt()
                                    ShareData(this@ProductDetailsActivity).SetUserData(userdata)
                                    ManageCartView(userdata.cartCount)
                                    showCartalert()
                                    Toast.makeText(this@ProductDetailsActivity, JSONObject(response).optString("message"), Toast.LENGTH_LONG).show()

                                } else
                                    Toast.makeText(this@ProductDetailsActivity, JSONObject(response).optString("message"), Toast.LENGTH_LONG).show()

                            }

                            override fun onError(error: String) {
                                loader.dismiss()
                            }

                            override fun onStart() {
                                loader.show()
                            }

                        })
                    } else
                        Toast.makeText(this@ProductDetailsActivity, "Select color variant", Toast.LENGTH_LONG).show()
                }else{
                    val params = HashMap<String, Any>()
                    params.put("customer_id", ShareData(this@ProductDetailsActivity).getUser()!!.customerId)
                    params.put("product_id", intent.getStringExtra("pid"))
                    params.put("qty", EDX_cart.text.toString())
                    params.put("product_attributes", checkattribute)
                    onHTTP().POSTCALL(com.evision.Utils.URL.ADDtoCART, params, object : OnHttpResponse {
                        override fun onSuccess(response: String) {
                            loader.dismiss()
                            if (JSONObject(response).optInt("code") == 200) {
                                val userdata = ShareData(this@ProductDetailsActivity).getUser()
                                EvisionLog.E("##userData", Gson().toJson(userdata))
                                userdata!!.cartCount = userdata.cartCount + EDX_cart.text.toString().toInt()
                                ShareData(this@ProductDetailsActivity).SetUserData(userdata)
                                ManageCartView(userdata.cartCount)
                                showCartalert()
                                Toast.makeText(this@ProductDetailsActivity, JSONObject(response).optString("message"), Toast.LENGTH_LONG).show()

                            } else
                                Toast.makeText(this@ProductDetailsActivity, JSONObject(response).optString("message"), Toast.LENGTH_LONG).show()

                        }

                        override fun onError(error: String) {
                            loader.dismiss()
                        }

                        override fun onStart() {
                            loader.show()
                        }

                    })
                }
                // showcartalert()
            }
        }

        bycategory.setOnClickListener {
            startActivity(Intent(this, ProductListActivity::class.java).putExtra("pid", cat_id).putExtra("cname", cat_name))
            finish()
        }
        bymodel.setOnClickListener {
            startActivity(Intent(this, ProductListActivity::class.java).putExtra("pid", "BYMODEL").putExtra("cname", cat_name)
                    .putExtra("cat_id", category_id)
                    .putExtra("model", brandname))
            finish()
        }

       /* TXT_rating.setOnClickListener {
            val logindata = ShareData(this).getUser()
            if (logindata == null) {
              //  ManageCartView(ShareData(this).getUser()!!.cartCount)
                startActivityForResult(Intent(this, LoginActivity::class.java), REQ_LOGIN)
                return@setOnClickListener
            }
            if (logindata!!.cartCount != null)
                CustomAlertForRating(this,intent.getStringExtra("pid"),ShareData(this).getUser()!!).show()
             return@setOnClickListener
        }*/

        ll_notify.setOnClickListener {
            val logindata = ShareData(this).getUser()
            if (logindata == null) {
                //  ManageCartView(ShareData(this).getUser()!!.cartCount)
                startActivityForResult(Intent(this, LoginActivity::class.java), REQ_LOGIN)
                return@setOnClickListener
            }
            if (logindata!!.cartCount != null)
                CustomAlertDialogNotify(this, intent.getStringExtra("pid"), ShareData(this).getUser()!!.email).show()
            return@setOnClickListener
        }

/*
       TXT_review.setOnClickListener {

        }
*/


    }

    fun ManageCartViewwithoutlogin() {
        val i = ShareData(this).read("cart", 0)
        val inflatedFrame = layoutInflater.inflate(R.layout.cart_layout, null)
        val item = inflatedFrame.findViewById(R.id.TXT_Counter) as TextView
        if (i!! <= 0)
            item.visibility = View.GONE
        else
            item.visibility = View.VISIBLE
        item.text = i.toString() + ""
        val drawable = BitmapDrawable(resources, Converter.getBitmapFromView(inflatedFrame))
        menuCartItem.setIcon(drawable)
        isReadyforCourtCount = true
        // if (ShareData(this).getUser() != null)
        //    nav_signout!!.setVisible(false)
    }
    private fun setadapterforimagelist() {
        val productMultipuleImageAdapter=ProductMultipuleImageAdapter(this, productimage_list, IMG_Product)
        val linLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        reclyer_productimagelist.layoutManager=linLayoutManager
        reclyer_productimagelist.adapter=productMultipuleImageAdapter
        reclyer_productimagelist.smoothScrollToPosition(1)

    }

    fun showcartalert(){
        var alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(resources.getString(R.string.app_name));
        alertBuilder.setMessage("Are you want to add this item in  your cart?")
        alertBuilder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog!!.dismiss()
                val params = HashMap<String, Any>()
                params.put("customer_id", ShareData(this@ProductDetailsActivity).getUser()!!.customerId)
                params.put("product_id", intent.getStringExtra("pid"))
                params.put("qty", EDX_cart.text.toString())
                onHTTP().POSTCALL(com.evision.Utils.URL.ADDtoCART, params, object : OnHttpResponse {
                    override fun onSuccess(response: String) {
                        loader.dismiss()
                        if (JSONObject(response).optInt("code") == 200) {

                            val userdata = ShareData(this@ProductDetailsActivity).getUser()
                            EvisionLog.E("##userData", Gson().toJson(userdata))
                            userdata!!.cartCount = userdata.cartCount + EDX_cart.text.toString().toInt()
                            ShareData(this@ProductDetailsActivity).SetUserData(userdata)
                            ManageCartView(userdata.cartCount)
                            showCartalert()

                        }
                        Toast.makeText(this@ProductDetailsActivity, JSONObject(response).optString("message"), Toast.LENGTH_LONG).show()
                    }

                    override fun onError(error: String) {
                        loader.dismiss()
                    }

                    override fun onStart() {
                        loader.show()
                    }

                })

            }
        })
        alertBuilder.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog!!.dismiss()
            }
        })
        val alert = alertBuilder.create()
        alert.show()
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
        isReadyforCourtCount = true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_sahre -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, sahrelink)
                sendIntent.type = "text/html"
                startActivity(sendIntent)
            }

            R.id.action_cart -> {
                val logindata = ShareData(this).getUser()
                if (logindata == null) {
                    startActivityForResult(Intent(this, LoginActivity::class.java), REQ_LOGIN)
                    return true
                }
                if (logindata!!.cartCount != null)
                    startActivity(Intent(this, CartActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        menuCartItem = menu!!.findItem(R.id.action_cart)
        if (ShareData(this).getUser() != null) {
            val logindata = ShareData(this).getUser()
            ManageCartView(logindata!!.cartCount)
        }else {
            ManageCartViewwithoutlogin();
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        if (ShareData(this).getUser() != null && isReadyforCourtCount) {
            val logindata = ShareData(this).getUser()
            ManageCartView(logindata!!.cartCount)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_LOGIN && resultCode == Activity.RESULT_OK) {
            MainActivity.isReadyforCourtCount=true
            val logindata = ShareData(this).getUser()
            if(logindata!!.cartCount!=null)
                ManageCartView(logindata!!.cartCount)

            //LOGINMANAGE()
        }else if(requestCode ==102){
            showquote()
        }
    }
    fun showquote(){
        quotelistItem = ShareData(this).getQuote()!!
        var productQuote = ProductQuote(intent.getStringExtra("pid"), TXT_Price.text.toString(), 1, imageUrl, TXT_Pname.text.toString());
        quotelistItem.add(productQuote)
        ShareData(this).SetQuoteData(quotelistItem)
        showquotealert()
    }
    fun showquotealert() {
        // var deviceResolution:DeviceResolution?=null
        val alertDialog = Dialog(this, R.style.Transparent)
        /*alertDialog.setTitle(activity.resources.getString(R.string.app_name))
        alertDialog.setMessage(message)*/
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view: View = LayoutInflater.from(this).inflate(R.layout.alert_layout_for_quote, null)
        alertDialog.setContentView(view)
        alertDialog.setCancelable(false)
        val tv_message: TextView = view.findViewById(R.id.tv_message)
        val btn_view_quote: TextView = view.findViewById(R.id.btn_view_quote)
        val btn_keep_buying: TextView = view.findViewById(R.id.btn_keep_buying)
        val img_cart_cross:ImageView=view.findViewById(R.id.img_cart_cross)

        btn_view_quote.setOnClickListener {
            alertDialog.dismiss()
            startActivity(Intent(this, QuoteActivity::class.java))
            //  finish()
            /* val intents:Intent=  Intent(this, MainActivity::class.java);
             intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
             intents.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
             intents.putExtra("ACTION", "NO")
             startActivity(intents)*/
            // startActivity(Intent(this, MainActivity::class.java))
            // activity.alertyesfuncation();
            // activity.calllogoutdeleteusertoken()
        }
        btn_keep_buying.setOnClickListener {
            alertDialog.dismiss()

        }
        img_cart_cross.setOnClickListener {
            alertDialog.dismiss()
        }
        //tv_message.setText("")
        alertDialog.show()
        /*alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        alertDialog.show()*/
    }
    fun showCartalert() {
        // var deviceResolution:DeviceResolution?=null
        val alertDialog = Dialog(this, R.style.Transparent)
        /*alertDialog.setTitle(activity.resources.getString(R.string.app_name))
        alertDialog.setMessage(message)*/
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view: View = LayoutInflater.from(this).inflate(R.layout.alert_layout_aftercart, null)
        alertDialog.setContentView(view)
        alertDialog.setCancelable(false)
        val tv_message: TextView = view.findViewById(R.id.tv_message)
        val btn_ok: Button = view.findViewById(R.id.btn_proced)
        val btn_no: Button = view.findViewById(R.id.btn_seg)
        val img_cart_cross:ImageView=view.findViewById(R.id.img_cart_cross)

        btn_ok.setOnClickListener {
            alertDialog.dismiss()
            finish()
           /* val intents:Intent=  Intent(this, MainActivity::class.java);
            intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intents.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intents.putExtra("ACTION", "NO")
            startActivity(intents)*/
          // startActivity(Intent(this, MainActivity::class.java))
            // activity.alertyesfuncation();
            // activity.calllogoutdeleteusertoken()
        }
        btn_no.setOnClickListener {
            alertDialog.dismiss()
            startActivity(Intent(this, CartActivity::class.java))

        }
        img_cart_cross.setOnClickListener {
            alertDialog.dismiss()
        }
        //tv_message.setText("")
        alertDialog.show()
        /*alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        alertDialog.show()*/
    }
   /* private fun LOGINMANAGE() {
        val logindata = ShareData(this).getUser()
        if (logindata != null) {
            ShowItem()
            TXT_useremail.setText(logindata!!.email)
            TXT_username.setText("Hello "+logindata!!.firstName)
            TXT_username.isClickable = false
            if(logindata.cartCount!=null)
                ManageCartView(logindata.cartCount)

        }
    }*/
   public fun callApiforvarient(attribute: ColorAttribute.Attribute) {
    //   var checkattribute=JSONArray()
       checkattribute= JSONArray()
        val checkobj=JSONObject()
         checkobj.put(attribute.term_id, attribute.term_name)
         checkattribute.put(checkobj)
       val params = HashMap<String, Any>()
       params.put("product_id", prod_id)
       params.put("checked_attribute", checkattribute)

       onHTTP().POSTCALL(com.evision.Utils.URL.SELECTATTRIBUTE, params, object : OnHttpResponse {
           override fun onSuccess(response: String) {
               loader.dismiss()
               try {
                   val objRes = JSONObject(response)
                   TXT_Price.setText(currency + objRes.optString("price"))
                   if (is_allowed_bac_credomatic) {
                       var prodamount: Float = TXT_Price!!.text.toString().substring(1).toFloat()
                       var tax_amount = prodamount * 7 / 100
                       var product_amount_with_tax = tax_amount + prodamount
                       //  val df = DecimalFormat("0.00")
                       var per_month_price = (product_amount_with_tax / 12)

                       ll_bac.visibility = View.VISIBLE
                       tv_attentation.setText("12 cuotas de " + currency + String.format("%.2f", per_month_price).replace(",", ".") + " cada una")
                   }
               } catch (e: Exception) {
                   e.printStackTrace()
               }
               /*  if (JSONObject(response).optInt("code") == 200) {

               }
               Toast.makeText(this@ProductDetailsActivity, JSONObject(response).optString("message"), Toast.LENGTH_LONG).show()
      */
           }

           override fun onError(error: String) {
               loader.dismiss()
           }

           override fun onStart() {
               loader.show()
           }

       })
   }
    fun shareWhatsapp(activity: Activity, url: String) {
        val pm: PackageManager = activity.packageManager
        try {
            val waIntent = Intent(Intent.ACTION_SEND)
            waIntent.type = "text/plain"
            val info: PackageInfo = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp")
            waIntent.putExtra(Intent.EXTRA_TEXT, " $url")
            activity.startActivity(Intent.createChooser(waIntent, "Share"))
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(activity, "Whatsapp not installed",
                    Toast.LENGTH_SHORT).show()
        }
    }

    fun shareTwitter(url: String?) {
        val tweetIntent = Intent(Intent.ACTION_SEND)
        tweetIntent.putExtra(Intent.EXTRA_TEXT, "This is a Test.")
        tweetIntent.type = "text/plain"

        val packManager = packageManager
        val resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY)

        var resolved = false
        for (resolveInfo in resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name)
                resolved = true
                break
            }
        }
        if (resolved) {
            startActivity(tweetIntent)
        } else {
            val i = Intent()
            i.putExtra(Intent.EXTRA_TEXT, url)
            i.action = Intent.ACTION_VIEW
            i.data = Uri.parse("http://twitter.com/share?url=" + urlEncode(url!!))
            startActivity(i)
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show()
        }
    }
    fun shareFacebook(  url: String) {
        var facebookAppFound = false
        var shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url))
        val pm = packageManager
        val activityList: List<ResolveInfo> = pm.queryIntentActivities(shareIntent, 0)
        for (app in activityList) {
            if (app.activityInfo.packageName.contains("com.facebook.katana")) {
                val activityInfo: ActivityInfo = app.activityInfo
                val name = ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name)
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                shareIntent.component = name
                facebookAppFound = true
                break
            }
        }
        if (!facebookAppFound) {
            val sharerUrl = "https://www.facebook.com/plugins/like.php?href=$url"
            shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
        }
       startActivity(shareIntent)
    }

    fun urlEncode(s: String): String? {
        return try {
            URLEncoder.encode(s, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            Log.wtf("wtf", "UTF-8 should always be supported", e)
            throw RuntimeException("URLEncoder.encode() failed for $s")
        }
    }
}
