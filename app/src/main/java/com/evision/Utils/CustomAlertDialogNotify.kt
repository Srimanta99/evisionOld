package com.evision.Utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.evision.R
import com.evision.Search.POJO.SearchResulty
import com.google.gson.Gson
import org.json.JSONObject

class CustomAlertDialogNotify(context: Context, val p_id: String,val  email: String) :Dialog(context){
    lateinit var loader: AppDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loader = AppDialog(context)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_layout_for_notify);
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(getWindow()!!.getAttributes())
        setCanceledOnTouchOutside(false)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        getWindow()!!.setAttributes(lp)
        val imagecross: ImageView =findViewById(R.id.btn_closepopup)
        val til_writecommant: EditText =findViewById(R.id.til_writecommant)
        val Email_notify:EditText=findViewById(R.id.Email_notify)
         Email_notify.setText(email!!)
        val btn_submit_notification: Button =findViewById(R.id.btn_submit_notification)

        btn_submit_notification.setOnClickListener {
            if (!Email_notify.text.toString().equals(""))
             callApiforsubmitreviewrating(p_id!!,Email_notify!!.text.toString())
            else
                Email_notify.requestFocus()
        }
        imagecross.setOnClickListener {
            dismiss()
        }



    }

    private fun callApiforsubmitreviewrating(pid:String,email:String) {
        val params = HashMap<String, Any>()
        params.put("customer_email",email)
        params.put("product_id",pid)
        EvisionLog.D("## PARAMS-", Gson().toJson(params))
        onHTTP().POSTCALL(com.evision.Utils.URL.NOTIFY_USER, params, object : OnHttpResponse {
            @SuppressLint("RestrictedApi")
            override fun onSuccess(response: String) {
                EvisionLog.D("## DATA-", response)
               val listdata = Gson().fromJson(response, NotifyApiResponse::class.java)
//                arrlist .addAll(listdata.productList)
                if (listdata.code == 200) {
                    Toast.makeText(context,listdata.message,Toast.LENGTH_LONG).show()
                    dismiss()
                }
                else if(listdata.code==400){
                    Toast.makeText(context,listdata.message,Toast.LENGTH_LONG).show()

                }else {

                    Toast.makeText(context, JSONObject(response).optString("message"), Toast.LENGTH_LONG).show()

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