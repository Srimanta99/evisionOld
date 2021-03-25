package com.evision.ProductList.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.evision.ProductList.ProductDetailsActivity
import com.evision.R
import com.evision.Utils.OnHttpResponse
import com.evision.Utils.ShareData
import com.evision.Utils.onHTTP
import com.evision.model.ColorAttribute
import org.json.JSONObject

class ColorAdapter(val productDetailsActivity: ProductDetailsActivity,
                   val attributeTermsArr: ArrayList<ColorAttribute.Attribute>,val TXT_Price: TextView) : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val img_product=itemView.findViewById<ImageView>(R.id.img_product)
        val img_color=itemView.findViewById<ImageView>(R.id.img_color)
        val tv_colorname=itemView.findViewById<TextView>(R.id.tv_colorname)
        val ll_coloritem=itemView.findViewById<LinearLayout>(R.id.ll_coloritem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(productDetailsActivity).inflate(R.layout.product_color_image_item,null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!attributeTermsArr.get(position).term_image.equals("")) {
            holder.img_product.visibility=View.VISIBLE
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(4))
            Glide.with(productDetailsActivity).load(attributeTermsArr.get(position).term_image)
                    .apply(requestOptions.placeholder(R.drawable.ic_placeholder))
                    .into(holder.img_product)
        }else{
             holder.img_color.visibility=View.VISIBLE
            holder.img_color.setBackgroundColor(Color.parseColor(attributeTermsArr.get(position).term_code));
        }
        holder.tv_colorname.setText(attributeTermsArr.get(position).term_name)
        holder.ll_coloritem.setOnClickListener {
           // TXT_Price.setText()
            productDetailsActivity.callApiforvarient(attributeTermsArr.get(position))
        }
    }



    override fun getItemCount(): Int {
       return  attributeTermsArr.size
    }

}