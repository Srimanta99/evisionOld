package com.evision.CartManage.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.evision.CartManage.CartActivity
import com.evision.CartManage.Pojo.UpsellProductItem
import com.evision.ProductList.ProductDetailsActivity
import com.evision.R
import com.evision.Utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_product_details.*
import org.json.JSONObject

class UpsellProductAdapter(mContext: CartActivity, upsellProducts: List<UpsellProductItem>) : RecyclerView.Adapter<UpsellProductAdapter.VHolder>() {
    val mContext = mContext
    var Listp =upsellProducts
    //Listp = upsellProducts
    var loader = AppDialog(mContext)


    class VHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val IMG_item = itemView.findViewById<ImageView>(R.id.IMG_item)
        val IMG_avilable = itemView.findViewById<ImageView>(R.id.IMG_avilable)
        val TXT_name = itemView.findViewById<TextView>(R.id.TXT_name)
        val TXT_Price = itemView.findViewById<TextView>(R.id.TXT_Price)
        val TXT_DESCRIPTION = itemView.findViewById<TextView>(R.id.TXT_DESCRIPTION)
        val SPECIAL_Price=itemView.findViewById<TextView>(R.id.SPECIAL_Price)
        val LL_addtocart=itemView.findViewById<LinearLayout>(R.id.LL_addtocart)
//        val TXT_Add_cart = itemView.findViewById<TextView>(R.id.TXT_Add_cart)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UpsellProductAdapter.VHolder {
        return VHolder(LayoutInflater.from(p0.context).inflate(R.layout.upsellproduct_item, p0, false))
    }

    override fun getItemCount(): Int {

            return Listp.size
    }


    override fun onBindViewHolder(p0: UpsellProductAdapter.VHolder, p1: Int) {
        val item = Listp.get(p1)
        Glide.with(mContext).load(item.image_path).into(p0.IMG_item)
        p0.TXT_name.setText(item.name)


        p0.TXT_Price.setText(item.currency + " " + item.price)

        if(item.addtocart_option>0)
            p0.LL_addtocart.visibility=View.VISIBLE

     /*   val spclprice = item.specialPrice.toDouble()
        if (spclprice > 0.0) {

            val newprice = "<b>" + item.currency + item.price + "</b>"
            p0.SPECIAL_Price.setText(Html.fromHtml(newprice))
            p0.TXT_Price.setText(item.currency + " " + item.specialPrice)
            p0.SPECIAL_Price.paintFlags = p0.TXT_Price.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
        }
*/


        Glide.with(mContext).load(item.image_path).apply(RequestOptions().placeholder(R.drawable.ic_placeholder)).into(p0.IMG_item)
        p0.itemView.setOnClickListener {
            mContext.startActivity(Intent(mContext, ProductDetailsActivity::class.java).putExtra("pid", item.product_id))
        }
        p0.LL_addtocart.setOnClickListener {
            val params = HashMap<String, Any>()
            params.put("customer_id", ShareData(mContext).getUser()!!.customerId)
            params.put("product_id", item.product_id)
            params.put("qty", 1)
            onHTTP().POSTCALL(com.evision.Utils.URL.ADDtoCART, params, object : OnHttpResponse {
                override fun onSuccess(response: String) {
                    loader.dismiss()
                    if (JSONObject(response).optInt("code") == 200) {
                        val userdata = ShareData(mContext).getUser()
                        EvisionLog.E("##userData", Gson().toJson(userdata))
                        userdata!!.cartCount = userdata.cartCount + 1
                        ShareData(mContext).SetUserData(userdata)
                        mContext.RECV.adapter=null
                        mContext.loadData()
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
      //  p0.IMG_avilable.visibility = View.INVISIBLE

       // if (item.addtocartOption > 0) {
//            p0.TXT_Add_cart.visibility = View.VISIBLE
          //  p0.IMG_avilable.visibility = View.VISIBLE
//            p0.TXT_Add_cart.isClickable = true
      //  } else {
//            p0.TXT_Add_cart.visibility = View.INVISIBLE
           // p0.IMG_avilable.visibility = View.INVISIBLE
//            p0.TXT_Add_cart.isClickable = false
       // }
        //if(item.price.equals("0.00"))
           // p0.TXT_Price.visibility= View.INVISIBLE
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