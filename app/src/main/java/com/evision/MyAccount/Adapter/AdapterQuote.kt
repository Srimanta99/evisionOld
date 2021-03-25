package com.evision.MyAccount.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.AsyncTask
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.evision.MyAccount.MyQuoteFragment
import com.evision.MyAccount.PdfActivity
import com.evision.MyAccount.Pojo.QuoteItem
import com.evision.R

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AdapterQuote(mContext: Context, list: ArrayList<QuoteItem>, val myQuoteFragment: MyQuoteFragment) : RecyclerView.Adapter<AdapterQuote.VHolder>() {
    val list = list
    val mContext = mContext
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_myquote, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.slno.setText("# " + (position + 1))
         holder.items.setText(list.get(position).total_items + " items")
        holder.requestdate.setText(list.get(position).quotation_time)
        holder.status.setText(list.get(position).quotation_status)
        if(!list.get(position).quotation_download_url.equals("")) {
          //  val content = SpannableString(list.get(position).uploaded_quotation_file_name)
          //  content.setSpan(UnderlineSpan(), 0, list.get(position).uploaded_quotation_file_name.length, 0)
            holder.action.setText(list.get(position).generated_quotation_file_name)
            holder.action.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        }else{
            holder.action.setText("")
        }
        holder.action.setOnClickListener {
          // if (myQuoteFragment.isStoragePermissionGranted())
            //    var downloadtask= Downloadtask(mContext,list.get(position).quotation_download_url, list.get(position).generated_quotation_file_name);
               // downloadtask.execute(list.get(position).quotation_download_url)
              // new  Downloadtask(mContext,list.get(position).quotation_download_url, list.get(position).generated_quotation_file_name)
            var intent:Intent = Intent(mContext,PdfActivity::class.java)
            intent.putExtra("pdf",list.get(position).quotation_download_url)
            mContext.startActivity(intent)

               // myQuoteFragment.downloadfromUrl(list.get(position).quotation_download_url, list.get(position).generated_quotation_file_name)
        }
    }




    class VHolder(itemV: View) : RecyclerView.ViewHolder(itemV) {
        val slno = itemV.findViewById<TextView>(R.id.slno)
        val items = itemV.findViewById<TextView>(R.id.items)
        val requestdate = itemV.findViewById<TextView>(R.id.requestdate)
        val status = itemV.findViewById<TextView>(R.id.status)
        val action = itemV.findViewById<TextView>(R.id.action)
    }



}
