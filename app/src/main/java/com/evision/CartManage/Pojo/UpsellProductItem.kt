package com.evision.CartManage.Pojo

data class UpsellProductItem(val product_id:String,val name:String,val modelo:String,
                             val marca:String,val image_path:String,val price:Double,val addtocart_option:Int,val currency:String) {
}