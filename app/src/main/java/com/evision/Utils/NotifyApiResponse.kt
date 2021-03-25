package com.evision.Utils

import com.google.gson.annotations.SerializedName

data class NotifyApiResponse(@SerializedName("code") val code:Int,
@SerializedName("success") val success:String,
@SerializedName("message") val message:String) {
}