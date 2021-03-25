package com.evision.Login_Registration

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.evision.Login_Registration.Pojo.LoginResponse
import com.evision.R
import com.evision.Utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var REQ_SIGNUP = 121
    lateinit var loder: AppDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loder = AppDialog(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        LOGIN_REQ.setOnClickListener {

            if (EMAIL.text.toString().trim().isEmpty()) {
                EMAIL.requestFocus()
                EMAIL.setHintTextColor(Color.RED)
                return@setOnClickListener
            }
            if (!Validation.isValidEmail(EMAIL.text.toString())) {
                EMAIL.requestFocus()
                EMAIL.setError(resources.getString(R.string.entervalidemail))
                return@setOnClickListener
            }

            if (EDX_PASS.text.toString().trim().isEmpty()) {
                EDX_PASS.requestFocus()
                EDX_PASS.setHintTextColor(Color.RED)
                return@setOnClickListener
            }

            var  product_id=ShareData(this).read("cartid","")
            var  pp_ids:String=""
            if (!product_id.equals(""))
                pp_ids=product_id!!.substring(1)
            else
                pp_ids=""

            val Params = HashMap<String, Any>()
            Params.put("email", EMAIL.text.toString())
            Params.put("password", EDX_PASS.text.toString())
            Params.put("product_ids", pp_ids)
            onHTTP().POSTCALL(URL.LOGIN_REQ, Params, object : OnHttpResponse {
                override fun onSuccess(response: String) {
                    EvisionLog.D("## LOGIN-", response)
                    loder.dismiss()
                   // ShareData(this@LoginActivity).write("cartid","")
                    val resp = Gson().fromJson(response, LoginResponse::class.java)
                    Toast.makeText(this@LoginActivity, resp.message, Toast.LENGTH_LONG).show()
                    if (resp.status == 200) {
                        ShareData(this@LoginActivity).SetUserData(response)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
//                    else {
//                        Toast.makeText(this@LoginActivity, resp.message, Toast.LENGTH_LONG).show()
//                    }
                }

                override fun onError(error: String) {
                    loder.dismiss()
                    Toast.makeText(this@LoginActivity, error, Toast.LENGTH_LONG).show()
                }

                override fun onStart() {
                    loder.show()
                }

            })
        }
        TXT_FORGOTPASSWORD.setOnClickListener {
            val forgotd = ForgotPasswordFragment.newInstance("", "")
            forgotd.show(supportFragmentManager, "")
        }
        SIGNUPREQ.setOnClickListener {
            startActivityForResult(Intent(this@LoginActivity, SignUpActivity::class.java), REQ_SIGNUP)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_SIGNUP && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
