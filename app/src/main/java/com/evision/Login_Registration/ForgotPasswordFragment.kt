package com.evision.Login_Registration


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.evision.Login_Registration.Pojo.LoginResponse
import com.evision.R
import com.evision.Utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_forgot_password.EDX_email
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForgotPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ForgotPasswordFragment : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var loader: AppDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        BTN_SAVE.setOnClickListener {
            if (!Validation.isValidEmail(EDX_email.text.toString())) {
                EDX_email.requestFocus()
                EDX_email.setError(resources.getString(R.string.entervalidemail))
                return@setOnClickListener
            }else{
                callApiforforgotpassword()
            }
        }

        BTN_CANCEL.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun callApiforforgotpassword() {
        loader = AppDialog(activity!!)
        val params = HashMap<String, Any>()
        params.put("email",EDX_email.text.toString())

        onHTTP().POSTCALL(URL.FORGOTPASSWORD, params, object : OnHttpResponse {
            override fun onSuccess(response: String) {
                loader.dismiss()
                var obj = JSONObject(response)
                EvisionLog.E("## REG-", response)
               // obj.optString("order_id")
              //  Toast.makeText(activity,resp.message, Toast.LENGTH_LONG).show()
              //  {"code":400,"status":"error","message":"There is no account with provided email address"}
                if (obj.optString("status").equals("success")) {

                    Toast.makeText(activity, obj.optString("message"), Toast.LENGTH_LONG).show()
                    dialog.dismiss()

                }else
                    Toast.makeText(activity, obj.optString("message"), Toast.LENGTH_LONG).show()

            }

            override fun onError(error: String) {
                loader.dismiss()
                Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
            }

            override fun onStart() {
                loader.show()
            }

        })
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ForgotPasswordFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
