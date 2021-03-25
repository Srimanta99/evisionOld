package com.evision.Utils

import android.content.Context
import android.content.SharedPreferences
import com.evision.Login_Registration.Pojo.LoginResponse
import com.evision.ProductList.Pojo.ProductQuote
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ShareData (mContext: Context){
    val mContext=mContext
    var prefs: SharedPreferences? =mContext.getSharedPreferences("com.evision",0)
    fun SetUserData(data:String){
        val editor=prefs!!.edit()
        editor.putString("USER",data)
        editor.apply()
    }
    fun Setvalueinpreference(data:String,key: String){
        val editor=prefs!!.edit()
        editor.putString(key,data)
        editor.apply()
    }


    fun SetUserData(data: LoginResponse){
        val editor=prefs!!.edit()
        EvisionLog.D("## USER DATA-",Gson().toJson(data))
        editor.putString("USER",Gson().toJson(data))
        editor.apply()
    }

    fun getUser():LoginResponse?{
        var sata = prefs!!.getString("USER", "")
        return Gson().fromJson(sata,LoginResponse::class.java)
    }

    fun Logout()
    {
        val editor=prefs!!.edit()
        editor.clear()
        editor.commit()
    }

    fun read( key:String,value: String):String?{
        return prefs!!.getString(key,value)
    }
    fun write( key: String,value: String){
        val prefeditor:SharedPreferences.Editor= prefs!!.edit()
        with(prefeditor){
            putString(key,value)
            commit()
        }
    }

    fun write( key: String,value: Int){
        val prefeditor:SharedPreferences.Editor= prefs!!.edit()
        with(prefeditor){
            putInt(key,value)
            commit()
        }
    }

    fun write( key: String,value: Long){
        val prefeditor:SharedPreferences.Editor= prefs!!.edit()
        with(prefeditor){
            putLong(key,value)
            commit()
        }
    }

    fun read( key:String,value: Int):Int?{
        return prefs!!.getInt(key,value)
    }

    fun read( key:String,value: Long):Long?{
        return prefs!!.getLong(key,value)
    }
    fun getQuote(): ArrayList<ProductQuote>?{
        var carsList= ArrayList<ProductQuote>()
        var quote = prefs!!.getString("QUOTE", "")
        if(!quote.equals("")) {
            val type: Type = object : TypeToken<ArrayList<ProductQuote?>?>() {}.type
            carsList = Gson().fromJson(quote, type)
            return carsList
        }else
            return carsList
    }

    fun SetQuoteData(data: List<ProductQuote>){
        val editor=prefs!!.edit()
        EvisionLog.D("## USER DATA-", Gson().toJson(data))
        editor.putString("QUOTE", Gson().toJson(data))
        editor.apply()
    }

}