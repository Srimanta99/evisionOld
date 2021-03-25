package com.evision.quote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.evision.ProductList.Pojo.ProductQuote
import com.evision.R

class AdapterQuote(val quoteActivity: QuoteActivity,val quotelistItem: ArrayList<ProductQuote>) : RecyclerView.Adapter<AdapterQuote.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val IMG_item = itemView.findViewById<ImageView>(R.id.IMG_item)
        val Dlete = itemView.findViewById<ImageView>(R.id.Dlete)
        val MINUS = itemView.findViewById<ImageView>(R.id.MINUS)
        val PLUS = itemView.findViewById<ImageView>(R.id.PLUS)
        val TXT_PName = itemView.findViewById<TextView>(R.id.TXT_PName)
        val TXT_Price = itemView.findViewById<TextView>(R.id.TXT_Price)
        val TXT_cratcount = itemView.findViewById<TextView>(R.id.TXT_cratcount)
        val TXT_MODEL = itemView.findViewById<TextView>(R.id.TXT_MODEL)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return AdapterQuote.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = quotelistItem.get(position)
        Glide.with(quoteActivity).load(itemData.productimage).apply(RequestOptions().placeholder(R.drawable.ic_placeholder)).into(holder.IMG_item)
        holder.TXT_PName.setText(itemData.productname)
        holder.TXT_Price.setText(itemData.productprice)
        holder.Dlete.setOnClickListener {
            quoteActivity.deleteQuote(position)
        }
    }

    override fun getItemCount(): Int {
       return quotelistItem.size;
    }
}