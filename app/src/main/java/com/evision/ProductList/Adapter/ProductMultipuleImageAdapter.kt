package com.evision.ProductList.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.evision.R

class ProductMultipuleImageAdapter(val activity: Activity, val list: ArrayList<String>, val imgProduct: ImageView): RecyclerView.Adapter<ProductMultipuleImageAdapter.VHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(LayoutInflater.from(activity).inflate(R.layout.multipule_imageitem, null))

    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(4))
        Glide.with(activity).load(list[position]).apply(requestOptions.placeholder(R.drawable.ic_placeholder)).into(holder.img_product)
        holder.img_product.setOnClickListener {
            Glide.with(activity).load(list[position]).into(imgProduct)
        }

    }

    class VHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img_product=itemView.findViewById<ImageView>(R.id.img_products)
    }
}