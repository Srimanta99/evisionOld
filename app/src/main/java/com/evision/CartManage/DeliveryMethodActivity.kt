package com.evision.CartManage

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.evision.CartManage.Dialogs.SelectDeliveryFragmentDialog
import com.evision.CartManage.Pojo.CustomerAddress
import com.evision.CartManage.Pojo.CustomerAddressBilling
import com.evision.CartManage.Pojo.DeliveryData
import com.evision.CartManage.Pojo.PickupData
import com.evision.R
import com.evision.Utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_delivery_method.*
import org.json.JSONArray
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat

class DeliveryMethodActivity : AppCompatActivity() {
    lateinit var loader: AppDialog
    var SlectDeiliveryID = ""
    var SelectDeliveryType = ""
    var is_same = false
    var delivery_cost=""
    lateinit var customerAddress: CustomerAddress
    lateinit var customerAddress_billing: CustomerAddressBilling
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_method)
        var toolbar:Toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_back)
        loader = AppDialog(this)
        customerAddress = intent.getParcelableExtra("shipping")
        customerAddress_billing = intent.getParcelableExtra("billing")
        is_same = intent.getBooleanExtra("is_same", false)
        TXT_delivery.setOnClickListener {
            val desi = SelectDeliveryFragmentDialog.newInstance("", "")
            desi.init(object : SelectDeliveryFragmentDialog.onSelect {
                override fun fordelivery(data: String) {
                    SelectDeliveryType = data
                    when (data) {
                        "pickup" -> {
                            SlectDeiliveryID = ""
                            TXT_delivery.setText(resources.getString(R.string.pickupfromstore))
                            TXT_TAG.setText(resources.getString(R.string.pickupstore))
                            val params = HashMap<String, Any>()
                            params.put("customer_id", ShareData(this@DeliveryMethodActivity).getUser()!!.customerId)
                            params.put("delivery_type", data)
                            params.put("country_id",customerAddress_billing.country_id)
                            params.put("state_id",customerAddress_billing.state_id)
                            params.put("city_id",customerAddress_billing.city_id)

                            onHTTP().POSTCALL(URL.GETDELIVERY, params, object : OnHttpResponse {
                                override fun onSuccess(response: String) {
                                    EvisionLog.D("## RESPONSE-", response)
                                    val arra = JSONArray(response)
                                    RGtest.removeAllViews()
                                    for (i in 0..(arra.length() - 1)) {
                                        val rb = Gson().fromJson(arra.optString(i), PickupData::class.java)
                                        val rbtn = RadioButton(this@DeliveryMethodActivity)
                                        rbtn.setText(rb.pickup_store_name)
                                        rbtn.id = rb.pickup_store_id.toInt()
                                        rbtn.setTag(R.id.zone,rb.pickup_store_id)
                                        rbtn.setTag(R.id.dprice,"0")
                                        RGtest.addView(rbtn)
                                    }
                                    loader.dismiss()
                                }

                                override fun onError(error: String) {
                                    loader.dismiss()
                                }

                                override fun onStart() {
                                    loader.show()
                                }

                            })

                        }
                        "delivery" -> {
                            SlectDeiliveryID = ""
                            TXT_delivery.setText(resources.getString(R.string.delivery))
                            TXT_TAG.setText(resources.getString(R.string.Choosedelivery))
                            val params = HashMap<String, Any>()
                            params.put("customer_id", ShareData(this@DeliveryMethodActivity).getUser()!!.customerId)
                            params.put("delivery_type", data)
                            params.put("country_id",customerAddress_billing.country_id)
                            params.put("state_id",customerAddress_billing.state_id)
                            params.put("city_id",customerAddress_billing.city_id)

                            onHTTP().POSTCALL(URL.GETDELIVERY, params, object : OnHttpResponse {
                                override fun onSuccess(response: String) {
                                    EvisionLog.D("## Delivery-", response)
                                    val arra = JSONArray(response)
                                    RGtest.removeAllViews()
                                    for (i in 0..(arra.length() - 1)) {
                                        val rb = Gson().fromJson(arra.optString(i), DeliveryData::class.java)
                                        val rbtn = RadioButton(this@DeliveryMethodActivity)
                                      //  rbtn.setText(" ${rb.zone_id} ${rb.zone_city} Delivery Cost: ${rb.delivery_cost}")
                                        rbtn.setText("  Delivery Cost: ${rb.delivery_cost}")
                                        rbtn.id = i
                                        rbtn.setTag(R.id.zone,rb.zone_id)
                                        rbtn.setTag(R.id.dprice,rb.delivery_cost)
                                        RGtest.addView(rbtn)
                                    }
                                    loader.dismiss()
                                }

                                override fun onError(error: String) {
                                    loader.dismiss()
                                }

                                override fun onStart() {
                                    loader.show()
                                }

                            })
                        }
                    }
                }

            })
            desi.show(supportFragmentManager, "")
        }

        RGtest.setOnCheckedChangeListener { group, checkedId ->
            val btn = findViewById<RadioButton>(checkedId)
            EvisionLog.D("## TEST3- ", btn.getTag(R.id.zone).toString())
            SlectDeiliveryID = btn.getTag(R.id.zone).toString()
            delivery_cost=btn.getTag(R.id.dprice).toString()
        }


        BTN_NEXT.setOnClickListener {
            if (SlectDeiliveryID.isNullOrEmpty()) {
                Toast.makeText(this, resources.getString(R.string.alert_delivery), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("delivery_type", SelectDeliveryType)
            intent.putExtra("delivery_sub_type", SlectDeiliveryID)
                    .putExtra("is_same", is_same)
                    .putExtra("shipping", customerAddress)
                   .putExtra("delivery_cost",delivery_cost)
                    .putExtra("billing", customerAddress_billing)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
