package com.evision.CartManage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.evision.MyAccount.MyAccActivity
import com.evision.R
import com.evision.Utils.AppDialog
import com.evision.Utils.EvisionLog
import com.evision.Utils.OnHttpResponse
import com.evision.Utils.onHTTP
import com.evision.mainpage.MainActivity
import kotlinx.android.synthetic.main.activity_order_sucess.*
import org.json.JSONObject

class OrderSucessActivity : AppCompatActivity() {
    lateinit var loader: AppDialog
    var response = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_sucess)
        loader = AppDialog(this)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        if (intent.getBooleanExtra("callurl", false) == false) {
            EvisionLog.D("## ORDER ID-", intent.getStringExtra("data"))
            setViewData(intent.getStringExtra("data"))
        } else {
            onHTTP().GETCALL(intent.getStringExtra("data"), object : OnHttpResponse {
                override fun onSuccess(response: String) {
                    setViewData(response)
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

    @SuppressLint("ResourceAsColor")
    private fun setViewData(stringExtra: String?) {
        EvisionLog.D("## ORDER ID-", stringExtra)
        val OBJECT = JSONObject(stringExtra)
        TXT_ORDERID.setText(resources.getString(R.string.orderno_) + " #" + OBJECT.optString("order_id"))
        TXT_ORDERSTATUS.setText(resources.getString(R.string.orderstatus)+" # " +OBJECT.optString("payment_status"))
        if (OBJECT.optString("payment_status").equals("PENDING")){
            ll_faild.visibility=View.VISIBLE
            TXT_ORDERID_faild.setText(resources.getString(R.string.orderno_) + " #" + OBJECT.optString("order_id"))
            TXT_ORDERSTATUS_faild.setText(resources.getString(R.string.orderstatus)+" # " +OBJECT.optString("payment_status"))
            img_faild.setBackgroundResource(R.drawable.green_thik)
            TXT_CONTINUTE_faild.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }

        }else  if (OBJECT.optString("payment_status").equals("FAILED")||OBJECT.optString("payment_status").equals("DECLINED")){
            ll_faild.visibility=View.VISIBLE
            TXT_ORDERID_faild.setText(resources.getString(R.string.orderno_) + " #" + OBJECT.optString("order_id"))
            TXT_ORDERSTATUS_faild.setText(resources.getString(R.string.orderstatus)+" # " +OBJECT.optString("payment_status"))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                TXT_ORDERID_faild.setTextColor(getColor(R.color.red))
                TXT_ORDERSTATUS_faild.setTextColor(getColor(R.color.red))
            }else{
                TXT_ORDERID_faild.setTextColor(resources.getColor(R.color.red))
                TXT_ORDERSTATUS_faild.setTextColor(resources.getColor(R.color.red))
            }
            TXT_CONTINUTE_faild.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }
        }else {
            ll_sucess.visibility=View.VISIBLE
            TXT_CONTINUTE1.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }

            TXT_TRACK1.setOnClickListener {
                startActivity(Intent(this, MyAccActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }
        }

    }
}
