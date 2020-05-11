package com.evision.Utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.evision.R
import android.view.WindowManager
import android.widget.*
import com.evision.Login_Registration.Pojo.LoginResponse
import com.google.gson.Gson
import org.json.JSONObject

class CustomAlertForRating(context: Context,val  pid: String, val user: LoginResponse) : Dialog(context) {
    lateinit var loader: AppDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loader = AppDialog(context)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_layout_for_rating);
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(getWindow().getAttributes())
        setCanceledOnTouchOutside(false)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        getWindow().setAttributes(lp)
        val imagecross:ImageView=findViewById(R.id.btn_closepopup)
        val ratingbar:RatingBar=findViewById(R.id.ratingbar)
        val til_writecommant:EditText=findViewById(R.id.til_writecommant)
        val txt_skip:TextView=findViewById(R.id.txt_skip)
        txt_skip.setOnClickListener {
            dismiss()
        }
        val btn_submit:Button=findViewById(R.id.btn_submit)
        btn_submit.setOnClickListener {
            if (ratingbar.rating>0 && !til_writecommant.text.toString().equals("") )
            callApiforsubmitreviewrating(ratingbar.rating,til_writecommant.text.toString())
            else
                Toast.makeText(context," Solicitud de cotización y califica esto",Toast.LENGTH_LONG).show()

        }
        imagecross.setOnClickListener {
            dismiss()
        }



    }

    private fun callApiforsubmitreviewrating(rating: Float, reviewmessage: String) {
        val params = HashMap<String, Any>()
        params.put("customer_id",user.customerId)
        params.put("product_id",pid)
        params.put("rating_number",rating.toString())
        params.put("review_comment",reviewmessage)
        EvisionLog.D("## PARAMS-", Gson().toJson(params))
        onHTTP().POSTCALL(com.evision.Utils.URL.RATE_PRODUCT, params, object : OnHttpResponse {
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