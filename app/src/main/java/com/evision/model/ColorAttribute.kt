package com.evision.model

data class ColorAttribute(val attribute_id:String,val attribute_name:String,val attribute_slug:String,val attribute_terms_arr:ArrayList<Attribute>) {
    data class Attribute(val term_id:String,val term_name:String,val term_slug:String,val term_code:String,val term_image:String,val is_term_selected:Boolean)
}