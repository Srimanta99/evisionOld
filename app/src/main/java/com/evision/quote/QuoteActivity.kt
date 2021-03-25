package com.evision.quote

import ApiInterfaceQuote
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.evision.Login_Registration.LoginActivity
import com.evision.ProductList.Pojo.ProductQuote
import com.evision.R
import com.evision.Utils.AppDialog
import com.evision.Utils.EvisionLog
import com.evision.Utils.ShareData
import com.evision.mainpage.MainActivity
import com.wecompli.network.RetrofitQuote
import kotlinx.android.synthetic.main.activity_quote.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuoteActivity : AppCompatActivity() {
    lateinit var loader: AppDialog
    lateinit var adapterQuote: AdapterQuote
    lateinit var loder: AppDialog
    //lateinit var recQuote:RecyclerView
    var quotelistItem=ArrayList<ProductQuote>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quote)
        loder = AppDialog(this)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        loader = AppDialog(this)
        toolbar.setTitle("Quote Items")
        toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_back)
       // recQuote=findViewById(R.id.recQuote1)
        recQuote1.layoutManager = LinearLayoutManager(this)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        quotelistItem = ShareData(this).getQuote()!!
        if(quotelistItem.size>0)
            btn_request_for_quaote.visibility= View.VISIBLE
        else
            btn_request_for_quaote.visibility= View.VISIBLE
        adapterQuote=AdapterQuote(this, quotelistItem);
        recQuote1.adapter=adapterQuote
        btn_keepbuy.setOnClickListener {
            finish()
        }

        btn_request_for_quaote.setOnClickListener {
            if (ShareData(this).getUser() == null) {
                quotelistItem.clear()
                ShareData(this).SetQuoteData(quotelistItem)
                startActivityForResult(Intent(this, LoginActivity::class.java), 21)
            } else {
                //  loadData()
                callAppiforadQuote()
            }
        }

    }

    private fun callAppiforadQuote() {
       /* {
            "customer_id":"2090",
            "quotation_products_data":[
            {
                "product_id":"8738",

                "product_price":"99.95",
                "product_quantity":4
            },
            {
                "product_id":"16",

                "product_price":"45.95",
                "product_quantity":2
            }
            ]
        }*/
        val jsonArray = JSONArray()
        val prodlist= ArrayList<Product>()
        for(i in 0 until quotelistItem.size){
            val prod=Product(quotelistItem.get(i).pid, quotelistItem.get(i).productprice.substring(1), quotelistItem.get(i).productQuentity)
            prodlist.add(prod)

         /*   val productQuote = JSONObject()
            try {
                productQuote.put("product_id", quotelistItem.get(i).pid)
                productQuote.put("product_price",  quotelistItem.get(i).productprice.substring(1))
                productQuote.put("product_quantity",  quotelistItem.get(i).productQuentity)
            } catch (e: JSONException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            jsonArray.put(productQuote)*/
        }

       /* val postParam  = JsonObject()
        postParam.addProperty("customer_id",ShareData(this).getUser()!!.customerId) ;
        postParam.addProperty("quotation_products_data",jsonArray.toString()) ;
        val Params = HashMap<String, Any>()
        Params.put("customer_id", ShareData(this).getUser()!!.customerId)
        Params.put("quotation_products_data", jsonArray.toString())*/

        val quoteModel=QuoteModel(ShareData(this).getUser()!!.customerId, prodlist)

        val apiInterface= RetrofitQuote.retrofitInstance?.create(ApiInterfaceQuote::class.java)
        loder.show()
        val callApi = apiInterface.callApiforQuote(quoteModel)
        callApi.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                EvisionLog.D("## LOGIN-", response.toString())
                // val details = Gson().fromJson(response.body().toString(), PDetails::class.java)
                val objRes = JSONObject(response.body()!!.string())
                if (objRes.optInt("code") == 200) {
                    loder.dismiss()
                    quotelistItem.clear()
                    ShareData(this@QuoteActivity).SetQuoteData(quotelistItem)
                    showAlert()

                    // Muchas gracias por su solicitud. El equipo de cotizaciones ha recibido su solicitud
                    //   y podrá ver su cotización desde la sección de cotizaciones en su cuenta luego de unas horas
                } else {
                    Toast.makeText(this@QuoteActivity, "Something wrong. Try later", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loder.dismiss()
            }
        })
       /* onHTTP().POSTCALL(URL.ADDQUOTE, Params, object : OnHttpResponse {
            override fun onSuccess(response: String) {
                EvisionLog.D("## LOGIN-", response)
                loder.dismiss()
                // ShareData(this@LoginActivity).write("cartid","")
                val objRes = JSONObject(response)
//                val details = Gson().fromJson(response, PDetails::class.java)
                if (objRes.optInt("code") == 200) {
                    startActivity(Intent(this@QuoteActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    finish()
                }
                 else {
                      Toast.makeText(this@QuoteActivity, "Something wrong. Try later", Toast.LENGTH_LONG).show()
                    }
            }

            override fun onError(error: String) {
                loder.dismiss()
                Toast.makeText(this@QuoteActivity, error, Toast.LENGTH_LONG).show()
            }

            override fun onStart() {
                loder.show()
            }

        })*/
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Muchas gracias por su solicitud. El equipo de cotizaciones ha recibido su solicitud y podrá ver su cotización desde la sección de cotizaciones en su cuenta luego de unas horas")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, id ->
                    startActivity(Intent(this@QuoteActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    //Toast.makeText(this@QuoteActivity, objRes.optString("message"), Toast.LENGTH_LONG).show()
                    finish()
                }
        val alert = builder.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 21) {
            if (ShareData(this).getUser() != null) {
            //    finish()
                callAppiforadQuote()
            }
            //else
               // loadData()
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
   fun deleteQuote(position: Int){
       quotelistItem.removeAt(position)
       ShareData(this).SetQuoteData(quotelistItem)
       quotelistItem = ShareData(this).getQuote()!!
      // adapterQuote.notifyDataSetChanged()
       adapterQuote=AdapterQuote(this, quotelistItem);
       recQuote1.adapter=adapterQuote
       if(quotelistItem.size>0)
           btn_request_for_quaote.visibility= View.VISIBLE
       else {
           btn_request_for_quaote.visibility = View.INVISIBLE
           btn_keepbuy.visibility=View.INVISIBLE
           Toast.makeText(this, "No Item Found", Toast.LENGTH_LONG).show()
       }
   }
}
