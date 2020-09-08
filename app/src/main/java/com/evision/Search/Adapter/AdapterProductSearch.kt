package com.evision.Search.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.evision.Login_Registration.LoginActivity
import com.evision.ProductList.Pojo.Product
import com.evision.ProductList.ProductDetailsActivity
import com.evision.ProductList.ProductListActivity
import com.evision.R
import com.evision.Utils.*
import com.google.gson.Gson
import org.json.JSONObject
import android.text.method.TextKeyListener.clear
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_product_details.*


class AdapterProductSearch(mContext: Context, list: List<Product>) : RecyclerView.Adapter<AdapterProductSearch.VHolder>() {
    val mContext = mContext
    var Listp =ArrayList<Product>()
   // var Listp = list
    var loader = AppDialog(mContext)

    class VHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val IMG_item = itemView.findViewById<ImageView>(R.id.IMG_item)
        val IMG_avilable = itemView.findViewById<ImageView>(R.id.IMG_avilable)
        val TXT_name = itemView.findViewById<TextView>(R.id.TXT_name)
        val TXT_Price = itemView.findViewById<TextView>(R.id.TXT_Price)
        val TXT_DESCRIPTION = itemView.findViewById<TextView>(R.id.TXT_DESCRIPTION)
        val SPECIAL_Price=itemView.findViewById<TextView>(R.id.SPECIAL_Price)
//        val TXT_Add_cart = itemView.findViewById<TextView>(R.id.TXT_Add_cart)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdapterProductSearch.VHolder {
        return VHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_listitem_search, p0, false))
    }

    override fun getItemCount(): Int {
       if (Listp.size > 7)
            return 7
        else
            return Listp.size
    }

    fun notify(list: List<Product>) {
        if (Listp != null) {
            Listp.clear()
            Listp.addAll(list)

        } else {
            Listp = list as ArrayList<Product>
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(p0: AdapterProductSearch.VHolder, p1: Int) {
        val item = Listp.get(p1)
        Glide.with(mContext).load(item.productImage).into(p0.IMG_item)
        p0.TXT_name.setText(item.productName)


        p0.TXT_Price.setText(item.currency + " " + item.price.toString())
        p0.SPECIAL_Price.setText("")
        val spclprice = item.specialPrice.toDouble()
        val  p_price=item.price.toDouble()
        if (p_price>0.0){
            p0.TXT_Price.visibility=View.VISIBLE
            p0.SPECIAL_Price.visibility=View.VISIBLE
        }else{
            p0.TXT_Price.visibility=View.INVISIBLE
            p0.SPECIAL_Price.visibility=View.INVISIBLE
        }

        if (spclprice > 0.0) {
            p0.TXT_Price.setText(item.currency + " " + spclprice)
            val newprice = "<b>" + item.currency + item.price.toString() + "</b>"
            p0.SPECIAL_Price.setText(Html.fromHtml(newprice))
            p0.SPECIAL_Price.paintFlags = p0.TXT_Price.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
            p0.TXT_Price.visibility=View.VISIBLE
            p0.SPECIAL_Price.visibility=View.VISIBLE
        }

        p0.TXT_DESCRIPTION.setText(item.short_description)
        Glide.with(mContext).load(item.productImage).apply(RequestOptions().centerCrop()).into(p0.IMG_item)
        p0.itemView.setOnClickListener {
            mContext.startActivity(Intent(mContext, ProductDetailsActivity::class.java).putExtra("pid", item.productId))
        }

    if (item.addtocartOption > 0) {
//            p0.TXT_Add_cart.visibility = View.VISIBLE
            p0.IMG_avilable.visibility = View.VISIBLE
//            p0.TXT_Add_cart.isClickable = true
        } else {
//            p0.TXT_Add_cart.visibility = View.INVISIBLE
            p0.IMG_avilable.visibility = View.INVISIBLE
//            p0.TXT_Add_cart.isClickable = false
        }
       // if(item.price.equals("0.00"))
         //  p0.TXT_Price.visibility=View.INVISIBLE
        /*
             p0.TXT_Add_cart.setOnClickListener {

                 if (ShareData(mContext).getUser() == null) {
                     mContext as ProductListActivity
                     mContext.startActivityForResult(Intent(mContext, LoginActivity::class.java), 11)
                 } else {
                     // TODO: ADD TO CART API CALL
                     val params = HashMap<String, Any>()
                     params.put("customer_id", ShareData(mContext).getUser()!!.customerId)
                     params.put("product_id", item.productId)
                     params.put("qty", 1)
                     onHTTP().POSTCALL(com.evision.Utils.URL.ADDtoCART, params, object : OnHttpResponse {
                         override fun onSuccess(response: String) {
                             EvisionLog.D("## DATA-", response)
                             loader.dismiss()
                             if (JSONObject(response).optInt("code") == 200) {
                                 val userdata = ShareData(mContext).getUser()
                                 userdata!!.cartCount = JSONObject(response).optInt("cart_count")

                                 ShareData(mContext).SetUserData(userdata)
                                 mContext as ProductListActivity
                                 mContext.ManageCartView()

                             }
                             Toast.makeText(mContext, JSONObject(response).optString("message"), Toast.LENGTH_LONG).show()

                         }

                         override fun onError(error: String) {
                             loader.dismiss()

                         }

                         override fun onStart() {
                             loader.show()
                         }

                     })
                 }
             }*/
    }
}